package io.ona.rdt_app.widget;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.ona.rdt_app.fragment.RDTJsonFormFragment;

import static io.ona.rdt_app.util.Utils.convertDate;
import static io.ona.rdt_app.widget.RDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
