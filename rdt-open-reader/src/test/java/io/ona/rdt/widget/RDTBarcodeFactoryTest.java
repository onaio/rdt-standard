package io.ona.rdt.widget;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.HINT;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.MalariaRDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.OPENMRS_ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.OPENMRS_ENTITY_PARENT;

/**
 * Created by Vincent Karuri on 31/07/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, Bundle.class, RDTApplication.class, LayoutInflater.class})
public class RDTBarcodeFactoryTest {

    private RDTBarcodeFactory barcodeFactory;
    private WidgetArgs widgetArgs;
    private JsonFormActivity jsonFormActivity;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Mock
    private View rootLayout;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        barcodeFactory = new MalariaRDTBarcodeFactory();
    }

    @Test
    public void testGetDateStrShouldReturnEmptyStringForNullDate() throws Exception {
        String result = Whitebox.invokeMethod(barcodeFactory, "getDateStr", null);
        assertEquals(result, "");
    }

    @Test
    public void testIsRDTExpiredShouldReturnTrueForExpiredRDT() throws Exception {
        boolean result = Whitebox.invokeMethod(barcodeFactory,  "isRDTExpired", getPastDate());
        assertTrue(result);
    }

    @Test
    public void testIsRDTExpiredShouldReturnFalseForValidRDT() throws Exception {
        boolean result = Whitebox.invokeMethod(barcodeFactory, "isRDTExpired", getFutureDate());
        assertFalse(result);
    }

    @Test
    public void testMoveToNextStepShouldMoveToNextStepOrSubmitForValidRDT() throws Exception {
        mockStaticMethods();
        setWidgetArgs();
        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getFutureDate());
        verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testMoveToNextStepShouldMoveToExpPageForExpiredRDT() throws Exception {
        mockStaticMethods();
        setWidgetArgs();
        whenNew(Bundle.class).withNoArguments().thenReturn(mock(Bundle.class));

        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getPastDate());
        verify(widgetArgs.getFormFragment()).transactThis(any(RDTJsonFormFragment.class));
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateCorrectValues() throws Exception {
        mockStaticMethods();
        setWidgetArgs();

        JsonApi jsonApi = mock(JsonApi.class);
        String[] barcodeVals = new String[]{"step1", "key", "rdt_id"};
        Date expDate = new Date();
        Whitebox.invokeMethod(barcodeFactory, "populateRelevantFields", barcodeVals, jsonApi, expDate);

        verify(jsonApi).writeValue(eq("step7"), eq("lbl_rdt_id"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step8"), eq("lbl_rdt_id"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step9"), eq("lbl_rdt_id"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step18"), eq("lbl_rdt_id"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step19"), eq("lbl_rdt_id"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step1"), eq("rdt_id"), eq(barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step1"), eq("expiration_date_reader"), anyString(), eq(""), eq(""), eq(""), eq(false));
    }

    @Test
    public void testHideAndClickScanButtonShouldHideAndClickScanBtn() throws Exception {
        RelativeLayout rootLayout = mock(RelativeLayout.class);
        Button scanBtn = mock(Button.class);
        doReturn(scanBtn).when(rootLayout).findViewById(eq(com.vijay.jsonwizard.R.id.scan_button));
        Whitebox.invokeMethod(barcodeFactory, "clickThenHideScanButton", rootLayout);

        verify(scanBtn).setVisibility(eq(View.GONE));
        verify(scanBtn).performClick();
    }

    @Test
    public void testOnActivityResultShouldMoveBackOneStepOnCancel() throws JSONException {
        setWidgetArgs();
        barcodeFactory.onActivityResult(BARCODE_REQUEST_CODE, RESULT_CANCELED, mock(Intent.class));
        verify((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(eq(true));
    }

    @Test
    public void testOnActivityResultShouldSuccessfullyCaptureBarcodeInformation() throws Exception {
        mockStaticMethods();
        setWidgetArgs();
        Intent data = mock(Intent.class);
        Barcode barcode = new Barcode();
        barcode.displayValue = "openrdt.ona.io/,31012299,52605,M017G71,MalariaPfPv,5060511890000";
        doReturn(barcode).when(data).getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);

        RDTBarcodeFactory barcodeFactory = spy(this.barcodeFactory);
        barcodeFactory.onActivityResult(BARCODE_REQUEST_CODE, RESULT_OK, data);

        verify(jsonFormActivity, atLeastOnce()).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq(false));
        verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testGetViewsFromJson() throws Exception {
        mockStaticClasses();
        mockStaticMethods();
        setWidgetArgs();
        doReturn(mock(Button.class)).when(rootLayout).findViewById(eq(com.vijay.jsonwizard.R.id.scan_button));
        barcodeFactory.getViewsFromJson("step1", jsonFormActivity, widgetArgs.getFormFragment(),
                widgetArgs.getJsonObject(), mock(CommonListener.class), false);

        WidgetArgs actualWidgetArgs =  Whitebox.getInternalState(barcodeFactory, "widgetArgs");
        assertEquals(widgetArgs.getFormFragment(), actualWidgetArgs.getFormFragment());
        assertEquals(widgetArgs.getJsonObject(), actualWidgetArgs.getJsonObject());
        assertEquals(jsonFormActivity, actualWidgetArgs.getContext());
    }

    private Date getFutureDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        return calendar.getTime();
    }

    private Date getPastDate() throws ParseException {
        return convertDate("201217", OPEN_RDT_DATE_FORMAT);
    }

    private void setWidgetArgs() throws JSONException {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        widgetArgs.setFormFragment(formFragment);
        widgetArgs.setStepName("step1");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ENTITY_ID, "entity_id");
        jsonObject.put(OPENMRS_ENTITY_ID, "openmrs_entity_id");
        jsonObject.put(OPENMRS_ENTITY, "openmrs_entity");
        jsonObject.put(OPENMRS_ENTITY_PARENT, "openmrs_entity_parent");
        jsonObject.put(KEY, "key");
        jsonObject.put(HINT, "hint");
        jsonObject.put("scanButtonText", "scanButtonText");
        widgetArgs.setJsonObject(jsonObject);

        jsonFormActivity = mock(JsonFormActivity.class);
        Resources resources = mock(Resources.class);
        doReturn(resources).when(jsonFormActivity).getResources();
        doReturn(0).when(resources).getColor(anyInt());
        widgetArgs.setContext(jsonFormActivity);
        Whitebox.setInternalState(barcodeFactory, "widgetArgs", widgetArgs);
    }

    @Test
    public void testAddOnBarCodeResultListeners() throws Exception {
        JsonFormActivity jsonFormActivity = mock(JsonFormActivity.class);
        Whitebox.invokeMethod(barcodeFactory, "addOnBarCodeResultListeners", jsonFormActivity, mock(MaterialEditText.class));
        verify(jsonFormActivity).addOnActivityResultListener(eq(BARCODE_REQUEST_CODE), any(OnActivityResultListener.class));
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn("step1").when(jsonObject).optString(eq(PRODUCT_EXPIRED_PAGE), anyString());
        doReturn("step1:expiration_date_reader").when(jsonObject).optString(eq(EXPIRATION_DATE_READER_ADDRESS), anyString());
        doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
        doReturn(new JSONArray("[\n" +
                "    \"step7:lbl_rdt_id\",\n" +
                "    \"step8:lbl_rdt_id\",\n" +
                "    \"step9:lbl_rdt_id\",\n" +
                "    \"step18:lbl_rdt_id\",\n" +
                "    \"step19:lbl_rdt_id\"\n" +
                "  ]")).when(jsonObject).optJSONArray(eq(RDT_ID_LBL_ADDRESSES));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        Whitebox.setInternalState(barcodeFactory, "stepStateConfig", stepStateConfig);
    }

    private void mockStaticClasses() {
        mockStatic(LayoutInflater.class);
        LayoutInflater layoutInflater = mock(LayoutInflater.class);
        mockStatic(LayoutInflater.class);
        RelativeLayout rootLayout = mock(RelativeLayout.class);
        PowerMockito.when(LayoutInflater.from(any(Context.class))).thenReturn(layoutInflater);
        doReturn(rootLayout).when(layoutInflater).inflate(anyInt(), isNull());
        doReturn(mock(MaterialEditText.class)).when(rootLayout).findViewById(R.id.edit_text);
        doReturn(mock(Button.class)).when(rootLayout).findViewById(R.id.scan_button);
    }
}
