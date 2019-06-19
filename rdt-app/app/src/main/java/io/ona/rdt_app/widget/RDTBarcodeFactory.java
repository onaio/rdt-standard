package io.ona.rdt_app.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.vision.barcode.Barcode;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.BarcodeFactory;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public class RDTBarcodeFactory extends BarcodeFactory {

    @Override
    protected void addOnBarCodeResultListeners(final Context context, final MaterialEditText editText) {
        if (context instanceof JsonApi) {
            JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE,
                    new OnActivityResultListener() {
                        @Override
                        public void onActivityResult(int requestCode,
                                                     int resultCode, Intent data) {
                            if (requestCode == JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE && resultCode == RESULT_OK) {
                                if (data != null) {
                                    Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
                                    Log.d("Scanned QR Code", barcode.displayValue);
                                    editText.setText(barcode.displayValue.split(",")[0]);
                                } else
                                    Log.i("", "NO RESULT FOR QR CODE");
                            }
                        }
                    });
        }
    }
}
