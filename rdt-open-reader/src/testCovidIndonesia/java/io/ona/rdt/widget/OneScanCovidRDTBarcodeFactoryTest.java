package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.HashMap;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;
import io.ona.rdt.shadow.DeviceDefinitionProcessorShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;

public class OneScanCovidRDTBarcodeFactoryTest extends WidgetFactoryRobolectricTest {

    @Mock
    private RDTJsonFormFragment formFragment;
    @Mock
    private CovidRDTJsonFormUtils formUtils;
    @Captor
    private ArgumentCaptor<WidgetArgs> widgetArgsArgumentCaptor;

    private OneScanCovidRDTBarcodeFactory oneScanCovidRDTBarcodeFactory;
    private JSONObject stepStateConfig;

    private final String TEST_STEP = "test-step";
    private final String TEST_PAGE = "test-page";
    private final String VAL_0 = "val-0";
    private final String VAL_2 = "val-2";
    private final String VAL_3 = "val-3";
    private final String DATE = "2020-08-08";
    private final String SENSOR_TRIGGERED = "true";

    private WidgetArgs widgetArgs;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        oneScanCovidRDTBarcodeFactory = Mockito.spy(new OneScanCovidRDTBarcodeFactory());
        stepStateConfig = new JSONObject();
        mockMethods();
    }

    @After
    public void tearDown() {
        RDTApplication.getInstance().getStepStateConfiguration().setStepStateObj(null);
        DeviceDefinitionProcessorShadow.setJSONObject(null);
        RDTJsonFormUtilsShadow.setJsonObject(null);
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

        RDTApplication.getInstance().getStepStateConfiguration().setStepStateObj(stepStateConfig);
        ReflectionHelpers.setField(oneScanCovidRDTBarcodeFactory, "formUtils", formUtils);
        intent.putExtra(Constants.Config.ENABLE_BATCH_SCAN, false);

        JSONObject deviceDetailsWidget = new JSONObject(new HashMap<String, JSONArray>() {
            {
                put(JsonFormConstants.OPTIONS_FIELD_NAME, new JSONArray("[{}]"));
            }
        });
        RDTJsonFormUtilsShadow.setJsonObject(deviceDetailsWidget);

        oneScanCovidRDTBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);

        verifySingleScanDataIsCorrectlyPopulated();
        Assert.assertEquals(String.format("[{\"text\":\"%s\"}]", VAL_0), deviceDetailsWidget.optString(JsonFormConstants.OPTIONS_FIELD_NAME));
        Mockito.verify(formUtils).populateRDTDetailsConfirmationPage(widgetArgsArgumentCaptor.capture(), ArgumentMatchers.eq(DeviceDefinitionProcessorShadow.DEVICE_ID));
        verifyWidgetArgsMatch(widgetArgsArgumentCaptor.getValue());
        Mockito.verify(oneScanCovidRDTBarcodeFactory).navigateToUnusableProductPage();

        // verify correct action on back-press
        oneScanCovidRDTBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
        Mockito.verify(formFragment).setMoveBackOneStep(true);
    }

    @Test
    public void testLaunchBarcodeScanner() throws Exception {
        MaterialEditText materialEditText = Mockito.mock(MaterialEditText.class);
        InputMethodManager inputManager = Mockito.mock(InputMethodManager.class);
        Activity activity = Mockito.mock(Activity.class);
        Mockito.when(activity.getSystemService(ArgumentMatchers.anyString())).thenReturn(inputManager);
        Whitebox.invokeMethod(oneScanCovidRDTBarcodeFactory, "launchBarcodeScanner", activity, materialEditText, "");
        Mockito.verify(activity).startActivityForResult(ArgumentMatchers.any(Intent.class), ArgumentMatchers.anyInt());
    }

    @Test
    public void testGetBarcodeValsAsCSV() throws Exception {
        String displayValue = Whitebox.invokeMethod(oneScanCovidRDTBarcodeFactory, "getBarcodeValsAsCSV", new Intent());
        Assert.assertEquals(String.format("%s,%s,%s,%s,%s", VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED), displayValue);
    }

    @Test
    public void testSplitCSV() throws Exception {
        String[] result = Whitebox.invokeMethod(oneScanCovidRDTBarcodeFactory, "splitCSV", "");
        Assert.assertArrayEquals(new String[]{VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED}, result);
    }

    private void verifyWidgetArgsMatch(WidgetArgs capturedWidgetArgs) {
        Assert.assertEquals(widgetArgs.getContext(), capturedWidgetArgs.getContext());
        Assert.assertEquals(widgetArgs.getStepName(), capturedWidgetArgs.getStepName());
        Assert.assertEquals(widgetArgs.getFormFragment(), capturedWidgetArgs.getFormFragment());
        Assert.assertEquals(widgetArgs.getJsonObject(), capturedWidgetArgs.getJsonObject());
        Assert.assertEquals(widgetArgs.getListener(), capturedWidgetArgs.getListener());
        Assert.assertEquals(widgetArgs.isPopup(), capturedWidgetArgs.isPopup());
    }

    private void verifyBatchScanDataIsCorrectlyPopulated() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(CovidRDTBarcodeFactory.BATCH_ID, OpenSRPContextShadow.OPENMRS_ID);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidConstants.FormFields.QR_CODE_READER, jsonObject.toString(), "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_PAGE, CovidRDTBarcodeFactory.BATCH_ID, OpenSRPContextShadow.OPENMRS_ID, "", "", "", false);
        Mockito.verify(oneScanCovidRDTBarcodeFactory).moveToNextStep();
    }

    private void verifySingleScanDataIsCorrectlyPopulated() throws JSONException {
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, JsonFormConstants.KEY, StringUtils.join(new String[]{
                VAL_0, DATE, VAL_2, VAL_3, SENSOR_TRIGGERED}, ","), "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidConstants.FormFields.UNIQUE_ID, VAL_0, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.EXP_DATE, DATE, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.LOT_NO, VAL_2, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.GTIN, VAL_3, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(TEST_STEP, CovidRDTBarcodeFactory.TEMP_SENSOR, SENSOR_TRIGGERED, "", "", "", false);
        Mockito.verify(jsonFormActivity).writeValue(CovidConstants.Step.COVID_CONDUCT_RDT_PAGE, Constants.FormFields.LBL_RDT_ID, "RDT ID: " + VAL_0, "", "", "", false);
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

        JSONObject jsonObject = new JSONObject(new HashMap<String, String>() {
            {
                put(JsonFormConstants.KEY, JsonFormConstants.KEY);
            }
        });
        widgetArgs = new WidgetArgs().withJsonObject(jsonObject).withStepName(TEST_STEP)
                .withContext(jsonFormActivity).withFormFragment(formFragment);

        ReflectionHelpers.setField(oneScanCovidRDTBarcodeFactory, "widgetArgs", widgetArgs);

        stepStateConfig.put(CovidConstants.Step.COVID_SAMPLE_DELIVERY_DETAILS_UNIQUE_BATCH_ID_PAGE, TEST_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE, CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE, CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        stepStateConfig.put(CovidConstants.Step.COVID_CONDUCT_RDT_PAGE, CovidConstants.Step.COVID_CONDUCT_RDT_PAGE);
        ReflectionHelpers.setField(oneScanCovidRDTBarcodeFactory, "stepStateConfig", stepStateConfig);
    }
}
