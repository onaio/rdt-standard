package io.ona.rdt.application;

import android.content.Intent;
import android.os.Build;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.evernote.android.job.JobManager;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.commonregistry.CommonFtsObject;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.receiver.TimeChangedBroadcastReceiver;

import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.activity.LoginActivity;
import io.ona.rdt.job.RDTJobCreator;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.repository.RDTRepository;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.RDTSyncConfiguration;
import io.ona.rdt.util.Utils;

import static io.ona.rdt.util.Constants.APP_VERSION;
import static io.ona.rdt.util.Constants.IS_IMG_SYNC_ENABLED;
import static io.ona.rdt.util.Constants.PHONE_MANUFACTURER;
import static io.ona.rdt.util.Constants.PHONE_MODEL;
import static io.ona.rdt.util.Constants.PHONE_OS_VERSION;
import static io.ona.rdt.util.Constants.RDT_PATIENTS;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    private String password;
    private RDTApplicationPresenter presenter;

    public static synchronized RDTApplication getInstance() {
        return (RDTApplication) mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        context.updateCommonFtsObject(presenter.createCommonFtsObject());

        // Initialize Modules
        CoreLibrary.init(context, new RDTSyncConfiguration(), System.currentTimeMillis());

        LocationHelper.init(Utils.ALLOWED_LEVELS, Utils.DEFAULT_LOCATION_LEVEL);

        SyncStatusBroadcastReceiver.init(this);

        Fabric.with(this, new Crashlytics.Builder().core(new CrashlyticsCore.Builder().disabled(BuildConfig.DEBUG).build()).build());

        JobManager.create(this).addJobCreator(new RDTJobCreator());

        AllSharedPreferences sharedPreferences = getContext().allSharedPreferences();
        if (sharedPreferences.getPreference(IS_IMG_SYNC_ENABLED).isEmpty()) {
            sharedPreferences.savePreference(IS_IMG_SYNC_ENABLED, String.valueOf(true));
        }
    }

    @Override
    public void logoutCurrentUser() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(intent);
        context.userService().logoutSession();
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new RDTRepository(getInstance().getApplicationContext(), context);
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);
        }
        return repository;
    }

    @Override
    public String getPassword() {
        if (password == null) {
            String username = getContext().allSharedPreferences().fetchRegisteredANM();
            password = getContext().userService().getGroupId(username);
        }
        return password;
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

    public RDTApplicationPresenter getPresenter() {
        if (presenter == null) {
            presenter = new RDTApplicationPresenter();
        }
        return presenter;
    }
}
