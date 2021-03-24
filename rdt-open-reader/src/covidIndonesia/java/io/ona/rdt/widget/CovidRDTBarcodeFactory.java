package io.ona.rdt.widget;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static io.ona.rdt.util.Utils.convertDate;

/**
 * Created by Vincent Karuri on 09/07/2020
 */
public abstract class CovidRDTBarcodeFactory extends RDTBarcodeFactory {

    private static final int SENSOR_TRIGGER_INDEX = 4;
    private static final int GTIN_INDEX = 3;
    private static final int EXP_DATE_INDEX = 1;
    private static final int LOT_NO_INDEX = 2;
    private static final int UNIQUE_ID_INDEX = 0;
    public static final String BATCH_ID = "batch_id";

    public static final String LOT_NO = "lot_no";
    public static final String EXP_DATE = "exp_date";
    public static final String GTIN = "gtin";
    public static final String TEMP_SENSOR = "temp_sensor";

    public static final String RDT_BARCODE_EXPIRATION_DATE_FORMAT = "YYYY-MM-dd";

    protected CovidRDTJsonFormUtils formUtils = new CovidRDTJsonFormUtils();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                processResultData(data);
            } catch (JSONException | ParseException | IOException | FHIRParserException e) {
                Timber.e(e);
            }
        } else if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            ((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(true);
        } else if (data == null) {
            Timber.i("No result for qr code");
        }
    }

    protected void processResultData(Intent data) throws JSONException, ParseException, IOException, FHIRParserException {
        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        String barcodeVals = getBarcodeValsAsCSV(data);

        jsonApi.writeValue(widgetArgs.getStepName(),
                widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                barcodeVals, "", "", "", false);

        String[] individualVals = splitCSV(barcodeVals);
        populateSingleScanData(individualVals);
        moveToNextStep(Boolean.parseBoolean(individualVals[SENSOR_TRIGGER_INDEX]),
                convertDate(individualVals[1], RDT_BARCODE_EXPIRATION_DATE_FORMAT));
    }

    protected abstract String getBarcodeValsAsCSV(Intent data);

    protected abstract String[] splitCSV(String barcodeCSV);

    protected void populateSingleScanData(String[] individualVals) throws JSONException, IOException, FHIRParserException {
        Context context = widgetArgs.getContext();
        JsonApi jsonApi = (JsonApi) context;
        String stepName = widgetArgs.getStepName();

        // populate RDT device details confirmation page
        DeviceDefinitionProcessor deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(context);
        String deviceId = deviceDefinitionProcessor.getDeviceId(individualVals[GTIN_INDEX]);
        formUtils.populateFormWithRDTDetails(widgetArgs, deviceId, false);

        // we can do this population asynchronously
        class FormWidgetPopulationTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    // write barcode values to hidden fields
                    jsonApi.writeValue(stepName, CovidConstants.FormFields.UNIQUE_ID, individualVals[UNIQUE_ID_INDEX], "", "", "", false);
                    jsonApi.writeValue(stepName, EXP_DATE, individualVals[EXP_DATE_INDEX], "", "", "", false);
                    jsonApi.writeValue(stepName, LOT_NO, individualVals[LOT_NO_INDEX], "", "", "", false);
                    jsonApi.writeValue(stepName, GTIN, individualVals[GTIN_INDEX], "", "", "", false);
                    jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[SENSOR_TRIGGER_INDEX], "", "", "", false);
                    jsonApi.writeValue(stepStateConfig.optString(CovidConstants.Step.COVID_CONDUCT_RDT_PAGE), Constants.FormFields.LBL_RDT_ID, "RDT ID: " + individualVals[UNIQUE_ID_INDEX], "", "", "", false);

                    // write unique id to confirmation page
                    String patientInfoConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_SAMPLE_COLLECTION_FORM_PATIENT_INFO_CONFIRMATION_PAGE);
                    JSONObject uniqueIdCheckBox = CovidRDTJsonFormUtils.getField(patientInfoConfirmationPage,
                            CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID,
                            widgetArgs.getContext());
                    CovidRDTJsonFormUtils.fillPatientData(uniqueIdCheckBox, individualVals[0]);
                } catch (JSONException e) {
                    Timber.e(e);
                }
                return null;
            }
        }

        new FormWidgetPopulationTask().execute();
    }

    protected void moveToNextStep(boolean isSensorTrigger, Date expDate) {
        if (isSensorTrigger) {
            navigateToUnusableProductPage();
        } else {
            moveToNextStep(expDate);
        }
    }
}
