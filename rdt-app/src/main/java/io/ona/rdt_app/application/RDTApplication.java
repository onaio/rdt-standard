package io.ona.rdt_app.application;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.evernote.android.job.JobManager;

import net.sqlcipher.database.SQLiteDatabase;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.job.SyncServiceJob;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.util.DatabaseMigrationUtils;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.receiver.TimeChangedBroadcastReceiver;

import java.util.Arrays;
import java.util.HashSet;

import io.fabric.sdk.android.Fabric;
import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;
import io.ona.rdt_app.job.RDTJobCreator;
import io.ona.rdt_app.repository.RDTRepository;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.RDTSyncConfiguration;

import static io.ona.rdt_app.util.Constants.PATIENTS;
import static org.smartregister.AllConstants.DRISHTI_BASE_URL;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    private static CommonFtsObject commonFtsObject;
    private AllSharedPreferences allSharedPreferences;

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

        getContext().userService(); // todo: can be removed when login screen is added

        allSharedPreferences = getContext().allSharedPreferences();
        initializeSharedPreferences(); // todo: can be removed when login screen is added
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
        return new String[]{Constants.DBConstants.NAME};
    }

    private static String[] getFtsSortFields() {
       return new String[]{Constants.DBConstants.NAME};
    }

    private void initializeSharedPreferences() {
        getContext().allSettings().registerANM(BuildConfig.ANM_ID, BuildConfig.ANM_PASSWORD);
        allSharedPreferences.updateUrl(BuildConfig.BASE_URL);
        allSharedPreferences.savePreference(DRISHTI_BASE_URL, BuildConfig.BASE_URL);
    }
}
