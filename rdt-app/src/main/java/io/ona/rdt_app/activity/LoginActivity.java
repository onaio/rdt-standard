package io.ona.rdt_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import org.smartregister.task.SaveTeamLocationsTask;
import org.smartregister.view.activity.BaseLoginActivity;
import org.smartregister.view.contract.BaseLoginContract;

import io.ona.rdt_app.R;
import io.ona.rdt_app.presenter.LoginPresenter;

import static io.ona.rdt_app.util.Utils.updateLocale;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class LoginActivity extends BaseLoginActivity implements BaseLoginContract.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        super.onCreate(savedInstanceState);
        addAttributionText();
    }

    private void addAttributionText() {
        updateLocale(this);
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
