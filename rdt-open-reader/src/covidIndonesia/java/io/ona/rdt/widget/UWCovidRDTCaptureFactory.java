package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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

            String rdtDetailsConfirmationPage = stepStateConfig.getString(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
            JSONObject rdtConfigField = RDTJsonFormUtils.getField(rdtDetailsConfirmationPage,
                    CovidConstants.FormFields.RDT_CONFIG, context);
            String rdtConfig = rdtConfigField.optString(JsonFormConstants.VALUE);

            if (StringUtils.isNotBlank(rdtConfig)) {
                intent.putExtra(edu.washington.cs.ubicomplab.rdt_reader.core.Constants.RDT_JSON_CONFIG, rdtConfig);
                intent.putExtra(CAPTURE_TIMEOUT, CAPTURE_TIMEOUT_MS);
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else if (CovidConstants.FormFields.OTHER_KEY.equals(rdtConfig)) {
                activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            } else {
                Utils.hideProgressDialogFromFG();
                onActivityResult(-1, Activity.RESULT_CANCELED, null);
                Utils.showToastInFG(context, context.getString(R.string.rdt_not_supported));
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
