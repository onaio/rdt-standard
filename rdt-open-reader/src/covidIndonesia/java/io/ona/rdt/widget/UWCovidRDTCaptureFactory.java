package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONObject;

import io.ona.rdt.R;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 18/11/2020
 */
public class UWCovidRDTCaptureFactory extends UWRDTCaptureFactory {

    protected void launchCamera(Intent intent, Context context) {
        try {
            Activity activity = (Activity) context;
            JSONObject rdtTypeField = RDTJsonFormUtils.getField(stepStateConfig.getString(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE),
                    Constants.RDTType.RDT_TYPE, context);
            String rdtType = rdtTypeField.getString(JsonFormConstants.VALUE);
            JSONObject deviceConfig = DeviceDefinitionProcessor.getInstance(context).extractDeviceConfig(rdtType);
            if (deviceConfig.length() != 0) {
                intent.putExtra(edu.washington.cs.ubicomplab.rdt_reader.core.Constants.RDT_JSON_CONFIG, deviceConfig.toString());
                intent.putExtra(CAPTURE_TIMEOUT, CAPTURE_TIMEOUT_MS);
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else if (CovidConstants.FormFields.OTHER_KEY.equals(rdtType)) {
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else {
                Utils.hideProgressDialogFromFG();
                onActivityResult(-1, Activity.RESULT_CANCELED, null);
                Utils.showToastInFG(context, context.getString(R.string.rdt_not_supported));
            }
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}
