package io.ona.rdt.widget;

import android.content.Intent;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import timber.log.Timber;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.RDT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Utils.convertDate;

/**
 * Created by Vincent Karuri on 17/06/2020
 */
public class MalariaGoogleBarcodeFactory extends RDTBarcodeFactory {

    public static final String OPEN_RDT_DATE_FORMAT = "ddMMyy";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Barcode barcode = data.getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
                Timber.d("Scanned QR Code " + barcode.displayValue);
                String[] barcodeValues = barcode.displayValue.split(",");

                Date expDate = null;
                if (barcodeValues.length >= 2) {
                    expDate = convertDate(barcodeValues[1].trim(), OPEN_RDT_DATE_FORMAT);
                    populateRelevantFields(barcodeValues, jsonApi, expDate);
                }
                moveToNextStep(expDate);
            } catch (JSONException e) {
                Timber.e(e);
            } catch (ParseException e) {
                Timber.e(e);
            }
        } else if (requestCode == BARCODE_REQUEST_CODE && resultCode == RESULT_CANCELED) {
            ((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(true);
        } else if (data == null) {
            Timber.i("No result for qr code");
        }
    }

    protected void populateRelevantFields(String[] barcodeValues, JsonApi jsonApi, Date expDate) throws JSONException {
        String idAndExpDate = barcodeValues[0] + "," + barcodeValues[1];
        JSONObject jsonObject = widgetArgs.getJsonObject();
        jsonObject.put(VALUE, idAndExpDate);

        // write barcode values to relevant widgets
        JSONArray rdtIdLblAddresses = stepStateConfig.getStepStateObj().optJSONArray(RDT_ID_LBL_ADDRESSES);
        String expirationDateAddress = stepStateConfig.getStepStateObj().optString(EXPIRATION_DATE_READER_ADDRESS, "");
        String[] stepAndId;

        // populate rdt id to all relevant txt labels
        for (int i = 0; i < rdtIdLblAddresses.length(); i++) {
            String addr = rdtIdLblAddresses.getString(i);
            stepAndId = addr.isEmpty() ? new String[0] : addr.split(":");
            if (stepAndId.length == 2) {
                jsonApi.writeValue(stepAndId[0].trim(), stepAndId[1].trim(), "RDT ID: " + barcodeValues[2].trim(), "", "", "", false);
            }
        }

        // write rdt id to hidden rdt id field
        jsonApi.writeValue(widgetArgs.getStepName(), stepStateConfig.getStepStateObj().optString(RDT_ID_KEY), barcodeValues[2].trim(), "", "", "", false);

        // populate exp. date to expiration date widget value
        stepAndId = expirationDateAddress.isEmpty() ? new String[0] : expirationDateAddress.split(":");
        if (stepAndId.length == 2) {
            jsonApi.writeValue(stepAndId[0].trim(), stepAndId[1].trim(), getDateStr(expDate), "", "", "", false);
        }
    }

    private String getDateStr(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
        return date == null ? "" : simpleDateFormat.format(date);
    }

    private boolean isRDTExpired(Date date) {
        return date == null ? true : new Date().after(date);
    }

    private void moveToNextStep(Date expDate) {
        JsonFormFragment formFragment = widgetArgs.getFormFragment();
        if (!isRDTExpired(expDate)) {
            moveToNextStep();
        } else {
            String expiredPageAddr = stepStateConfig.getStepStateObj().optString(RDT_EXPIRED_PAGE, "step1");
            JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(expiredPageAddr);
            formFragment.transactThis(nextFragment);
        }
    }
}
