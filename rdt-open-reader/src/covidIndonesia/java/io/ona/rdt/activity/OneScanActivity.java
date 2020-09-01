package io.ona.rdt.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.barcode.Barcode;

import org.apache.commons.lang3.StringUtils;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.util.OneScanHelper;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;

public class OneScanActivity extends AppCompatActivity {

    private OneScanHelper oneScanHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_scan);
        setSupportActionBar(findViewById(R.id.toolbar));
        oneScanHelper = new OneScanHelper(this);
        doScan("camera");
        getOneScanVersion();
    }

    private void getOneScanVersion() {
        TextView versionView = findViewById(R.id.textView);
        OneScanHelper.VersionRequest request = new OneScanHelper.VersionRequest();
        oneScanHelper.send(request, (resultCode, bundle) -> {
            if (resultCode == Activity.RESULT_OK) {
                OneScanHelper.VersionResponse response = new OneScanHelper.VersionResponse(bundle);
                versionView.setText(String.format("OneScan Version\n%s", response.version));
            } else {
                versionView.setText("OneScan Version\nError: OneScan not installed");
            }
        });
    }

    private void doScan(String reader) {
        TextView resultView = findViewById(R.id.textViewResult);
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
                OneScanHelper.ScanResponse response = new OneScanHelper.ScanResponse(bundle);
                resultView.setText(String.format("\tStatus: %s\n\n\tBarcode Text: %s" +
                                "\n\n\tProduct ID: %s\n\n\tSerial No: %s\n\n\tAdditional ID: %s\n\n" +
                                "\tLot: %s\n\n\tExpiration Date: %s\n\n\tSensor triggered: %s",
                        response.status,
                        response.barcodeText,
                        response.productId,
                        response.serialNumber,
                        response.additionalIdentifier,
                        response.lot,
                        response.expirationDate,
                        response.sensorTriggered ? "yes" : "no"));
                        setResultAndFinishDelayed(response, 1500);
            }
        });
    }


    private void setResultAndFinishDelayed(OneScanHelper.ScanResponse response, long milliseconds) {
        final Handler handler = new Handler();
        handler.postDelayed(() -> setResultAndFinish(response), milliseconds);
    }

    private void setResultAndFinish(OneScanHelper.ScanResponse response) {
        if (response.sensorTriggered) {
            new AlertDialog.Builder(this)
                    .setMessage("This RDT has been flagged for high heat exposure and reliability has been compromised. Please use another RDT.")
                    .setCancelable(false)
                    .setPositiveButton("OK", (paramDialogInterface, paramInt) -> onBackPressed())
                    .show();
        } else {
            Intent resultIntent = new Intent();
            Barcode barcode = new Barcode();
            barcode.displayValue = StringUtils.join(new String[]{response.serialNumber, response.expirationDate,
                    response.lot, response.productId, String.valueOf(response.sensorTriggered),
                    response.status}, ',');
            resultIntent.putExtra(BARCODE_KEY, barcode);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        oneScanHelper.doActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            onBackPressed();
        }
    }
}
