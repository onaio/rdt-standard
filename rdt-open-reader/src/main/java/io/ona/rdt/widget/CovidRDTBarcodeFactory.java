package io.ona.rdt.widget;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;

import io.ona.rdt.fragment.RDTJsonFormFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;

/**
 * Created by Vincent Karuri on 17/06/2020
 */
public class CovidRDTBarcodeFactory extends RDTBarcodeFactory {

    private static final String TAG = CovidRDTBarcodeFactory.class.getName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
                Log.d("Scanned QR Code", barcode.displayValue);
                String barcodeVals = StringUtils.join(barcode.displayValue.split("\u001D"), ",");
                jsonApi.writeValue(widgetArgs.getStepName(),
                        widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                        removeLeadingAndTrailingCommas(barcodeVals), "", "", "",
                        false);
                moveToNextStep();
            } catch (JSONException e) {
                Log.e(TAG, e.getStackTrace().toString());
            }
        } else if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            ((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(true);
        } else if (data == null) {
            Log.i("", "No result for qr code");
        }
    }

    private String removeLeadingAndTrailingCommas(String str) {
        if (StringUtils.isBlank(str)) { return str; }
        String truncatedStr = str.trim().charAt(0) == ',' ? str.substring(1) : str.trim();
        int truncatedStrLen = truncatedStr.length();
        return truncatedStr.charAt(truncatedStrLen - 1) == ','
                ? truncatedStr.substring(0, truncatedStrLen - 1) : truncatedStr;
    }

    private void moveToNextStep() {
        if (!widgetArgs.getFormFragment().next()) {
            widgetArgs.getFormFragment().save(true);
        }
    }
}
