package io.ona.rdt.job;

import android.content.Intent;
import androidx.annotation.NonNull;

import org.smartregister.AllConstants;
import org.smartregister.job.BaseJob;
import org.smartregister.service.ImageUploadSyncService;

/**
 * Created by Vincent Karuri on 28/06/2019
 */
public class ImageUploadSyncServiceJob extends BaseJob {

    public static final String TAG = "ImageUploadSyncService";

    @NonNull
    @Override
    protected Result onRunJob(@NonNull Params params) {
        Intent intent = new Intent(getApplicationContext(), ImageUploadSyncService.class);
        getApplicationContext().startService(intent);
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Result.RESCHEDULE : Result.SUCCESS;
    }
}
