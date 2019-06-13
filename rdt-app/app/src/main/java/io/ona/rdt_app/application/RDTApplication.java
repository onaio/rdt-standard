package io.ona.rdt_app.application;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.Repository;
import org.smartregister.view.activity.DrishtiApplication;

import io.ona.rdt_app.repository.RDTRepository;

import static org.smartregister.util.Log.logError;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class RDTApplication extends DrishtiApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        context = Context.getInstance();
        context.updateApplicationContext(getApplicationContext());
        // Initialize Modules
        CoreLibrary.init(context, null);
        SyncStatusBroadcastReceiver.init(this);
        context.initRepository();
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
            }
        } catch (UnsatisfiedLinkError e) {
            logError("Error on getRepository: " + e);

        }
        return repository;
    }

    @Override
    public String getPassword() {
        return "sample_pass1";
    }
}
