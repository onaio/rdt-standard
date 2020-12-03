package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;

import com.google.android.gms.vision.barcode.Barcode;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;

import io.ona.rdt.activity.OneScanActivity;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

import static android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;

/**
 * Created by Vincent Karuri on 09/07/2020
 */

public class OneScanCovidRDTBarcodeFactory extends CovidRDTBarcodeFactory {

    private static final String BATCH_ID_TEXT = "batch_id_text";
    private CovidRDTJsonFormUtils formUtils = new CovidRDTJsonFormUtils();

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

    @Override
    protected void processResultData(Intent data) throws JSONException, ParseException, IOException, FHIRParserException {
        if (data.getBooleanExtra(Constants.Config.ENABLE_BATCH_SCAN, false)) {
            populateBatchScanData(data);
        } else {
            super.processResultData(data);
        }
    }

    private void populateBatchScanData(Intent data) throws JSONException {

        JSONObject jsonObject = new JSONObject(data.getStringExtra("data"));

        formUtils.getNextUniqueIds(null, (args, uniqueIds) -> {

            String uniqueId = Utils.getUniqueId(uniqueIds);

            try {
                jsonObject.put(BATCH_ID, uniqueId);
                JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
                String stepName = widgetArgs.getStepName();

                jsonApi.writeValue(stepName, CovidConstants.FormFields.QR_CODE_READER, jsonObject.toString(),  "", "", "", false);
                jsonApi.writeValue(stepStateConfig.getString(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE), BATCH_ID, uniqueId,  "", "", "", false);
                jsonApi.writeValue(stepStateConfig.getString(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE), BATCH_ID_TEXT, uniqueId,  "", "", "", false);

                moveToNextStep();
            } catch (JSONException e) {
                Timber.e(e);
            }
        }, 1);
    }

}