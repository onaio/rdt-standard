package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.vision.barcode.Barcode;

import org.apache.commons.lang3.StringUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.OneScanHelper;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;
import static com.vijay.jsonwizard.utils.Utils.showToast;

public class OneScanActivity extends AppCompatActivity implements View.OnClickListener {

    private OneScanHelper oneScanHelper;
    private OneScanHelper.ScanResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_one_scan);
        oneScanHelper = new OneScanHelper(this);
        doScan(CovidConstants.ScannerType.SCANNER);
        addListeners();
    }

    private void addListeners() {
        findViewById(R.id.btn_exit_barcode_results_view).setOnClickListener(this);
        findViewById(R.id.barcode_results_next_button).setOnClickListener(this);
    }

    private void doScan(String reader) {
        OneScanHelper.ScanRequest request = new OneScanHelper.ScanRequest();
        request.reader = reader;
        request.title = "Scan barcode";
        request.clientId = BuildConfig.ONE_SCAN_CLIENT_ID;
        request.token = BuildConfig.ONE_SCAN_AUTH_TOKEN;
        request.appName = getResources().getString(R.string.app_name);
        request.appVersion = BuildConfig.VERSION_NAME;
        request.appUserName = RDTApplication.getInstance().getContext().allSettings().fetchRegisteredANM();

        oneScanHelper.send(request, (resultCode, bundle) -> {
            if (resultCode == Activity.RESULT_OK) {
                response = new OneScanHelper.ScanResponse(bundle);
//                resultView.setText(String.format("\tStatus: %s\n\n\tBarcode Text: %s" +
//                                "\n\n\tProduct ID: %s\n\n\tSerial No: %s\n\n\tAdditional ID: %s\n\n" +
//                                "\tLot: %s\n\n\tExpiration Date: %s\n\n\tSensor triggered: %s",
//                        response.status,
//                        response.barcodeText,
//                        response.productId,
//                        response.serialNumber,
//                        response.additionalIdentifier,
//                        response.lot,
//                        response.expirationDate,
//                        response.sensorTriggered ? "yes" : "no"));
            }
        });
    }

    private void setResultAndFinish(OneScanHelper.ScanResponse response) {
        Intent resultIntent = new Intent();
        Barcode barcode = new Barcode();
        barcode.displayValue = StringUtils.join(new String[]{response.serialNumber, response.expirationDate,
                response.lot, response.productId, String.valueOf(response.sensorTriggered),
                response.status}, ',');
        resultIntent.putExtra(BARCODE_KEY, barcode);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        oneScanHelper.doActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_barcode_results_view:
                onBackPressed();
                break;
            case R.id.barcode_results_next_button:
                setResultAndFinish(response);
                break;
        }
    }
}
