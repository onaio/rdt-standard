package io.ona.rdt.util;

import android.app.Activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.smartregister.domain.UniqueId;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.callback.OnUniqueIdsFetchedCallback;
import io.ona.rdt.domain.Patient;

import static com.vijay.jsonwizard.utils.Utils.showToast;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;

/**
 * Created by Vincent Karuri on 06/08/2019
 */
public class FormLauncher implements OnUniqueIdsFetchedCallback {

    private RDTJsonFormUtils formUtils = new RDTJsonFormUtils();

    public void launchForm(Activity activity, String formName, Patient patient) throws JSONException {
        if (RDT_TEST_FORM.equals(formName) || SAMPLE_COLLECTION_FORM.equals(formName)) {
            FormLaunchArgs args = new FormLaunchArgs();
            args.withActivity(activity)
                    .withFormJsonObj(formUtils.getFormJsonObject(formName, activity))
                    .withPatient(patient)
                    .withFormName(formName);
            formUtils.getNextUniqueIds(args, this, 1);
        } else {
            formUtils.launchForm(activity, formName, patient, null);
        }
    }

    @Override
    public synchronized void onUniqueIdsFetched(FormLaunchArgs args, List<UniqueId> uniqueIds) {
        Activity activity = args.getActivity();
        List<String> ids = new ArrayList<>();
        for (UniqueId uniqueId : uniqueIds) {
            String currUniqueId = uniqueId.getOpenmrsId();
            if (StringUtils.isNotBlank(currUniqueId)) {
                currUniqueId = currUniqueId.replace("-", "");
                ids.add(currUniqueId);
            } else {
                showToast(activity, activity.getString(R.string.unique_id_fetch_error_msg));
                ids.clear();
                break;
            }
        }
        // only launch form if you have all the unique ids you need
        if (!ids.isEmpty()) {
            formUtils.launchForm(activity, args.getFormName(), args.getPatient(), ids);
        }
    }
}
