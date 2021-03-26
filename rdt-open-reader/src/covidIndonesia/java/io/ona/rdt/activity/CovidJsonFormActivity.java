package io.ona.rdt.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Location;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

import static io.ona.rdt.util.CovidConstants.FormFields.LAST_KNOWN_LOCATION;
import static io.ona.rdt.util.CovidConstants.FormFields.LAT;
import static io.ona.rdt.util.CovidConstants.FormFields.LNG;
import static io.ona.rdt.util.CovidConstants.RequestCodes.LOCATION_PERMISSIONS;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormActivity extends RDTJsonFormActivity {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (RDTJsonFormUtils.isLocationServiceDisabled(this)) {
            RDTJsonFormUtils.showLocationServicesDialog(this);
        }

        requestPermissions();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationCallback = new LocationCallback();
        if (isLocationPermissionGranted()) {
            fusedLocationProviderClient.requestLocationUpdates(getLocationRequest(), locationCallback, null);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, getOnLocationSuccessListener());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.verifyUserAuthorization();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private LocationRequest getLocationRequest() {
        final int requestInterval = 5000;
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(requestInterval);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        return locationRequest;
    }

    @Override
    protected RDTJsonFormUtils initializeFormUtils() {
        return new CovidRDTJsonFormUtils();
    }

    public void requestPermissions() {
        if (!isLocationPermissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSIONS);
        }
    }

    private boolean isLocationPermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSIONS && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            formUtils.showToast(this, getString(R.string.location_permissions));
            finish();
        }
    }

    @Override
    protected JsonFormFragment getFirstStep() {
        return CovidJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
    }

    private OnSuccessListener<Location> getOnLocationSuccessListener() {
        return location -> {
            if (location != null) {
                try {
                    JSONObject locationObj = new JSONObject();
                    locationObj.put(LNG, location.getLongitude());
                    locationObj.put(LAT, location.getLatitude());
                    RDTApplication.getInstance().getContext().allSettings().put(LAST_KNOWN_LOCATION,
                            locationObj.toString());
                } catch (JSONException e) {
                    Timber.e(e);
                }
            }
        };
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean touchEvent = super.dispatchTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View view = getCurrentFocus();
            if (view instanceof MaterialEditText) {
                Rect outRect = new Rect();
                view.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    view.clearFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
        return touchEvent;
    }
}
