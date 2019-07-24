package io.ona.rdt_app.interactor;

import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt_app.util.Utils;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginInteractor extends BaseLoginInteractor implements BaseLoginContract.Interactor {

    public LoginInteractor(BaseLoginContract.Presenter loginPresenter) {
        super(loginPresenter);
    }

    @Override
    protected void scheduleJobsPeriodically() {
        Utils.scheduleJobsPeriodically();
    }

    @Override
    public void scheduleJobsImmediately() {
        Utils.scheduleJobsImmediately();
    }
}
