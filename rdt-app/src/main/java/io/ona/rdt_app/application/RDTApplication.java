package io.ona.rdt_app.application;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.activity.LoginActivity;
import io.ona.rdt_app.job.RDTJobCreator;
import io.ona.rdt_app.repository.RDTRepository;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.RDTSyncConfiguration;
import io.ona.rdt_app.util.Utils;

import static io.ona.rdt_app.util.Constants.APP_VERSION;
import static io.ona.rdt_app.util.Constants.IS_IMG_SYNC_ENABLED;
import static io.ona.rdt_app.util.Constants.PHONE_MANUFACTURER;
import static io.ona.rdt_app.util.Constants.PHONE_MODEL;
import static io.ona.rdt_app.util.Constants.PHONE_OS_VERSION;
import static io.ona.rdt_app.util.Constants.RDT_PATIENTS;
import static org.smartregister.util.Log.logError;
import static org.smartregister.util.Log.logInfo;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    private static CommonFtsObject commonFtsObject;
    private String password;
    private Map<String, String> phoneProperties;

    public static synchronized RDTApplication getInstance() {
        return (RDTApplication) mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        phoneProperties = new HashMap<>();
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        context.updateCommonFtsObject(createCommonFtsObject());

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

    public static CommonFtsObject createCommonFtsObject() {
        if (commonFtsObject == null) {
            commonFtsObject = new CommonFtsObject(getFtsTables());
            commonFtsObject.updateSearchFields(RDT_PATIENTS, getFtsSearchFields());
            commonFtsObject.updateSortFields(RDT_PATIENTS, getFtsSortFields());
        }
        return commonFtsObject;
    }

    private static String[] getFtsTables() {
        return new String[]{RDT_PATIENTS};
    }

    private static String[] getFtsSearchFields() {
        return new String[]{Constants.DBConstants.NAME};
    }

    private static String[] getFtsSortFields() {
       return new String[]{Constants.DBConstants.NAME};
    }

    public Map<String, String> getPhoneProperties() {
        if (phoneProperties.size() == 0) {
            phoneProperties.put(PHONE_MANUFACTURER, Build.MANUFACTURER);
            phoneProperties.put(PHONE_MODEL, Build.MODEL);
            phoneProperties.put(PHONE_OS_VERSION, Build.VERSION.RELEASE);
            phoneProperties.put(APP_VERSION, BuildConfig.VERSION_NAME);
        }
        return phoneProperties;
    }

    public void updateLocale(android.content.Context context) {
        Locale locale = new Locale(BuildConfig.LOCALE);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }
}
