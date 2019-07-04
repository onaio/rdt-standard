package io.ona.rdt_app.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.BarcodeFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.util.Constants;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public class RDTBarcodeFactory extends BarcodeFactory {

    private RelativeLayout rootLayout;
    private JSONObject jsonObject;
    private JsonFormFragment formFragment;

    private static final String TAG = RDTBarcodeFactory.class.getName();

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        this.jsonObject = jsonObject;
        this.formFragment = formFragment;

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener);

        this.rootLayout = (RelativeLayout) views.get(0);

        hideAndClickScanButton();

        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context,
                                       JsonFormFragment formFragment, final JSONObject jsonObject,
                                       CommonListener listener, boolean popup) {
        this.jsonObject = jsonObject;
        this.formFragment = formFragment;

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        rootLayout = (RelativeLayout) views.get(0);

        hideAndClickScanButton();

        return views;
    }

    private void hideAndClickScanButton() {
        Button scanButton = rootLayout.findViewById(com.vijay.jsonwizard.R.id.scan_button);
        scanButton.setVisibility(View.GONE);
        scanButton.performClick();
    }

    @Override
    protected void addOnBarCodeResultListeners(final Context context, final MaterialEditText editText) {
        editText.setVisibility(View.GONE);
        if (context instanceof JsonApi) {
            final JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE,
                    new OnActivityResultListener() {
                        @Override
                        public void onActivityResult(int requestCode, int resultCode, Intent data) {
                            if (requestCode == JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE && resultCode == RESULT_OK) {
                                if (data != null) {
                                    try {
                                        Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
                                        Log.d("Scanned QR Code", barcode.displayValue);
                                        String[] barcodeValues = barcode.displayValue.split(",");
                                        String idAndExpDate = barcodeValues[0] + "," + barcodeValues[1];
                                        jsonObject.put(VALUE, idAndExpDate);
                                        jsonApi.writeValue("step5", Constants.Form.LBL_RDT_ID, barcodeValues[0], "", "", "", false);
                                        formFragment.next();
                                    } catch (JSONException e) {
                                        Log.e(TAG, e.getStackTrace().toString());
                                    }
                                } else {
                                    Log.i("", "No result for qr code");
                                }
                            } else if (requestCode == JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
                                ((Activity) context).finish();
                            }
                        }
                    });
        }
    }
}
