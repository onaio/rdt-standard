package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.HashMap;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;
import io.ona.rdt.shadow.DeviceDefinitionProcessorShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.widget.validator.CovidImageViewFactory;

public class OneScanCovidRDTBarcodeFactoryTest extends WidgetFactoryRobolectricTest {

    @Mock
    private RDTJsonFormFragment formFragment;

    private OneScanCovidRDTBarcodeFactory oneScanCovidRDTBarcodeFactory;
    private JSONObject stepStateConfig;

    private final String TEST_STEP = "test-step";
    private final String TEST_PAGE = "test-page";
    private final String VAL_0 = "val-0";
    private final String VAL_2 = "val-2";
    private final String VAL_3 = "val-3";
    private final String DATE = "2020-08-08";
    private final String SENSOR_TRIGGERED = "true";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        oneScanCovidRDTBarcodeFactory = Mockito.spy(new OneScanCovidRDTBarcodeFactory());
        stepStateConfig = new JSONObject();
        mockMethods();
    }

    @Config(shadows = {DeviceDefinitionProcessorShadow.class, RDTJsonFormUtilsShadow.class})
    @Test
    public void testOnActivityResultShouldPopulateCorrectData() throws Exception {
        // verify batch scan data extraction works
        Intent intent = new Intent();
        intent.putExtra(Constants.Config.ENABLE_BATCH_SCAN, true);
        intent.putExtra("data", "{}");

        oneScanCovidRDTBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);

        verifyBatchScanDataIsCorrectlyPopulated();

        // verify single scan data extraction works
        Mockito.clearInvocations(jsonFormActivity);

        JSONObject deviceDetailsWidget = new JSONObject(new HashMap<String, JSONArray>() {{
            put(JsonFormConstants.OPTIONS_FIELD_NAME, new JSONArray("[{}]"));
        }});
        RDTJsonFormUtilsShadow.setJsonObject(deviceDetailsWidget);

        JSONObject deviceConfig = new JSONObject(new HashMap<String, String>() {{
            put(CovidConstants.FHIRResource.REF_IMG, CovidConstants.FHIRResource.REF_IMG);
        }});
        DeviceDefinitionProcessorShadow.setJSONObject(deviceConfig);

        intent.putExtra(Constants.Config.ENABLE_BATCH_SCAN, false);

        oneScanCovidRDTBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);

        verifySingleScanDataIsCorrectlyPopulated();
        verifyRDTDetailsConfirmationPageIsPopulated();

        String deviceDetails = ReflectionHelpers.callInstanceMethod(oneScanCovidRDTBarcodeFactory, "getFormattedRDTDetails",
                ReflectionHelpers.ClassParameter.from(String.class, DeviceDefinitionProcessorShadow.MANUFACTURER),
                ReflectionHelpers.ClassParameter.from(String.class, DeviceDefinitionProcessorShadow.DEVICE_NAME));

        Assert.assertEquals(deviceDetails, deviceDetailsWidget.getString(JsonFormConstants.TEXT));
        Assert.assertEquals(CovidConstants.FHIRResource.REF_IMG, deviceDetailsWidget.getString(CovidImageViewFactory.BASE64_ENCODED_IMG));
        Assert.assertEquals(String.format("[{\"text\":\"%s\"}]", VAL_0), deviceDetailsWidget.optString(JsonFormConstants.OPTIONS_FIELD_NAME));

        Mockito.verify(oneScanCovidRDTBarcodeFactory).navigateToUnusableProductPage();
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, "", String.format("%s,%s,%s,%s,%s", VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED), "", "", "", false);

        // verify correct action on back-press
        oneScanCovidRDTBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
        Mockito.verify(formFragment).setMoveBackOneStep(true);
    }

    private void verifyBatchScanDataIsCorrectlyPopulated() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CovidRDTBarcodeFactory.BATCH_ID, OpenSRPContextShadow.OPENMRS_ID);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidConstants.FormFields.QR_CODE_READER, jsonObject.toString(), "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_PAGE, CovidRDTBarcodeFactory.BATCH_ID, OpenSRPContextShadow.OPENMRS_ID, "", "", "", false);
        Mockito.verify(oneScanCovidRDTBarcodeFactory).moveToNextStep();
    }

    private void verifySingleScanDataIsCorrectlyPopulated() throws JSONException {
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidConstants.FormFields.UNIQUE_ID, VAL_0, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.EXP_DATE, DATE, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.LOT_NO, VAL_2, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.GTIN, VAL_3, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.TEMP_SENSOR, SENSOR_TRIGGERED, "", "", "", false);
    }

    private void verifyRDTDetailsConfirmationPageIsPopulated() throws JSONException {
        Mockito.verify(jsonFormActivity).writeValue(ArgumentMatchers.eq(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE),
                ArgumentMatchers.eq(CovidConstants.FormFields.RDT_CONFIG), ArgumentMatchers.eq(DeviceDefinitionProcessorShadow.getJsonObject().toString()),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
        DeviceDefinitionProcessorShadow.setJSONObject(null);
        RDTJsonFormUtilsShadow.setJsonObject(null);
    }

    private void mockMethods() throws JSONException {
        Mockito.doNothing().when(oneScanCovidRDTBarcodeFactory).moveToNextStep();
        Mockito.doNothing().when(oneScanCovidRDTBarcodeFactory).navigateToUnusableProductPage();
        Mockito.doReturn(String.format("%s,%s,%s,%s,%s", VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED)).when(oneScanCovidRDTBarcodeFactory)
                .getBarcodeValsAsCSV(Mockito.any(Intent.class));
        Mockito.doReturn(new String[]{VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED}).when(oneScanCovidRDTBarcodeFactory)
                .splitCSV(ArgumentMatchers.anyString());

        formFragment = Mockito.mock(RDTJsonFormFragment.class);

        jsonFormActivity = Mockito.spy(jsonFormActivity);
        Mockito.doNothing().when(jsonFormActivity).writeValue(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());

        WidgetArgs widgetArgs = new WidgetArgs().withJsonObject(new JSONObject()).withStepName(TEST_STEP)
                .withContext(jsonFormActivity).withFormFragment(formFragment);

        ReflectionHelpers.setField(oneScanCovidRDTBarcodeFactory, "widgetArgs", widgetArgs);

        stepStateConfig.put(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE, TEST_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE, CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE, CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        ReflectionHelpers.setField(oneScanCovidRDTBarcodeFactory, "stepStateConfig", stepStateConfig);
    }
}
