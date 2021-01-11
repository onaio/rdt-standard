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
    public void testGetBarcodeValuesAsCSVShouldReturnValidBarcodeData() throws Exception {
        Barcode barcode = new Barcode();
        barcode.displayValue = "openrdt.ona.io.widget.GoogleCovidRDTBarcodeFactory\u001D31012299564524\u001D52605,M017G71\u001DMalariaPfPv\u001D5060511890000";
        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY, barcode);
        String barcodeCSV = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "getBarcodeValsAsCSV", intent);
        String[] data = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "splitCSV", barcodeCSV);

        String[] results = new String[]{"enrdt.ona.io.w", "get.GoogleCovidRDTBarcodeFactory", "012299", "4524", "52605"};
        for (int i = 0; i < results.length; i++) {
            Assert.assertEquals(results[i], data[i]);
        }
    }
}
