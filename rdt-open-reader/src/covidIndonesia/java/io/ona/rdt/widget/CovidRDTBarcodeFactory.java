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
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import io.ona.rdt.util.RDTJsonFormUtils;
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


    public static final String LOT_NO = "lot_no";
    public static final String EXP_DATE = "exp_date";
    public static final String GTIN = "gtin";
    public static final String TEMP_SENSOR = "temp_sensor";

    public static final String RDT_BARCODE_EXPIRATION_DATE_FORMAT = "YYYY-MM-dd";

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

        jsonApi.writeValue(stepName, CovidConstants.FormFields.UNIQUE_ID, individualVals[UNIQUE_ID_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, EXP_DATE, individualVals[EXP_DATE_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, LOT_NO, individualVals[LOT_NO_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, GTIN, individualVals[GTIN_INDEX],  "", "", "", false);
        jsonApi.writeValue(stepName, TEMP_SENSOR, individualVals[SENSOR_TRIGGER_INDEX],  "", "", "", false);

        // write fhir resource device id to rdt type field
        DeviceDefinitionProcessor deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(context);
        String deviceId = deviceDefinitionProcessor.getDeviceId(individualVals[GTIN_INDEX]);
        if (deviceId != null) {
            populateRDTDetailsConfirmationPage(deviceDefinitionProcessor, deviceId);
        }

        // write unique id to confirmation page
        String patientInfoConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_SAMPLE_COLLECTION_FORM_PATIENT_INFO_CONFIRMATION_PAGE);
        JSONObject uniqueIdCheckBox = CovidRDTJsonFormUtils.getField(patientInfoConfirmationPage,
                        CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID,
                        widgetArgs.getContext());
        CovidRDTJsonFormUtils.fillPatientData(uniqueIdCheckBox, individualVals[0]);
    }

    private void populateRDTDetailsConfirmationPage(DeviceDefinitionProcessor deviceDefinitionProcessor, String deviceId) throws JSONException {
        Context context = widgetArgs.getContext();
        String rdtDetailsConfirmationPage = stepStateConfig.optString(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        JSONObject deviceDetailsWidget = RDTJsonFormUtils.getField(rdtDetailsConfirmationPage,
                CovidConstants.FormFields.SELECTED_RDT_IMAGE, context);

        String deviceDetails = getFormattedRDTDetails(deviceDefinitionProcessor.extractManufacturerName(deviceId),
                deviceDefinitionProcessor.extractDeviceName(deviceId));
        JSONObject deviceConfig = deviceDefinitionProcessor.extractDeviceConfig(deviceId);

        // write device details to confirmation page
        deviceDetailsWidget.put(JsonFormConstants.TEXT, deviceDetails);
        deviceDetailsWidget.put(CovidImageViewFactory.BASE64_ENCODED_IMG,
                deviceConfig.optString(CovidConstants.FHIRResource.REF_IMG));

        // save extracted device config
        ((JsonApi) context).writeValue(rdtDetailsConfirmationPage, CovidConstants.FormFields.RDT_CONFIG,
                deviceConfig.toString(), "", "", "", false);
    }

    private String getFormattedRDTDetails(String manufacturer, String deviceName) {
        final String htmlLineBreak = "<br>";
        final String doubleHtmlLineBreak = "<br><br>";
        Context context = widgetArgs.getContext();

        String formattedMftStr = StringUtils.join(new String[]{context.getString(R.string.manufacturer_name), manufacturer}, htmlLineBreak);
        String formattedDeviceNameStr = StringUtils.join(new String[]{context.getString(R.string.rdt_name), deviceName}, htmlLineBreak);

        return StringUtils.join(new String[]{formattedMftStr, formattedDeviceNameStr}, doubleHtmlLineBreak);
    }

    protected void moveToNextStep(boolean isSensorTrigger, Date expDate) {
        if (isSensorTrigger) {
            navigateToUnusableProductPage();
        } else {
            moveToNextStep(expDate);
        }
    }
}
