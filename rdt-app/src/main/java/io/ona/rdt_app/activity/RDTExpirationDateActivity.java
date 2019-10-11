package io.ona.rdt_app.activity;

import android.content.Intent;
import android.os.Bundle;

import edu.washington.cs.ubicomplab.rdt_reader.ExpirationDateActivity;

import static io.ona.rdt_app.util.Constants.EXPIRATION_DATE;
import static io.ona.rdt_app.util.Constants.EXPIRATION_DATE_RESULT;
import static io.ona.rdt_app.util.Utils.updateLocale;

/**
 * Created by Vincent Karuri on 21/06/2019
 */
public class RDTExpirationDateActivity extends ExpirationDateActivity {

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
}
