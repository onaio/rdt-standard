package io.ona.rdt_app.util;

import android.app.Activity;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.UniqueId;

import io.ona.rdt_app.R;
import io.ona.rdt_app.callback.OnUniqueIdFetchedCallback;
import io.ona.rdt_app.model.Patient;
import timber.log.Timber;

import static com.vijay.jsonwizard.utils.Utils.showToast;
import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 06/08/2019
 */
public class FormLauncher implements OnUniqueIdFetchedCallback {

    private final String TAG = FormLauncher.class.getName();
    private RDTJsonFormUtils formUtils = new RDTJsonFormUtils();

    public void launchForm(Activity activity, String formName, Patient patient) throws JSONException {
        if (patient != null) {
            FormLaunchArgs args = new FormLaunchArgs();
            args.withActivity(activity)
                    .withFormJsonObj(formUtils.getFormJsonObject(formName, activity))
                    .withPatient(patient);
            formUtils.getNextUniqueId(args, this);
        } else {
            formUtils.launchForm(activity, formName, patient, null);
        }
    }

    @Override
    public synchronized void onUniqueIdFetched(FormLaunchArgs args, UniqueId uniqueId) {
        try {
            String rdtId = uniqueId.getOpenmrsId();
            Activity activity = args.getActivity();
            if (!StringUtils.isBlank(rdtId)) {
                formUtils.launchForm(activity, RDT_TEST_FORM, args.getPatient(), rdtId);
            } else {
                showToast(activity, activity.getString(R.string.unique_id_fetch_error_msg));
            }
        } catch (JSONException e) {
            Timber.e(TAG, e.getStackTrace().toString());
        }
    }
}
