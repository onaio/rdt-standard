package io.ona.rdt.job;

import android.content.Context;

import androidx.annotation.NonNull;

import com.evernote.android.job.Job;

import org.smartregister.job.SyncAllLocationsServiceJob;
import org.smartregister.sync.intent.SyncAllLocationsIntentService;

import io.ona.rdt.application.RDTApplication;

public class RDTSyncAllLocationsServiceJob extends SyncAllLocationsServiceJob {

    public static final String TAG = "RDTSyncAllLocationsServiceJob";

    public RDTSyncAllLocationsServiceJob(Class<? extends SyncAllLocationsIntentService> serviceClass) {
        super(serviceClass);
    }

    @NonNull
    @Override
    protected Job.Result onRunJob(@NonNull Job.Params params) {
        return super.onRunJob(params);
    }

    @Override
    public Context getApplicationContext() {
        return RDTApplication.getInstance().getApplicationContext();
    }
}
