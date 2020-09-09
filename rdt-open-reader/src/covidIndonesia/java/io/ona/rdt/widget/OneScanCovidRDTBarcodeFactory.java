package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;

import java.util.Date;

import io.ona.rdt.R;
import io.ona.rdt.activity.OneScanActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import timber.log.Timber;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;

/**
 * Created by Vincent Karuri on 09/07/2020
 */

public class OneScanCovidRDTBarcodeFactory extends CovidRDTBarcodeFactory {

    protected void launchBarcodeScanner(Activity activity, MaterialEditText editText, String barcodeType) {
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(editText.getWindowToken(), HIDE_NOT_ALWAYS);
        Intent intent = new Intent(activity, OneScanActivity.class);
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

    @Override
    protected void moveToNextStep(Intent data, Date expDate) {
        Barcode barcode = data.getParcelableExtra(BARCODE_KEY);
        if (Boolean.parseBoolean(barcode.displayValue.split(",")[4])) {
            JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
            String expiredPageAddr = stepStateConfig.getStepStateObj().optString(PRODUCT_EXPIRED_PAGE, "step1");
            try {
                jsonApi.writeValue(expiredPageAddr, "lbl_sample_expired", "",  "", "", "", false);
                jsonApi.writeValue(expiredPageAddr, "lbl_collect_new_sample", widgetArgs.getContext().getString(R.string.rdt_high_heat_exposure),  "", "", "", false);
            } catch (JSONException ex) {
                Timber.e(ex);
            }
            JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(expiredPageAddr);
            widgetArgs.getFormFragment().transactThis(nextFragment);
        } else {
            super.moveToNextStep(data, expDate);
        }
    }
}