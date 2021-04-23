package io.ona.rdt.interactor;

import org.smartregister.CoreLibrary;
import org.smartregister.login.interactor.BaseLoginInteractor;
import org.smartregister.util.NetworkUtils;
import org.smartregister.view.contract.BaseLoginContract;

import java.lang.ref.WeakReference;

import io.ona.rdt.util.Utils;

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

    @Override
    public void login(WeakReference<BaseLoginContract.View> view, String userName, char[] password) {
        getSharedPreferences().saveForceRemoteLogin(NetworkUtils.isNetworkAvailable(), userName);
        super.login(view, userName, password);
    }
}
