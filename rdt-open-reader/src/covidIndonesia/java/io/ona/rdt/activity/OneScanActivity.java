package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.util.OneScanHelper;

public class OneScanActivity extends AppCompatActivity {

    private OneScanHelper oneScanHelper;
    private TextView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_scan);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        oneScanHelper = new OneScanHelper(this);
        resultView = findViewById(R.id.textViewResult);

        FloatingActionButton cameraButton = findViewById(R.id.fabCamera);
        cameraButton.setOnClickListener(view -> doScan("camera"));

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

        cameraButton.performClick();
    }

    void doScan(String reader) {
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
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        oneScanHelper.doActivityResult(requestCode, resultCode, data);
    }
}
