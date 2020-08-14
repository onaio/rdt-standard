package io.ona.rdt.job;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import io.ona.rdt.application.RDTApplication;

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
        Context applicationContext = RDTApplication.getInstance().getApplicationContext();
        Intent intent = new Intent(applicationContext, ImageUploadSyncService.class);
        applicationContext.startService(intent);
        return params != null && params.getExtras().getBoolean(AllConstants.INTENT_KEY.TO_RESCHEDULE, false) ? Result.RESCHEDULE : Result.SUCCESS;
    }
}
