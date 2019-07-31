package io.ona.rdt_app.widget;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.Date;

import static org.junit.Assert.assertEquals;

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
}
