package io.ona.rdt.sync;

import android.content.Intent;

import org.smartregister.job.LocationStructureServiceJob;
import org.smartregister.sync.intent.SyncIntentService;

import io.ona.rdt.job.ImageUploadSyncServiceJob;

import static io.ona.rdt.util.Utils.isImageSyncEnabled;

/**
 * Created by Vincent Karuri on 28/08/2019
 */
public class RDTSyncIntentService extends SyncIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationStructureServiceJob.scheduleJobImmediately(LocationStructureServiceJob.TAG);
        super.onHandleIntent(intent);
        if (isImageSyncEnabled()) {
            ImageUploadSyncServiceJob.scheduleJobImmediately(ImageUploadSyncServiceJob.TAG);
        }
    }
}
