package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;

import io.ona.rdt.activity.OneScanActivity;
import io.ona.rdt.util.Constants;
import timber.log.Timber;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;

/**
 * Created by Vincent Karuri on 09/07/2020
 */

public class OneScanCovidRDTBarcodeFactory extends CovidRDTBarcodeFactory {

    protected void launchBarcodeScanner(Activity activity, MaterialEditText editText, String barcodeType) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), HIDE_NOT_ALWAYS);
        Intent intent = new Intent(activity, OneScanActivity.class);
        intent.putExtra(Constants.Config.ENABLE_BATCH_SCAN, widgetArgs.getJsonObject().optBoolean(Constants.Config.ENABLE_BATCH_SCAN));
        activity.startActivityForResult(intent, BARCODE_REQUEST_CODE);
    }

    @Override
    protected String getBarcodeValsAsCSV(Intent data) {
        Barcode barcode = data.getParcelableExtra(BARCODE_KEY);
        Timber.d("Scanned QR Code " + barcode.displayValue);
        return barcode.displayValue;
    }

    @Override
    protected String[] splitCSV(String barcodeCSV) {
        return barcodeCSV.split(",");
    }
}