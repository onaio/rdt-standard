package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.TestUtils;
import io.ona.rdt.activity.OneScanActivity;
import io.ona.rdt.util.OneScanHelper;
import io.ona.rdt.widget.CovidRDTBarcodeFactory;

/**
 * Created by Vincent Karuri on 18/09/2020
 */
public class OneScanActivityTest extends ActivityRobolectricTest {

    private OneScanActivity oneScanActivity;

    @Before
    public void setUp() {
        oneScanActivity = Robolectric.buildActivity(OneScanActivity.class).create().resume().get();
    }

    @Test
    public void testBarcodeResultScreenPopulationShouldPopulateCorrectData() {
        final String barcodeText = "text";
        final boolean sensorTriggered = false;
        final String productId = "product_id";
        final String lot = "lot_no";
        final String serialNumber = "serial_no";
        final String additionalIdentifier = "identifier";
        final String expirationDate = TestUtils.getFormattedDateWithOffset(-1, CovidRDTBarcodeFactory.RDT_BARCODE_EXPIRATION_DATE_FORMAT);
        final String sensorNotTriggered = "No";
        final String sensorTriggeredStr = "sensorTriggered";
        final String expDateStr = "expirationDate";
        final String serialNumberStr = "serialNumber";
        final String performPostScanActionsMethod = "performPostScanActions";

        Bundle bundle = new Bundle();
        bundle.putString("status", "ok");
        bundle.putString("barcodeText", barcodeText);
        bundle.putBoolean(sensorTriggeredStr, sensorTriggered);
        bundle.putString("productId", productId);
        bundle.putString("lot", lot);
        bundle.putString(expDateStr, TestUtils.getFormattedDateWithOffset(1, CovidRDTBarcodeFactory.RDT_BARCODE_EXPIRATION_DATE_FORMAT));
        bundle.putString(serialNumberStr, serialNumber);
        bundle.putString("additionalIdentifier", additionalIdentifier);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));

        // for usable product, show valid product status
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.product_invalid_status).getVisibility());
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.invalid_product_icon).getVisibility());
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.product_valid_status).getVisibility());
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.valid_product_icon).getVisibility());

        // sensor triggered, show unusable product status
        bundle.putBoolean(sensorTriggeredStr, true);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.product_invalid_status).getVisibility());
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.invalid_product_icon).getVisibility());
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.product_valid_status).getVisibility());
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.valid_product_icon).getVisibility());

        // product expired, show unusable product status
        bundle.putBoolean(sensorTriggeredStr, false);
        bundle.putString(expDateStr, expirationDate);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.product_invalid_status).getVisibility());
        Assert.assertEquals(View.VISIBLE, oneScanActivity.findViewById(R.id.invalid_product_icon).getVisibility());
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.product_valid_status).getVisibility());
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(R.id.valid_product_icon).getVisibility());

        // verify details
        verifyBarcodeResult(R.id.barcode_product_id, productId);
        verifyBarcodeResult(R.id.barcode_serial_no, serialNumber);
        verifyBarcodeResult(R.id.barcode_additional_id, additionalIdentifier);
        verifyBarcodeResult(R.id.barcode_lot_no, lot);
        verifyBarcodeResult(R.id.barcode_expiration_date, expirationDate);
        verifyBarcodeResult(R.id.barcode_is_sensor_triggered, sensorNotTriggered);

        // verify details for missing information
        bundle = new Bundle();
        bundle.putString(serialNumberStr, serialNumber);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));
        verifyBarcodeResult(R.id.barcode_serial_no, serialNumber);
        verifyBarcodeResult(R.id.barcode_is_sensor_triggered, sensorNotTriggered);
        verifyEmptyResultIsHidden(R.id.barcode_product_id);
        verifyEmptyResultIsHidden(R.id.barcode_additional_id);
        verifyEmptyResultIsHidden(R.id.barcode_lot_no);
        verifyEmptyResultIsHidden(R.id.barcode_expiration_date);
    }

    private void verifyEmptyResultIsHidden(int viewId) {
        Assert.assertEquals(View.GONE, oneScanActivity.findViewById(viewId).getVisibility());
    }

    private void verifyBarcodeResult(int viewId, String expectedResult) {
        View barcodeResultRow = oneScanActivity.findViewById(viewId);
        String actualResult = ((TextView) barcodeResultRow.findViewById(R.id.tv_barcode_result_value)).getText().toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testBarcodeResultScreenPopulationShouldVerifyCorrectJSONArray() throws JSONException {
        final String barcodeText = "text";
        final boolean sensorTriggered = false;
        final String productId = "product_id";
        final String lot = "lot_no";
        final String serialNumber = "serial_no";
        final String additionalIdentifier = "identifier";
        final String expirationDate = TestUtils.getFormattedDateWithOffset(-1, CovidRDTBarcodeFactory.RDT_BARCODE_EXPIRATION_DATE_FORMAT);
        final String sensorNotTriggered = "No";
        final String sensorTriggeredStr = "sensorTriggered";
        final String expDateStr = "expirationDate";
        final String serialNumberStr = "serialNumber";
        final String performPostScanActionsMethod = "performPostScanActions";
        final String enableBatchScanField = "enableBatchScan";
        final String dataArrayField = "dataArray";

        Bundle bundle = new Bundle();
        bundle.putString("status", "ok");
        bundle.putString("barcodeText", barcodeText);
        bundle.putBoolean(sensorTriggeredStr, sensorTriggered);
        bundle.putString("productId", productId);
        bundle.putString("lot", lot);
        bundle.putString(expDateStr, expirationDate);
        bundle.putString(serialNumberStr, serialNumber);
        bundle.putString("additionalIdentifier", additionalIdentifier);
        bundle.putString(sensorTriggeredStr, sensorNotTriggered);

        // enable batch scan
        ReflectionHelpers.setField(oneScanActivity, enableBatchScanField, true);

        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));
        ReflectionHelpers.callInstanceMethod(oneScanActivity, performPostScanActionsMethod,
                ReflectionHelpers.ClassParameter.from(Bundle.class, bundle));

        JSONArray dataArray = ReflectionHelpers.getField(oneScanActivity, dataArrayField);

        Assert.assertEquals(2, dataArray.length());

        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject scanObject = dataArray.getJSONObject(i);
            Assert.assertEquals(productId, scanObject.getString("productId"));
            Assert.assertEquals(serialNumber, scanObject.getString("serialNumber"));
            Assert.assertEquals(additionalIdentifier, scanObject.getString("additionalIdentifier"));
            Assert.assertEquals(lot, scanObject.getString("lot"));
            Assert.assertEquals(expirationDate, scanObject.getString("expirationDate"));
        }

    }

    @Test
    public void testActivityResultShouldVerifyCorrectResultData() throws JSONException {
        final String setResultAndFinishMethod = "setResultAndFinish";
        final String enableBatchScanField = "enableBatchScan";
        final String dataArrayField = "dataArray";

        final String valStatus = "ok";
        final String valBarcodeTest = "test";
        final boolean valSensorTriggered = false;
        final String valProductId = "test-product";
        final String valLot = "test-lot";
        final String valExpirationDate = TestUtils.getFormattedDateWithOffset(1, CovidRDTBarcodeFactory.RDT_BARCODE_EXPIRATION_DATE_FORMAT);
        final String valSerialNumber = "test-serial";
        final String valAdditionalIdentifier = "test-additional-identifier";
        final String keyProductId = "productId";
        final String keySerialNumber = "serialNumber";
        final String keyAdditionalIdentifier = "additionalIdentifier";
        final String keyLot = "lot";
        final String keyExperienceDate = "experienceDate";

        final Bundle bundle = new Bundle();
        bundle.putString("status", valStatus);
        bundle.putString("barcodeText", valBarcodeTest);
        bundle.putBoolean("sensorTriggered", valSensorTriggered);
        bundle.putString(keyProductId, valProductId);
        bundle.putString(keyLot, valLot);
        bundle.putString(keyExperienceDate, valExpirationDate);
        bundle.putString(keySerialNumber, valSerialNumber);
        bundle.putString(keyAdditionalIdentifier, valAdditionalIdentifier);
        final OneScanHelper.ScanResponse scanResponse = new OneScanHelper.ScanResponse(bundle);
        final JSONObject scanObject = new JSONObject();
        scanObject.put(keyProductId, valProductId)
                .put(keyLot, valLot)
                .put(keyProductId, valProductId)
                .put(keyExperienceDate, valExpirationDate)
                .put(keySerialNumber, valSerialNumber)
                .put(keyAdditionalIdentifier, valAdditionalIdentifier);
        final JSONArray dataArray = new JSONArray();
        dataArray.put(scanObject);

        // when batch scan is enabled
        ShadowActivity shadowOneScanActivity = Shadows.shadowOf(oneScanActivity);
        ReflectionHelpers.setField(oneScanActivity, enableBatchScanField, true);
        ReflectionHelpers.setField(oneScanActivity, dataArrayField, dataArray);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, setResultAndFinishMethod,
                ReflectionHelpers.ClassParameter.from(OneScanHelper.ScanResponse.class, scanResponse));

        String resultData = shadowOneScanActivity.getResultIntent().getStringExtra("data");
        Assert.assertEquals(Activity.RESULT_OK, shadowOneScanActivity.getResultCode());
        Assert.assertNotNull(resultData);
        JSONObject resultObj = new JSONObject(resultData);
        Assert.assertTrue(resultObj.has("scans"));
        JSONArray resultScansArray = resultObj.getJSONArray("scans");
        Assert.assertEquals(dataArray.toString(), resultScansArray.toString());

        // when batch scan is disabled
        ReflectionHelpers.setField(oneScanActivity, enableBatchScanField, false);
        ReflectionHelpers.callInstanceMethod(oneScanActivity, setResultAndFinishMethod,
                ReflectionHelpers.ClassParameter.from(OneScanHelper.ScanResponse.class, scanResponse));

        Barcode resultBarcode = shadowOneScanActivity.getResultIntent().getParcelableExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY);
        final String displayValues = StringUtils.join(new String[]{scanResponse.serialNumber, scanResponse.expirationDate,
                scanResponse.lot, scanResponse.productId, String.valueOf(scanResponse.sensorTriggered),
                scanResponse.status}, ',');

        Assert.assertEquals(Activity.RESULT_OK, shadowOneScanActivity.getResultCode());
        Assert.assertNotNull(resultBarcode);
        Assert.assertEquals(displayValues, resultBarcode.displayValue);

    }

    @Override
    public Activity getActivity() {
        return oneScanActivity;
    }
}

