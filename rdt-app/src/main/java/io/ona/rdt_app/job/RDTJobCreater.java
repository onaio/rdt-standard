package io.ona.rdt_app.job;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

import org.smartregister.job.SyncServiceJob;
import org.smartregister.sync.intent.SyncIntentService;

/**
 * Created by Vincent Karuri on 18/06/2019
 */
public class RDTJobCreater implements JobCreator {

    private final String TAG = RDTJobCreater.class.getName();

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case SyncServiceJob.TAG:
                return new SyncServiceJob(SyncIntentService.class);
            default:
                Log.w(TAG, tag + " is not declared in RevealJobCreator Job Creator");
                return null;
        }
    }
}
