package io.ona.rdt_app.widget;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Calendar;
import java.util.Date;

import io.ona.rdt_app.fragment.RDTJsonFormFragment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 31/07/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class})
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
    public void testConvertDateShouldReturnNullDateForNullDateStr() throws Exception {
        String dateStr = null;
        Date result = barcodeFactory.convertDate(dateStr);
        assertNull(result);
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateForValidDateFormat() throws Exception {
        String dateStr = "201217";
        Date result = barcodeFactory.convertDate(dateStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        assertEquals(calendar.get(Calendar.DATE), 20);
        assertEquals(calendar.get(Calendar.MONTH), 11); // month is 0-indexed
        assertEquals(calendar.get(Calendar.YEAR), 2017);
    }

    @Test
    public void testIsRDTExpiredShouldReturnTrueForExpiredRDT() throws Exception {
        Date date = barcodeFactory.convertDate("201217");
        boolean result = Whitebox.invokeMethod(barcodeFactory,  "isRDTExpired", date);
        assertTrue(result);
    }

    @Test
    public void testIsRDTExpiredShouldReturnFalseForValidRDT() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        boolean result = Whitebox.invokeMethod(barcodeFactory, "isRDTExpired", calendar.getTime());
        assertFalse(result);
    }

    @Test
    public void testMoveToNextStepShouldMoveToNextStepOrSubmitForValidRDT() throws Exception {
        JsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        Whitebox.setInternalState(barcodeFactory, "formFragment", formFragment);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.YEAR, 1);
        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", calendar.getTime());
        verify(formFragment).next();
    }

    @Test
    public void testMoveToNextStepShouldMoveToExpPageForExpiredRDT() throws Exception {
        JsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        Whitebox.setInternalState(barcodeFactory, "formFragment", formFragment);
        Whitebox.setInternalState(barcodeFactory, "jsonObject", mock(JSONObject.class));

        mockStatic(RDTJsonFormFragment.class);

        RDTJsonFormFragment rdtJsonFormFragment = mock(RDTJsonFormFragment.class);
        doReturn(rdtJsonFormFragment).when(RDTJsonFormFragment.class, "getFormFragment", isNull());

        Date date = barcodeFactory.convertDate("201217");
        Whitebox.invokeMethod(barcodeFactory, "moveToNextStep", date);
        verify(formFragment).transactThis(eq(rdtJsonFormFragment));
    }
}
