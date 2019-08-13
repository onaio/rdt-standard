package io.ona.rdt_app.widget;

import android.os.Bundle;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

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

import io.ona.rdt_app.fragment.RDTJsonFormFragment;

import static io.ona.rdt_app.util.Constants.EXPIRED_PAGE_ADDRESS;
import static io.ona.rdt_app.util.Utils.convertDate;
import static io.ona.rdt_app.widget.RDTBarcodeFactory.EXPIRATION_DATE_ADDRESS;
import static io.ona.rdt_app.widget.RDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static io.ona.rdt_app.widget.RDTBarcodeFactory.RDT_ID_ADDRESS;
import static io.ona.rdt_app.widget.RDTBarcodeFactory.RDT_ID_LBL_ADDRESSES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
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
    public void testGetDateStrShouldReturnDateStringForNonNullDate() throws Exception {
        Date now = new Date();
        String result = Whitebox.invokeMethod(barcodeFactory, "getDateStr", now);
        assertEquals(result, now.toString());
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
        JsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        Whitebox.setInternalState(barcodeFactory, "formFragment", formFragment);
        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getFutureDate());
        verify(formFragment).next();
    }

    @Test
    public void testMoveToNextStepShouldMoveToExpPageForExpiredRDT() throws Exception {
        JsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        Whitebox.setInternalState(barcodeFactory, "formFragment", formFragment);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(EXPIRED_PAGE_ADDRESS, "step2");
        Whitebox.setInternalState(barcodeFactory, "jsonObject", jsonObject);

        whenNew(Bundle.class).withNoArguments().thenReturn(mock(Bundle.class));

        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", getPastDate());
        verify(formFragment).transactThis(any(RDTJsonFormFragment.class));
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateCorrectValues() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(RDT_ID_LBL_ADDRESSES, "step1:lbl_address1");
        jsonObject.put(RDT_ID_ADDRESS, "step2:rdt_id_addr");
        jsonObject.put(EXPIRATION_DATE_ADDRESS, "step3:exp_date_addr");
        Whitebox.setInternalState(barcodeFactory, "jsonObject", jsonObject);

        JsonApi jsonApi = spy(new TestJsonApi());
        String[] barcodeVals = new String[]{"step1", "key", "rdt_id"};
        Date expDate = new Date();
        Whitebox.invokeMethod(barcodeFactory, "populateRelevantFields", barcodeVals, jsonApi, expDate);

        verify(jsonApi).writeValue(eq("step1"), eq("lbl_address1"), eq("RDT ID: " + barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step2"), eq("rdt_id_addr"), eq(barcodeVals[2]), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step3"), eq("exp_date_addr"), anyString(), eq(""), eq(""), eq(""), eq(false));
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
}
