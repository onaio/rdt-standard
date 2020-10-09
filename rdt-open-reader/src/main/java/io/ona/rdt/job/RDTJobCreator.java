package io.ona.rdt.job;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import org.smartregister.job.ImageUploadServiceJob;
import org.smartregister.job.LocationStructureServiceJob;
import org.smartregister.job.PullUniqueIdsServiceJob;

import io.ona.rdt.services.RDTSyncAllLocationsIntentService;
import io.ona.rdt.sync.RDTSyncIntentService;

/**
 * Created by Vincent Karuri on 18/06/2019
 */
public class RDTJobCreator implements JobCreator {

    private final String TAG = RDTJobCreator.class.getName();

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case RDTSyncServiceJob.TAG:
                return new RDTSyncServiceJob(RDTSyncIntentService.class);
            case ImageUploadSyncServiceJob.TAG:
                return new ImageUploadServiceJob();
            case PullUniqueIdsServiceJob.TAG:
                return new PullUniqueIdsServiceJob();
            case RDTSyncSettingsServiceJob.TAG:
                return new RDTSyncSettingsServiceJob();
            case LocationStructureServiceJob.TAG:
                return new LocationStructureServiceJob();
            case RDTSyncAllLocationsServiceJob.TAG:
                return new RDTSyncAllLocationsServiceJob(RDTSyncAllLocationsIntentService.class);
            default:
                Log.w(TAG, tag + " is not declared in RDTJobCreator Job Creator");
                return null;
        }
    }
}
