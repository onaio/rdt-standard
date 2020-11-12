package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.vision.barcode.Barcode;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.OneScanHelper;
import io.ona.rdt.util.Utils;
import io.ona.rdt.widget.CovidRDTBarcodeFactory;
import io.ona.rdt.widget.RDTBarcodeFactory;
import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY;

public class OneScanActivity extends AppCompatActivity implements View.OnClickListener {

    private OneScanHelper oneScanHelper;
    private OneScanHelper.ScanResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Utils.updateLocale(this);
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
                performPostScanActions(bundle);
            }
        });
    }

    private void performPostScanActions(Bundle bundle) {
        response = new OneScanHelper.ScanResponse(bundle);
        try {
            displayStatusMessageAndSymbol(response.sensorTriggered, response.expirationDate);
            populateBarcodeDetails();
        } catch (ParseException e) {
            Timber.e(e);
        }
    }

    private void populateBarcodeDetails() {
        setBarcodeResult(R.id.barcode_product_id, response.productId);
        setBarcodeResult(R.id.barcode_serial_no, response.serialNumber);
        setBarcodeResult(R.id.barcode_additional_id, response.additionalIdentifier);
        setBarcodeResult(R.id.barcode_lot_no, response.lot);
        setBarcodeResult(R.id.barcode_expiration_date, response.expirationDate);
        setBarcodeResult(R.id.barcode_is_sensor_triggered, response.sensorTriggered ? getString(R.string.yes) : getString(R.string.no));
    }

    private void setBarcodeResult(int viewId, String result) {
        View barcodeResultRow = findViewById(viewId);
        if (StringUtils.isBlank(result)) {
            barcodeResultRow.setVisibility(View.GONE);
        } else {
            ((TextView) barcodeResultRow.findViewById(R.id.tv_barcode_result_value)).setText(result);
        }
    }

    private void displayStatusMessageAndSymbol(boolean isSensorTriggered, String expirationDate) throws ParseException {
        if (!isSensorTriggered && StringUtils.isBlank(expirationDate)
                || isProductUsable(isSensorTriggered, expirationDate)) {
            setValidProductStatus();
        } else {
            setUnusableProductStatus();
        }
    }

    private boolean isProductUsable(boolean isSensorTriggered, String expirationDate) throws ParseException {
        return !RDTBarcodeFactory.isRDTExpired(Utils.convertDate(expirationDate, CovidRDTBarcodeFactory.RDT_BARCODE_EXPIRATION_DATE_FORMAT))
                && !isSensorTriggered;
    }

    private void setUnusableProductStatus() {
        findViewById(R.id.product_valid_status).setVisibility(View.GONE);
        findViewById(R.id.valid_product_icon).setVisibility(View.GONE);
        findViewById(R.id.product_invalid_status).setVisibility(View.VISIBLE);
        findViewById(R.id.invalid_product_icon).setVisibility(View.VISIBLE);
    }

    private void setValidProductStatus() {
        findViewById(R.id.product_invalid_status).setVisibility(View.GONE);
        findViewById(R.id.invalid_product_icon).setVisibility(View.GONE);
        findViewById(R.id.product_valid_status).setVisibility(View.VISIBLE);
        findViewById(R.id.valid_product_icon).setVisibility(View.VISIBLE);
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
