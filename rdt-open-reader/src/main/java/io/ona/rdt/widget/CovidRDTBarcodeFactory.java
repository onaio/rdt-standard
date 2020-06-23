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
    private final String UNIQUE_ID = "unique_id";
    private final String LOT_NO = "lot_no";
    private final String EXP_DATE = "exp_date";
    private final String GTIN = "gtin";
    private final String TEMP_SENSOR = "temp_sensor";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
                Log.d("Scanned QR Code", barcode.displayValue);
                String barcodeVals = StringUtils.join(barcode.displayValue.split("\u001D"), ",");

                // write values
                barcodeVals = removeLeadingAndTrailingCommas(barcodeVals);
                jsonApi.writeValue(widgetArgs.getStepName(),
                        widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                        barcodeVals, "", "", "", false);
                populateRelevantFields(extractValuesFromCSVs(barcodeVals));

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

    private void populateRelevantFields(String[] individualVals) throws JSONException {
        JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        String stepName = widgetArgs.getStepName();
        jsonApi.writeValue(stepName, GTIN, individualVals[0],  "", "", "", false);
        jsonApi.writeValue(stepName, LOT_NO, individualVals[1],  "", "", "", false);
        jsonApi.writeValue(stepName, EXP_DATE, individualVals[2],  "", "", "", false);
        jsonApi.writeValue(stepName, UNIQUE_ID, individualVals[3],  "", "", "", false);
        jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[4],  "", "", "", false);
    }

    private String removeLeadingAndTrailingCommas(String str) {
        if (StringUtils.isBlank(str)) { return str; }
        String truncatedStr = str.trim().charAt(0) == ',' ? str.substring(1) : str.trim();
        int truncatedStrLen = truncatedStr.length();
        return truncatedStr.charAt(truncatedStrLen - 1) == ','
                ? truncatedStr.substring(0, truncatedStrLen - 1) : truncatedStr;
    }

    private String[] extractValuesFromCSVs(String values) {
        String[] vals = new String[5];
        String[] dataSections = values.split(",");
        String gtin = dataSections[0].substring(2, 16);
        String lotNum = dataSections[0].substring(18);
        String expDate = dataSections[1].substring(2, 8);
        String uniqueId = dataSections[1].substring(10);
        String tempSensor = dataSections[2];
        vals[0] = gtin;
        vals[1] = lotNum;
        vals[2] = expDate;
        vals[3] = uniqueId;
        vals[4] = tempSensor;
        return vals;
    }

    private void moveToNextStep() {
        if (!widgetArgs.getFormFragment().next()) {
            widgetArgs.getFormFragment().save(true);
        }
    }
}
