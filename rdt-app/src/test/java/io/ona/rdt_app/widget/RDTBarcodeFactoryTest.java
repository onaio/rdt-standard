package io.ona.rdt_app.widget;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent Karuri on 31/07/2019
 */
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
}
