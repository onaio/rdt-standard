package io.ona.rdt.widget;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Vincent Karuri on 17/06/2020
 */
public class CovidGoogleBarcodeFactory extends CovidBarcodeFactory {

    @Override
    protected String getBarcodeValsAsCSV(Intent data) {
        Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
        Log.d("Scanned QR Code", barcode.displayValue);
        String barcodeVals = StringUtils.join(barcode.displayValue.split("\u001D"), ",");
       return removeLeadingAndTrailingCommas(barcodeVals);
    }

    @Override
    protected String[] splitCSV(String barcodeCSV) {
        return extractValuesFromCSV(barcodeCSV);
    }

    private String removeLeadingAndTrailingCommas(String str) {
        if (StringUtils.isBlank(str)) { return str; }
        String truncatedStr = str.trim().charAt(0) == ',' ? str.substring(1) : str.trim();
        int truncatedStrLen = truncatedStr.length();
        return truncatedStr.charAt(truncatedStrLen - 1) == ','
                ? truncatedStr.substring(0, truncatedStrLen - 1) : truncatedStr;
    }

    private String[] extractValuesFromCSV(String values) {
        String[] vals = new String[5];
        String[] dataSections = values.split(",");
        String gtin = dataSections[0].substring(2, 16);
        String lotNum = dataSections[0].substring(18);
        String expDate = dataSections[1].substring(2, 8);
        String uniqueId = dataSections[1].substring(10);
        String tempSensor = dataSections[2];
        vals[0] = gtin;
        vals[1] = lotNum;
        vals[2] = expDate;
        vals[3] = uniqueId;
        vals[4] = tempSensor;
        return vals;
    }
}
