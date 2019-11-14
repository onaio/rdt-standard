package io.ona.rdt.widget;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.stub.JsonApiStub;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static io.ona.rdt.util.Constants.EXPIRED_PAGE_ADDRESS;
import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.RDTBarcodeFactory.EXPIRATION_DATE_ADDRESS;
import static io.ona.rdt.widget.RDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static io.ona.rdt.widget.RDTBarcodeFactory.RDT_ID_ADDRESS;
import static io.ona.rdt.widget.RDTBarcodeFactory.RDT_ID_LBL_ADDRESSES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.whenNew;

/**
 * Created by Vincent Karuri on 31/07/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, Bundle.class})
public class RDTBarcodeFactoryTest {

    private RDTBarcodeFactory barcodeFactory;
    private WidgetArgs widgetArgs;
    private JsonFormActivity jsonFormActivity;

    @Before
    public void setUp() {
        barcodeFactory = new RDTBarcodeFactory();
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
        setWidgetArgs();
        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getFutureDate());
        verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testMoveToNextStepShouldMoveToExpPageForExpiredRDT() throws Exception {
        setWidgetArgs();
        whenNew(Bundle.class).withNoArguments().thenReturn(mock(Bundle.class));

        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getPastDate());
        verify(widgetArgs.getFormFragment()).transactThis(any(RDTJsonFormFragment.class));
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateCorrectValues() throws Exception {
        setWidgetArgs();

        JsonApi jsonApi = spy(new JsonApiStub());
        String[] barcodeVals = new String[]{"step1", "key", "rdt_id"};
        Date expDate = new Date();
        Whitebox.invokeMethod(barcodeFactory, "populateRelevantFields", barcodeVals, jsonApi, expDate);

        verify(jsonApi).writeValue(eq("step1"), eq("lbl_address1"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step5"), eq("lbl_address5"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step6"), eq("lbl_address6"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step2"), eq("rdt_id_addr"), eq(barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step3"), eq("exp_date_addr"), anyString(), eq(""), eq(""), eq(""), eq(false));
    }

    @Test
    public void testHideAndClickScanButtonShouldHideAndClickScanBtn() throws Exception {
        RelativeLayout rootLayout = mock(RelativeLayout.class);
        Button scanBtn = mock(Button.class);
        doReturn(scanBtn).when(rootLayout).findViewById(eq(com.vijay.jsonwizard.R.id.scan_button));
        Whitebox.setInternalState(barcodeFactory, "rootLayout", rootLayout);
        Whitebox.invokeMethod(barcodeFactory, "hideAndClickScanButton");

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

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RDT_ID_LBL_ADDRESSES, "step1:lbl_address1,step5:lbl_address5,step6:lbl_address6,");
        jsonObject.put(RDT_ID_ADDRESS, "step2:rdt_id_addr");
        jsonObject.put(EXPIRATION_DATE_ADDRESS, "step3:exp_date_addr");
        jsonObject.put(EXPIRED_PAGE_ADDRESS, "step2");
        widgetArgs.setJsonObject(jsonObject);

        jsonFormActivity = mock(JsonFormActivity.class);
        widgetArgs.setContext(jsonFormActivity);

        Whitebox.setInternalState(barcodeFactory, "widgetArgs", widgetArgs);
    }
}
