package io.ona.rdt.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.RDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Created by Vincent Karuri on 31/07/2019
 */
public class UtilsTest {
    @Test
    public void testConvertDateShouldReturnNullDateForNullDateStr() throws Exception {
        String dateStr = null;
        Date result = convertDate(dateStr, OPEN_RDT_DATE_FORMAT);
        assertNull(result);
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateForValidDateFormat() throws Exception {
        Date date = convertDate("201217", "ddMMyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(calendar.get(Calendar.DATE), 20);
        assertEquals(calendar.get(Calendar.MONTH), 11); // month is 0-indexed
        assertEquals(calendar.get(Calendar.YEAR), 2017);
    }
}
