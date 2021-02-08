package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import io.ona.rdt.R;
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

            String rdtDeviceId = RDTJsonFormUtils.getField(widgetArgs.getStepName(),
                    CovidConstants.FormFields.RDT_DEVICE_ID, context).optString(JsonFormConstants.VALUE);
            JSONObject rdtConfig = DeviceDefinitionProcessor.getInstance(context, false)
                    .extractDeviceConfig(rdtDeviceId);
            if (rdtConfig != null) {
                intent.putExtra(edu.washington.cs.ubicomplab.rdt_reader.core.Constants.RDT_JSON_CONFIG, rdtConfig.toString());
                intent.putExtra(CAPTURE_TIMEOUT, CAPTURE_TIMEOUT_MS);
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else if (CovidConstants.FormFields.OTHER_KEY.equals(rdtDeviceId)) {
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else {
                Utils.hideProgressDialogFromFG();
                onActivityResult(-1, Activity.RESULT_CANCELED, null);
                Utils.showToastInFG(context, context.getString(R.string.rdt_not_supported));
            }
        } catch (IOException | FHIRParserException | JSONException e) {
            Timber.e(e);
        }
    }
}
