package io.ona.rdt_app.activity;

import android.content.Intent;

import org.smartregister.task.SaveTeamLocationsTask;
import org.smartregister.view.activity.BaseLoginActivity;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt_app.presenter.LoginPresenter;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginActivity extends BaseLoginActivity implements BaseLoginContract.View {

    @Override
    protected int getContentView() {
        return org.smartregister.R.layout.activity_login; // todo: change this to rdt login layout
    }

    @Override
    protected void initializePresenter() {
        mLoginPresenter = new LoginPresenter(this);
    }

    @Override
    public void goToHome(boolean isRemote) {
        if (isRemote) {
            org.smartregister.util.Utils.startAsyncTask(new SaveTeamLocationsTask(), null);
        }
        Intent intent = new Intent(this, PatientRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!mLoginPresenter.isUserLoggedOut()) {
            goToHome(false);
        }
    }
}
