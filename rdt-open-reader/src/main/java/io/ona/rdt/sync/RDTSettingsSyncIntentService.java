package io.ona.rdt.sync;

import android.content.Intent;

import org.smartregister.sync.intent.SettingsSyncIntentService;

import io.ona.rdt.job.RDTSyncServiceJob;

/**
 * Created by Vincent Karuri on 05/03/2020
 */
public class RDTSettingsSyncIntentService extends SettingsSyncIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        boolean isSuccessfulSync = processSettings(intent);
        if (isSuccessfulSync) {
            RDTSyncServiceJob.scheduleJobImmediately(RDTSyncServiceJob.TAG);
        }
    }
}

