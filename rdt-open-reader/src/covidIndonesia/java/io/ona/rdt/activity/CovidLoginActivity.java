package io.ona.rdt.activity;

import android.content.Intent;

import org.smartregister.task.SaveTeamLocationsTask;

import timber.log.Timber;

/**
 * Created by Vincent Karuri on 19/06/2020
 */
public class CovidLoginActivity extends LoginActivity {

    @Override
    public void goToHome(boolean isRemote) {
        if (isRemote) {
            org.smartregister.util.Utils.startAsyncTask(new SaveTeamLocationsTask(), null);
        }
        Intent intent = new Intent(this, CovidPatientRegisterActivity.class);
        startActivity(intent);
        finish();
    }
}
