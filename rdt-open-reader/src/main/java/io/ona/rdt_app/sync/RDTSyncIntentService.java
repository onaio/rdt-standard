package io.ona.rdt_app.sync;

import android.content.Intent;

import org.smartregister.sync.intent.SyncIntentService;

import io.ona.rdt_app.job.ImageUploadSyncServiceJob;

import static io.ona.rdt_app.util.Utils.isImageSyncEnabled;

/**
 * Created by Vincent Karuri on 28/08/2019
 */
public class RDTSyncIntentService extends SyncIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        if (isImageSyncEnabled()) {
            ImageUploadSyncServiceJob.scheduleJobImmediately(ImageUploadSyncServiceJob.TAG);
        }
    }
}
