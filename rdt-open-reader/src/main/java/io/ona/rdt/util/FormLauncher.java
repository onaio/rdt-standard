package io.ona.rdt.util;

import android.app.Activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.smartregister.domain.UniqueId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.ona.rdt.R;
import io.ona.rdt.callback.OnUniqueIdsFetchedCallback;
import io.ona.rdt.domain.Patient;

import static com.vijay.jsonwizard.utils.Utils.showToast;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 06/08/2019
 */
public class FormLauncher implements OnUniqueIdsFetchedCallback {

    private RDTJsonFormUtils formUtils = getFormUtils();
    private Set<String> formsThatRequireUniqueIDs = getFormsThatRequireUniqueIDs();

    public void launchForm(Activity activity, String formName, Patient patient) throws JSONException {
        if (formRequiresUniqueId(formName)) {
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

    private Set<String> getFormsThatRequireUniqueIDs() {
        if (formsThatRequireUniqueIDs == null) {
            formsThatRequireUniqueIDs = initializeFormsThatRequireUniqueIDs();
        }
        return formsThatRequireUniqueIDs;
    }

    protected Set<String> initializeFormsThatRequireUniqueIDs() {
        return new HashSet<>(Arrays.asList(RDT_TEST_FORM));
    }

    private boolean formRequiresUniqueId(String formName) {
        return getFormsThatRequireUniqueIDs().contains(formName);
    }

    @Override
    public synchronized void onUniqueIdsFetched(FormLaunchArgs args, List<UniqueId> uniqueIds) {
        Activity activity = args.getActivity();

        // only launch form if you have all the unique ids you need
        if (!uniqueIds.isEmpty()) {
            formUtils.launchForm(activity, args.getFormName(), args.getPatient(), Utils.getUniqueId(uniqueIds));
        } else {
            showToast(activity, activity.getString(R.string.unique_id_fetch_error_msg));
        }
    }

    protected RDTJsonFormUtils getFormUtils() {
        return new RDTJsonFormUtils();
    }
}
