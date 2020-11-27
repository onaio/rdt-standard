package io.ona.rdt.widget;

import android.content.Context;
import android.content.Intent;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;
import io.ona.rdt.widget.validator.CovidImageViewFactory;
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
    private static final String BATCH_ID_TEXT = "batch_id_text";

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
                } else {
                    String barcodeVals = getBarcodeValsAsCSV(data);
                    jsonApi.writeValue(widgetArgs.getStepName(),
                            widgetArgs.getJsonObject().optString(JsonFormConstants.KEY),
                            barcodeVals, "", "", "", false);

                    String[] individualVals = splitCSV(barcodeVals);
                    populateRelevantFields(individualVals);
                    moveToNextStep(Boolean.parseBoolean(individualVals[SENSOR_TRIGGER_INDEX]),
                            convertDate(individualVals[1], RDT_BARCODE_EXPIRATION_DATE_FORMAT));
                }
            } catch (JSONException | ParseException | IOException | FHIRParserException e) {
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

            String uniqueId = Utils.getUniqueId(uniqueIds);

            try {
                jsonObject.put(BATCH_ID, uniqueId);
                JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
                String stepName = widgetArgs.getStepName();

                jsonApi.writeValue(stepName, CovidConstants.FormFields.QR_CODE_READER, jsonObject.toString(),  "", "", "", false);
                jsonApi.writeValue(stepStateConfig.getString(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE), BATCH_ID, uniqueId,  "", "", "", false);
                jsonApi.writeValue(stepStateConfig.getString(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE), BATCH_ID_TEXT, uniqueId,  "", "", "", false);

                moveToNextStep();
            } catch (JSONException e) {
                Timber.e(e);
            }
        }, 1);
    }

    protected void populateRelevantFields(String[] individualVals) throws JSONException, IOException, FHIRParserException {
        Context context = widgetArgs.getContext();
        JsonApi jsonApi = (JsonApi) context;
        String stepName = widgetArgs.getStepName();

        jsonApi.writeValue(stepName, CovidConstants.FormFields.UNIQUE_ID, individualVals[UNIQUE_ID_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, EXP_DATE, individualVals[EXP_DATE_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, LOT_NO, individualVals[LOT_NO_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, GTIN, individualVals[GTIN_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[SENSOR_TRIGGER_INDEX],  "", "", "", false);

        // write fhir resource device id to rdt type field
        DeviceDefinitionProcessor deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(context);
        String deviceId = deviceDefinitionProcessor.getDeviceId(individualVals[GTIN_INDEX]);
        if (deviceId != null) {
            jsonApi.writeValue(stepStateConfig.getString(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE),
                    Constants.RDTType.RDT_TYPE, deviceId, "", "", "", false);
            populateRDTDetailsConfirmationPage(deviceDefinitionProcessor, deviceId);
        }

        // write unique id to confirmation page
        String patientInfoConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_SAMPLE_COLLECTION_FORM_PATIENT_INFO_CONFIRMATION_PAGE);
        JSONObject uniqueIdCheckBox = CovidRDTJsonFormUtils.getField(patientInfoConfirmationPage,
                        CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID,
                        widgetArgs.getContext());
        CovidRDTJsonFormUtils.fillPatientData(uniqueIdCheckBox, individualVals[0]);
    }

    private void populateRDTDetailsConfirmationPage(DeviceDefinitionProcessor deviceDefinitionProcessor, String deviceId) throws JSONException, FHIRParserException, IOException {
        Context context = widgetArgs.getContext();

        final String htmlLineBreak = "<br>";
        final String doubleHtmlLineBreak = "<br><br>";

        String manufacturer = StringUtils.join(new String[]{context.getString(R.string.manufacturer_name),
                deviceDefinitionProcessor.extractManufacturerName(deviceId)}, htmlLineBreak);

        String rdtName = StringUtils.join(new String[]{context.getString(R.string.rdt_name),
                deviceDefinitionProcessor.extractDeviceName(deviceId)}, htmlLineBreak);

        String rdtDetailsConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        JSONObject deviceDetailsWidget = RDTJsonFormUtils.getField(rdtDetailsConfirmationPage,
                CovidConstants.FormFields.SELECTED_RDT_IMAGE, context);

        String deviceDetails = StringUtils.join(new String[]{manufacturer, rdtName}, doubleHtmlLineBreak);
        JSONObject deviceConfig = deviceDefinitionProcessor.extractDeviceConfig(deviceId);

        // write device details to confirmation page
        deviceDetailsWidget.put(JsonFormConstants.TEXT, deviceDetails);
        deviceDetailsWidget.put(CovidImageViewFactory.BASE64_ENCODED_IMG,
                deviceConfig.optString(CovidConstants.FHIRResource.REF_IMG));

        // save extracted device config
        ((JsonApi) widgetArgs.getContext()).writeValue(rdtDetailsConfirmationPage, CovidConstants.FormFields.RDT_CONFIG,
                deviceConfig.toString(), "", "", "", false);
    }

    protected void moveToNextStep(boolean isSensorTrigger, Date expDate) {
        if (isSensorTrigger) {
            navigateToUnusableProductPage();
        } else {
            moveToNextStep(expDate);
        }
    }
}
