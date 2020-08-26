package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.smartregister.task.SaveTeamLocationsTask;
import org.smartregister.view.activity.BaseLoginActivity;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.presenter.LoginPresenter;
import io.ona.rdt.presenter.RDTApplicationPresenter;

import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static io.ona.rdt.util.Utils.updateLocale;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginActivity extends BaseLoginActivity implements BaseLoginContract.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        updateFTSDetails();
        super.onCreate(savedInstanceState);
        addAttributionText();
    }

    private void updateFTSDetails() {
        // NB: assumes the Repository is first accessed within the LoginActivity through the UserService
        RDTApplication rdtApplication = RDTApplication.getInstance();
        RDTApplicationPresenter rdtApplicationPresenter = rdtApplication.getPresenter();
        rdtApplicationPresenter.setRegisterTableName(getRegisterTableName());
        rdtApplication.getContext().updateCommonFtsObject(rdtApplicationPresenter.createCommonFtsObject());
    }

    protected String getRegisterTableName() {
        return RDT_PATIENTS;
    }

    private void addAttributionText() {
        TextView tvLoginAttributions = findViewById(R.id.tv_login_attributions);
        String attributionsTxt = getResources().getString(R.string.login_attributions);
        tvLoginAttributions.setText(Html.fromHtml(attributionsTxt));
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
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
        Intent intent = new Intent(this, getLoginActivityClass());
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

    protected Class getLoginActivityClass() {
        return PatientRegisterActivity.class;
    }
}
