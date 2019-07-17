package io.ona.rdt_app.interactor;

import org.slf4j.helpers.Util;
import org.smartregister.job.SyncServiceJob;
import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;
import io.ona.rdt_app.util.Utils;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginInteractor extends BaseLoginInteractor implements BaseLoginContract.Interactor {

    public LoginInteractor(BaseLoginContract.Presenter loginPresenter) {
        super(loginPresenter);
    }

    protected void scheduleJobsPeriodically() {
        Utils.scheduleJobsPeriodically();
    }

    public void scheduleJobsImmediately() {
        Utils.scheduleJobsImmediately();
    }
}
