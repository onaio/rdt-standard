package io.ona.rdt.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.android.material.navigation.NavigationView;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.domain.FetchStatus;
import org.smartregister.receiver.SyncStatusBroadcastReceiver;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.activity.BaseRegisterActivity;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_RDT_PERMISSIONS;
import static io.ona.rdt.util.Utils.updateLocale;

public class PatientRegisterActivity extends BaseRegisterActivity implements SyncStatusBroadcastReceiver.SyncStatusListener, OnFormSavedCallback, PatientRegisterActivityContract.View {

    public static final String LATEST_SYNC_DATE_FORMAT = "dd MMM hh:mm a";

    private DrawerLayout drawerLayout;
    private RDTJsonFormUtils formUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        super.onCreate(savedInstanceState);
        formUtils = getFormUtils();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
        requestPermissions();

        SyncStatusBroadcastReceiver.getInstance().addSyncStatusListener(this);
    }

    private RDTJsonFormUtils getFormUtils() {
        if (formUtils == null) {
            formUtils = initializeFormUtils();
        }
        return formUtils;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SyncStatusBroadcastReceiver.getInstance().removeSyncStatusListener(this);
    }

    protected RDTJsonFormUtils initializeFormUtils() {
        return new RDTJsonFormUtils();
    }

    public void requestPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_RDT_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_RDT_PERMISSIONS && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            formUtils.showToast(this, getString(R.string.rdt_permissions_required));
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void initializePresenter() {
        presenter = getPresenter();
    }

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        if (mBaseFragment == null) {
            mBaseFragment = new PatientRegisterFragment();
        }
        return mBaseFragment;
    }

    public PatientRegisterFragment getPatientRegisterFragment() {
        return (PatientRegisterFragment) getRegisterFragment();
    }

    @Override
    protected Fragment[] getOtherFragments() {
        return new Fragment[0];
    }

    @Override
    public void startFormActivity(String s, String s1, Map<String, String> map) {
        // TODO: implement this
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
        if (fetchStatus.equals(FetchStatus.fetched) || fetchStatus.equals(FetchStatus.nothingFetched)) {
            updateSyncDate();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == JsonFormConstants.RESULT_CODE.RUNTIME_EXCEPTION_OCCURRED) {
            // for exceptions thrown and caught during widget instantiation
            Bundle bundle = data.getExtras();
            throw  (RuntimeException) bundle.getSerializable(JsonFormConstants.RESULT_INTENT.RUNTIME_EXCEPTION);
        } else if (requestCode == REQUEST_CODE_GET_JSON && resultCode == RESULT_CANCELED) {
            // for general uncaught runtime exceptions thrown by json form activity
            throw new RuntimeException("Runtime exception thrown by json form activity!");
        } else if (requestCode == REQUEST_CODE_GET_JSON && resultCode == RESULT_OK && data != null) {
            String jsonForm = data.getStringExtra("json");
            Timber.d(jsonForm);
            getPresenter().saveForm(jsonForm, this);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onFormSaved() {
        if (mBaseFragment != null && mBaseFragment.getActivity() != null) {
            mBaseFragment.refreshListView();
        }
    }

    public void openDrawerLayout() {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void closeDrawerLayout() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        Menu menuNav = navigationView.getMenu();
        MenuItem imgSyncToggle = menuNav.findItem(R.id.menu_item_toggle_img_sync);
        View actionView = imgSyncToggle.getActionView();
        Switch imgSyncToggleBtn = actionView.findViewById(R.id.img_sync_switch_button);
        imgSyncToggleBtn.setOnCheckedChangeListener((buttonView, isChecked) -> allSharedPreferences.savePreference(IS_IMG_SYNC_ENABLED, String.valueOf(!Utils.isImageSyncEnabled())));

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                selectDrawerItem(menuItem);
                return true;
            }
        });
    }

    public boolean selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case R.id.menu_item_sync:
                closeDrawerLayout();
                Utils.scheduleJobsImmediately();
                return true;
            case R.id.menu_item_logout:
                logoutCurrentUser();
                return true;
        }
        return false;
    }

    public void logoutCurrentUser() {
        Intent intent = new Intent(getApplicationContext(), getLoginPage());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        getApplicationContext().startActivity(intent);
        RDTApplication.getInstance().getContext().userService().logoutSession();
    }

    protected Class getLoginPage() {
        return LoginActivity.class;
    }

    protected PatientRegisterActivityPresenter getPresenter() {
        if (presenter == null) {
            presenter = createPatientRegisterActivityPresenter();
        }
        return (PatientRegisterActivityPresenter) presenter;
    }

    protected PatientRegisterActivityPresenter createPatientRegisterActivityPresenter() {
        return new PatientRegisterActivityPresenter(this);
    }

    public void updateSyncDate() {
        Date lastSyncDate = new Date(System.currentTimeMillis());
        String lblSync = getString(R.string.lbl_latest_sync);
        TextView tvLatestSyncDate = findViewById(R.id.tv_latest_sync_date);
        tvLatestSyncDate.setText(String.format(lblSync, new SimpleDateFormat(LATEST_SYNC_DATE_FORMAT, Locale.getDefault()).format(lastSyncDate)));
        tvLatestSyncDate.setVisibility(View.VISIBLE);
    }
}
