package io.ona.rdt_app.presenter;

import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import org.smartregister.login.model.BaseLoginModel;
import org.smartregister.login.presenter.BaseLoginPresenter;
import org.smartregister.view.contract.BaseLoginContract;

import java.lang.ref.WeakReference;

import io.ona.rdt_app.interactor.LoginInteractor;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginPresenter extends BaseLoginPresenter implements BaseLoginContract.Presenter {

    public LoginPresenter(BaseLoginContract.View loginView) {
        mLoginView = new WeakReference<>(loginView);
        mLoginInteractor = new LoginInteractor(this);
        mLoginModel = new BaseLoginModel();
    }

    @Override
    public void processViewCustomizations() {
        // do nothing
    }

    @Override
    public boolean isServerSettingsSet() {
        return false;
    }

    @Override
    public void canvasGlobalLayoutListenerProcessor(ScrollView canvasSV, ViewTreeObserver.OnGlobalLayoutListener layoutListener) {
        // do nothing
    }
}
