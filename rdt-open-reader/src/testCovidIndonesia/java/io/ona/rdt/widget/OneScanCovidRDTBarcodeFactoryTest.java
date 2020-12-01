package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
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
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.widget.validator.CovidImageViewFactory;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

public class OneScanCovidRDTBarcodeFactoryTest extends WidgetFactoryRobolectricTest {

    @Mock
    private RDTJsonFormFragment formFragment;

    private OneScanCovidRDTBarcodeFactory covidRdtBarcodeFactory;
    private JSONObject stepStateConfig;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        covidRdtBarcodeFactory = Mockito.spy(new OneScanCovidRDTBarcodeFactory());
        stepStateConfig = new JSONObject();
        mockMethods();
    }

    @Config(shadows = {DeviceDefinitionProcessorShadow.class, RDTJsonFormUtilsShadow.class})
    @Test
    public void testOnActivityResultShouldPopulateCorrectData() throws Exception {
        // verify batch scan data works
        Intent intent = new Intent();
        intent.putExtra("enable_batch_scan", true);
        intent.putExtra("data", "{}");

        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);

        verifyBatchScanDataIsCorrectlyPopulated();

        // verify single scan works
        Mockito.clearInvocations(jsonFormActivity);

        JSONObject deviceDetailsWidget = new JSONObject(new HashMap<String, JSONArray>(){{
            put(JsonFormConstants.OPTIONS_FIELD_NAME, new JSONArray("[{}]"));
        }});
        RDTJsonFormUtilsShadow.setJsonObject(deviceDetailsWidget);

        JSONObject deviceConfig = new JSONObject(new HashMap<String,String>(){{
            put(CovidConstants.FHIRResource.REF_IMG, CovidConstants.FHIRResource.REF_IMG);
        }});
        DeviceDefinitionProcessorShadow.setJSONObject(deviceConfig);

        intent.putExtra("enable_batch_scan", false);

        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);

        verifySingleScanDataIsCorrectlyPopulated();
        verifyRDTDetailsConfirmationPageIsPopulated();

        String deviceDetails = "<b>Manufacturer:</b><br>" + DeviceDefinitionProcessorShadow.MANUFACTURER
                + "<br><br><b>RDT name:</b><br>" + DeviceDefinitionProcessorShadow.DEVICE_NAME;
        assertEquals(deviceDetails, deviceDetailsWidget.getString(JsonFormConstants.TEXT));
        assertEquals(CovidConstants.FHIRResource.REF_IMG, deviceDetailsWidget.getString(CovidImageViewFactory.BASE64_ENCODED_IMG));
        assertEquals("[{\"text\":\"val-0\"}]", deviceDetailsWidget.optString(JsonFormConstants.OPTIONS_FIELD_NAME));

        Mockito.verify(covidRdtBarcodeFactory).navigateToUnusableProductPage();
        Mockito.verify(jsonFormActivity).writeValue("test-step", "", "val-0,2020-08-08,val-2,val-3,true", "", "", "", false);

        // verify correct action on backpress
        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
        Mockito.verify(formFragment).setMoveBackOneStep(true);
    }

    private void verifyBatchScanDataIsCorrectlyPopulated() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("batch_id", OpenSRPContextShadow.OPENMRS_ID);
        Mockito.verify(jsonFormActivity).writeValue("test-step", "qr_code_reader", jsonObject.toString(), "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue("test-page", "batch_id", OpenSRPContextShadow.OPENMRS_ID, "", "", "", false);
        Mockito.verify(covidRdtBarcodeFactory).moveToNextStep();
    }

    private void verifySingleScanDataIsCorrectlyPopulated() throws JSONException {
        Mockito.verify(jsonFormActivity).writeValue("test-step", "unique_id", "val-0", "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue("test-step", "exp_date", "2020-08-08", "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue("test-step", "lot_no", "val-2", "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue("test-step", "gtin", "val-3", "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue("test-step", "temp_sensor", "true", "", "", "", false);
    }


    private void verifyRDTDetailsConfirmationPageIsPopulated() throws JSONException {
        Mockito.verify(jsonFormActivity).writeValue(eq(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE),
                eq(CovidConstants.FormFields.RDT_CONFIG), eq(DeviceDefinitionProcessorShadow.getJsonObject().toString()),
                anyString(), anyString(), anyString(), anyBoolean());
        DeviceDefinitionProcessorShadow.setJSONObject(null);
        RDTJsonFormUtilsShadow.setJsonObject(null);
    }

    private void mockMethods() throws JSONException {
        Mockito.doNothing().when(covidRdtBarcodeFactory).moveToNextStep();
        Mockito.doNothing().when(covidRdtBarcodeFactory).navigateToUnusableProductPage();
        Mockito.doReturn("val-0,2020-08-08,val-2,val-3,true").when(covidRdtBarcodeFactory)
                .getBarcodeValsAsCSV(Mockito.any(Intent.class));
        Mockito.doReturn(new String[]{"val-0", "2020-08-08", "val-2", "val-3", "true"}).when(covidRdtBarcodeFactory)
                .splitCSV(anyString());

        formFragment = Mockito.mock(RDTJsonFormFragment.class);

        jsonFormActivity = Mockito.spy(jsonFormActivity);
        Mockito.doNothing().when(jsonFormActivity).writeValue(anyString(), anyString(), anyString(),
                anyString(), anyString(), anyString(), anyBoolean());

        WidgetArgs widgetArgs = new WidgetArgs().withJsonObject(new JSONObject()).withStepName("test-step")
                .withContext(jsonFormActivity).withFormFragment(formFragment);

        ReflectionHelpers.setField(covidRdtBarcodeFactory, "widgetArgs", widgetArgs);

        stepStateConfig.put(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE, "test-page");
        stepStateConfig.put(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE, CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE, CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        ReflectionHelpers.setField(covidRdtBarcodeFactory, "stepStateConfig", stepStateConfig);
    }
}
