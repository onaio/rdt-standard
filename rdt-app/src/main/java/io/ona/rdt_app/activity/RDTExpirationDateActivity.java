package io.ona.rdt_app.activity;

import android.content.Intent;

import edu.washington.cs.ubicomplab.rdt_reader.ExpirationDateActivity;
import edu.washington.cs.ubicomplab.rdt_reader.MainActivity;

/**
 * Created by Vincent Karuri on 21/06/2019
 */
public class RDTExpirationDateActivity extends ExpirationDateActivity {
    @Override
    public void onBackPressed() {
        finish();
    }
}
