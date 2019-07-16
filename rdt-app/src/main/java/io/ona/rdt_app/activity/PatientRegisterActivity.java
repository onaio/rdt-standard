package io.ona.rdt_app.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.util.Constants.REQUEST_CODE_GET_JSON;
import static io.ona.rdt_app.util.Constants.REQUEST_RDT_PERMISSIONS;

public class PatientRegisterActivity extends BaseRegisterActivity implements SyncStatusBroadcastReceiver.SyncStatusListener, OnFormSavedCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    public void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_RDT_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_RDT_PERMISSIONS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            new RDTJsonFormUtils().showToast(this, getApplicationContext().getString(R.string.rdt_permissions_required));
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

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
