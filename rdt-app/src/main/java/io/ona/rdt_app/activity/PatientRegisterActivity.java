package io.ona.rdt_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.FetchStatus;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt_app.R;
import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.fragment.PatientRegisterFragment;
import io.ona.rdt_app.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;

import static io.ona.rdt_app.util.Constants.REQUEST_CODE_GET_JSON;

public class PatientRegisterActivity extends BaseRegisterActivity implements SyncStatusBroadcastReceiver.SyncStatusListener, OnFormSavedCallback {

    @Override
    protected void initializePresenter() {
        presenter = new PatientRegisterActivityPresenter(this);
    }

    @Override
    protected BaseRegisterFragment getRegisterFragment() {
        return new PatientRegisterFragment();
    }

    @Override
    protected android.support.v4.app.Fragment[] getOtherFragments() {
        return new android.support.v4.app.Fragment[0];
    }

    @Override
    public void startFormActivity(String s, String s1, String s2) {
        // TODO: implement this
    }

    @Override
    public void startFormActivity(JSONObject jsonObject) {
        // TODO: implement this
    }

    @Override
    protected void onActivityResultExtended(int i, int i1, Intent intent) {
        // TODO: implement this
    }

    @Override
    public List<String> getViewIdentifiers() {
        return new ArrayList<>();         // TODO: implement this
    }

    @Override
    public void startRegistration() {
        // do this in register fragment
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK && data != null) {
            try {
                String jsonForm = data.getStringExtra("json");
                Log.d(TAG, jsonForm);
                PatientRegisterFragmentPresenter presenter = ((PatientRegisterFragment) getRegisterFragment()).getPresenter();
                presenter.saveForm(jsonForm, this);
            } catch (JSONException e) {
                Log.e(TAG, e.getStackTrace().toString());
            }
        }
    }

    @Override
    public void onFormSaved() {
        if (mBaseFragment != null && mBaseFragment.getActivity() != null) {
            mBaseFragment.refreshListView();
        }
    }
}
