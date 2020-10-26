package io.ona.rdt.application;

import android.app.Activity;

import com.evernote.android.job.JobManager;
import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.location.helper.LocationHelper;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;
import org.smartregister.view.receiver.TimeChangedBroadcastReceiver;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.job.RDTJobCreator;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.repository.ParasiteProfileRepository;
import io.ona.rdt.repository.RDTRepository;
import io.ona.rdt.repository.RDTTestsRepository;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.RDTSyncConfiguration;
import io.ona.rdt.util.ReleaseTree;
import io.ona.rdt.util.StepStateConfig;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;
import static org.smartregister.util.Log.logInfo;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    private String password;
    private RDTApplicationPresenter presenter;
    private Activity currentActivity;
    private RDTTestsRepository rdtTestsRepository;
    private ParasiteProfileRepository parasiteProfileRepository;
    private com.vijay.jsonwizard.utils.AllSharedPreferences sharedPreferences;

    public static synchronized RDTApplication getInstance() {
        return (RDTApplication) mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initializeCrashlyticsAndLogging();

        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());

        // initialize modules
        CoreLibrary.init(context, new RDTSyncConfiguration(), System.currentTimeMillis());

        LocationHelper.init(Utils.ALLOWED_LEVELS, Utils.DEFAULT_LOCATION_LEVEL);

        SyncStatusBroadcastReceiver.init(this);

        JobManager.create(this).addJobCreator(new RDTJobCreator());

        // set image sync
        AllSharedPreferences sharedPreferences = getContext().allSharedPreferences();
        if (sharedPreferences.getPreference(IS_IMG_SYNC_ENABLED).isEmpty()) {
            sharedPreferences.savePreference(IS_IMG_SYNC_ENABLED, String.valueOf(true));
        }

        // set locale initially
        if (sharedPreferences.getPreference(CovidConstants.Locale.LOCALE).isEmpty()) {
            sharedPreferences.savePreference(CovidConstants.Locale.LOCALE, BuildConfig.LOCALE);
        }
    }

    private void initializeCrashlyticsAndLogging() {
        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false);
            Timber.plant(new Timber.DebugTree());
        } else {
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
            Timber.plant(new ReleaseTree());
        }
    }

    @Override
    public void logoutCurrentUser() {
        // do nothing
    }

    @Override
    public Repository getRepository() {
        try {
            if (repository == null) {
                repository = new RDTRepository(this, context);
            }
        } catch (UnsatisfiedLinkError e) {
            Timber.e(e);
        }
        return repository;
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

    public Activity getCurrentActivity(){
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity){
        this.currentActivity = currentActivity;
    }

    public void clearCurrActivityReference(Activity activity){
        if (activity.equals(getCurrentActivity())) {
            setCurrentActivity(null);
        }
    }

    public StepStateConfig getStepStateConfiguration() {
        return StepStateConfig.getInstance(getApplicationContext());
    }

    public RDTTestsRepository getRdtTestsRepository() {
        if (rdtTestsRepository == null) {
            rdtTestsRepository = new RDTTestsRepository();
        }
        return rdtTestsRepository;
    }

    public ParasiteProfileRepository getParasiteProfileRepository() {
        if (parasiteProfileRepository == null) {
            parasiteProfileRepository = new ParasiteProfileRepository();
        }
        return parasiteProfileRepository;
    }
}
