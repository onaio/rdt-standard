package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import java.util.Date;

import io.ona.rdt.activity.OneScanActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import timber.log.Timber;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;

/**
 * Created by Vincent Karuri on 09/07/2020
 */

public class OneScanCovidRDTBarcodeFactory extends CovidRDTBarcodeFactory {

    private final String TEMP_SENSOR = "temp_sensor";

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
    protected void moveToNextStep(Date expDate) {

        if (isSensorTriggered()) {
            String triggeredPageAddr = stepStateConfig.getStepStateObj().optString(Constants.Step.SENSOR_TRIGGERED_PAGE, "step1");
            JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(triggeredPageAddr);
            widgetArgs.getFormFragment().transactThis(nextFragment);
        } else {
            super.moveToNextStep(expDate);
        }
    }

    private boolean isSensorTriggered() {
        boolean result = false;

        try {
            JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
            String stepName = widgetArgs.getStepName();
            JSONObject stepObject = jsonApi.getStep(stepName);
            JSONArray fields = stepObject.getJSONArray(JsonFormUtils.FIELDS);
            for (int i = 0; i < fields.length(); i++) {
                JSONObject fieldObject = fields.getJSONObject(i);
                if (TEMP_SENSOR.equals(fieldObject.getString(JsonFormUtils.KEY))) {
                    result = fieldObject.optBoolean(JsonFormUtils.VALUE, false);
                }
            }
        } catch (JSONException ex) {
            Timber.e(ex);
        }

        return result;
    }
}