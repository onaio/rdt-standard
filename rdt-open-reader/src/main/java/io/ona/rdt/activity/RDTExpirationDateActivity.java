package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;

import edu.washington.cs.ubicomplab.rdt_reader.ExpirationDateActivity;
import io.ona.rdt.application.RDTApplication;

import static io.ona.rdt.util.Constants.EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.EXPIRATION_DATE_RESULT;
import static io.ona.rdt.util.Utils.updateLocale;

/**
 * Created by Vincent Karuri on 21/06/2019
 */
public class RDTExpirationDateActivity extends ExpirationDateActivity {

    final RDTApplication rdtApplication = RDTApplication.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @Override
    protected void onResult(String expDate, boolean isValid) {
        super.onResult(expDate, isValid);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXPIRATION_DATE_RESULT, isValid);
        resultIntent.putExtra(EXPIRATION_DATE, expDate);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    protected void onResume() {
        super.onResume();
        rdtApplication.setCurrentActivity(this);
    }

    protected void onPause() {
        rdtApplication.clearCurrActivityReference(this);
        super.onPause();
    }

    protected void onDestroy() {
        rdtApplication.clearCurrActivityReference(this);
        super.onDestroy();
    }
}
