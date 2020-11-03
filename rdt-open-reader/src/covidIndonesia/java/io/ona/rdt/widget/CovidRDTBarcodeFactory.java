package io.ona.rdt.widget;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.UniqueId;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static io.ona.rdt.util.Utils.convertDate;
import static org.smartregister.util.Utils.isEmptyCollection;

/**
 * Created by Vincent Karuri on 09/07/2020
 */
public abstract class CovidRDTBarcodeFactory extends RDTBarcodeFactory {

    private static final int SENSOR_TRIGGER_INDEX = 4;

    private final String LOT_NO = "lot_no";
    private final String EXP_DATE = "exp_date";
    private final String GTIN = "gtin";
    private final String TEMP_SENSOR = "temp_sensor";

    public static final String RDT_BARCODE_EXPIRATION_DATE_FORMAT = "YYYY-MM-dd";
    private RDTJsonFormUtils formUtils = new RDTJsonFormUtils();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                if (data.getBooleanExtra(Constants.Config.ENABLE_BATCH_SCAN, false)) {
                    populateBarcodeData(data);
                }
                else {
                    String barcodeVals = getBarcodeValsAsCSV(data);
                    jsonApi.writeValue(widgetArgs.getStepName(),
                            widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                            barcodeVals, "", "", "", false);

                    String[] individualVals = splitCSV(barcodeVals);
                    populateRelevantFields(individualVals);
                    moveToNextStep(Boolean.parseBoolean(individualVals[SENSOR_TRIGGER_INDEX]),
                            convertDate(individualVals[1], RDT_BARCODE_EXPIRATION_DATE_FORMAT));
                }
            } catch (JSONException | ParseException e) {
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

    protected void populateBarcodeData(Intent data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data.getStringExtra("data"));

        formUtils.getNextUniqueIds(null, (args, uniqueIds) -> {
            List<String> ids = new ArrayList<>();
            for (UniqueId uniqueId : uniqueIds) {
                String currUniqueId = uniqueId.getOpenmrsId();
                if (StringUtils.isNotBlank(currUniqueId)) {
                    currUniqueId = currUniqueId.replace("-", "");
                    ids.add(currUniqueId);
                }
            }

            String uniqueId = isEmptyCollection(ids) ? "" : ids.get(0);

            try {
                jsonObject.put("batch_id", uniqueId);
                JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
                String stepName = widgetArgs.getStepName();

                jsonApi.writeValue(stepName, CovidConstants.FormFields.QR_CODE_READER, jsonObject.toString(),  "", "", "", false);
                jsonApi.writeValue("step5", "batch_id", uniqueId,  "", "", "", false);

                moveToNextStep();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, 1);
    }

    protected void populateRelevantFields(String[] individualVals) throws JSONException {
        JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        String stepName = widgetArgs.getStepName();

        jsonApi.writeValue(stepName, CovidConstants.FormFields.UNIQUE_ID, individualVals[0],  "", "", "", false);
        jsonApi.writeValue(stepName, EXP_DATE, individualVals[1],  "", "", "", false);
        jsonApi.writeValue(stepName, LOT_NO, individualVals[2],  "", "", "", false);
        jsonApi.writeValue(stepName, GTIN, individualVals[3],  "", "", "", false);
        jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[SENSOR_TRIGGER_INDEX],  "", "", "", false);

        // write unique id to confirmation page
        String patientInfoConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_SAMPLE_COLLECTION_FORM_PATIENT_INFO_CONFIRMATION_PAGE);
        JSONObject uniqueIdCheckBox = CovidRDTJsonFormUtils.getField(patientInfoConfirmationPage,
                        CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID,
                        widgetArgs.getContext());
        CovidRDTJsonFormUtils.fillPatientData(uniqueIdCheckBox, individualVals[0]);
    }

    protected void moveToNextStep(boolean isSensorTrigger, Date expDate) {
        if (isSensorTrigger) {
            navigateToUnusableProductPage();
        } else {
            moveToNextStep(expDate);
        }
    }
}
