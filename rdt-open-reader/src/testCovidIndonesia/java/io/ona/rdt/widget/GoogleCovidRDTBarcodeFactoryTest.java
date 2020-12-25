package io.ona.rdt.widget;

import android.content.Intent;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;

public class GoogleCovidRDTBarcodeFactoryTest extends WidgetFactoryRobolectricTest {

    private GoogleCovidRDTBarcodeFactory googleCovidRDTBarcodeFactory;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        googleCovidRDTBarcodeFactory = new GoogleCovidRDTBarcodeFactory();
    }

    @Test
    public void testGetBarcodeValuesAsCSV() throws Exception {
        Barcode barcode = new Barcode();
        barcode.displayValue = "openrdt.ona.io.widget.GoogleCovidRDTBarcodeFactory\u001D31012299564524\u001D52605,M017G71\u001DMalariaPfPv\u001D5060511890000";
        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY, barcode);
        String barcodeCSV = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "getBarcodeValsAsCSV", intent);
        String[] data = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "splitCSV", barcodeCSV);

        Assert.assertEquals("enrdt.ona.io.w", data[0]);
        Assert.assertEquals("get.GoogleCovidRDTBarcodeFactory", data[1]);
        Assert.assertEquals("012299", data[2]);
        Assert.assertEquals("4524", data[3]);
        Assert.assertEquals("52605", data[4]);
    }
}
