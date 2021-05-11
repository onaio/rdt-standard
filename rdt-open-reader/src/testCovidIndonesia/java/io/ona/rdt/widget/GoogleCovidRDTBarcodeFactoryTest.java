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
        barcode.displayValue = "010697027751469910NPC20059990\u001D1722022821A2182-B\u001D800901190409U02005055";
        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY, barcode);
        String barcodeCSV = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "getBarcodeValsAsCSV", intent);
        String[] data = Whitebox.invokeMethod(googleCovidRDTBarcodeFactory, "splitCSV", barcodeCSV);

        String[] results = new String[]{"06970277514699", "NPC20059990", "220228", "A2182-B", "800901190409U02005055"};
        for (int i = 0; i < results.length; i++) {
            Assert.assertEquals(results[i], data[i]);
        }
    }
}
