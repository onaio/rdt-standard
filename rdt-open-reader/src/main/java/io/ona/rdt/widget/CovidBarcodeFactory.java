package io.ona.rdt.widget;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;

/**
 * Created by Vincent Karuri on 09/07/2020
 */
public abstract class CovidBarcodeFactory extends RDTBarcodeFactory {

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
                String barcodeVals = getBarcodeValsAsCSV(data);
                jsonApi.writeValue(widgetArgs.getStepName(),
                        widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                        barcodeVals, "", "", "", false);
                populateRelevantFields(splitCSV(barcodeVals));
                moveToNextStep();
            } catch (JSONException e) {
                Timber.e(e);
            }
        } else if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            ((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(true);
        } else if (data == null) {
            Timber.i("No result for qr code");
        }
    }

    protected abstract String getBarcodeValsAsCSV(Intent data);

    protected abstract String[] splitCSV(String barcodeCSV);

    protected void populateRelevantFields(String[] individualVals) throws JSONException {
        JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        String stepName = widgetArgs.getStepName();
        jsonApi.writeValue(stepName, GTIN, individualVals[0],  "", "", "", false);
        jsonApi.writeValue(stepName, LOT_NO, individualVals[1],  "", "", "", false);
        jsonApi.writeValue(stepName, EXP_DATE, individualVals[2],  "", "", "", false);
        jsonApi.writeValue(stepName, UNIQUE_ID, individualVals[3],  "", "", "", false);
        jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[4],  "", "", "", false);
    }
}
