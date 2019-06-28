package io.ona.rdt_app.application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.evernote.android.job.JobManager;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.Repository;
import org.smartregister.util.DatabaseMigrationUtils;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.receiver.TimeChangedBroadcastReceiver;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;
import io.ona.rdt_app.job.RDTJobCreator;
import io.ona.rdt_app.repository.RDTRepository;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.RDTSyncConfiguration;

import static io.ona.rdt_app.util.Constants.PATIENTS;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    private static CommonFtsObject commonFtsObject;

    public static synchronized RDTApplication getInstance() {
        return (RDTApplication) mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        context.updateCommonFtsObject(createCommonFtsObject());
        // Initialize Modules
        CoreLibrary.init(context, new RDTSyncConfiguration());
        SyncStatusBroadcastReceiver.init(this);

        // Fabric.with(this, new Crashlytics());
        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        getRepository();

        JobManager.create(this).addJobCreator(new RDTJobCreator());
        scheduleJobsPeriodically();
    }

    @Override
    public void logoutCurrentUser() {
        // do nothing
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new RDTRepository(getInstance().getApplicationContext(), context);
                SQLiteDatabase db = repository.getWritableDatabase();
                DatabaseMigrationUtils.createAddedECTables(db, new HashSet<>(Arrays.asList(PATIENTS)), null);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);
        }
        return repository;
    }

    @Override
    public String getPassword() {
        return "password";
    }

    public Context getContext() {
        return context;
    }

    @Override
    public void onTerminate() {
        logInfo("Application is terminating. Stopping Sync scheduler and resetting isSyncInProgress setting.");
        TimeChangedBroadcastReceiver.destroy(this);
        SyncStatusBroadcastReceiver.destroy(this);
        super.onTerminate();
    }

    public static CommonFtsObject createCommonFtsObject() {
        if (commonFtsObject == null) {
            commonFtsObject = new CommonFtsObject(getFtsTables());
            commonFtsObject.updateSearchFields(PATIENTS, getFtsSearchFields());
            commonFtsObject.updateSortFields(PATIENTS, getFtsSortFields());
        }
        return commonFtsObject;
    }

    private static String[] getFtsTables() {
        return new String[]{PATIENTS};
    }

    private static String[] getFtsSearchFields() {
        return new String[]{Constants.DBConstants.BASE_ENTITY_ID, Constants.DBConstants.ID, Constants.DBConstants.NAME};
    }

    private static String[] getFtsSortFields() {
       return new String[]{Constants.DBConstants.BASE_ENTITY_ID, Constants.DBConstants.ID, Constants.DBConstants.NAME};
    }


    protected void scheduleJobsPeriodically() {
        ImageUploadSyncServiceJob
                .scheduleJob(ImageUploadSyncServiceJob.TAG,
                TimeUnit.MINUTES.toMillis(BuildConfig.SYNC_INTERVAL_MINUTES),
                getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));
    }

    protected long getFlexValue(int value) {
        final int MINIMUM_JOB_FLEX_VALUE = 1;
        int minutes = MINIMUM_JOB_FLEX_VALUE;
        if (value > MINIMUM_JOB_FLEX_VALUE) {
            minutes = (int) Math.ceil(value / 3);
        }

        return TimeUnit.MINUTES.toMillis(minutes);
    }
}
