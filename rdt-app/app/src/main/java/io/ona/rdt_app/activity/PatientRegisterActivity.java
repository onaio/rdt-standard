package io.ona.rdt_app.activity;

import android.content.Intent;
import android.view.View;

import org.json.JSONObject;
import org.smartregister.domain.FetchStatus;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.List;

import io.ona.rdt_app.R;
import io.ona.rdt_app.fragment.PatientRegisterFragment;
import io.ona.rdt_app.presenter.PatientRegisterActivityPresenter;

public class PatientRegisterActivity extends BaseRegisterActivity implements SyncStatusBroadcastReceiver.SyncStatusListener {

    @Override
    protected void initializePresenter() {
        presenter = new PatientRegisterActivityPresenter(this);
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        PatientRegisterFragment fragment = new PatientRegisterFragment();
        return fragment;
    }

    @Override
    protected android.support.v4.app.Fragment[] getOtherFragments() {
        return new android.support.v4.app.Fragment[0];
    }

    @Override
    public void startFormActivity(String s, String s1, String s2) {

    }

    @Override
    public void startFormActivity(JSONObject jsonObject) {

    }

    @Override
    protected void onActivityResultExtended(int i, int i1, Intent intent) {

    }

    @Override
    public List<String> getViewIdentifiers() {
        return null;
    }

    @Override
    public void startRegistration() {

    }

    @Override
    protected void registerBottomNavigation() {
        //not used for task register
        findViewById(R.id.bottom_navigation).setVisibility(View.GONE);
    }

    @Override
    public void onSyncStart() {
        // TODO: implement this
    }

    @Override
    public void onSyncInProgress(FetchStatus fetchStatus) {
        // TODO: implement this
    }

    @Override
    public void onSyncComplete(FetchStatus fetchStatus) {
        // TODO: implement this
    }
}
