package io.ona.rdt.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONArray;
import org.json.JSONException;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidJsonFormFragment;
import timber.log.Timber;

import static io.ona.rdt.util.CovidConstants.FormFields.LAST_KNOWN_LOCATION;
import static io.ona.rdt.util.CovidConstants.RequestCodes.LOCATION_PERMISSIONS;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormActivity extends RDTJsonFormActivity implements OnSuccessListener<Location> {

    private FusedLocationProviderClient fusedLocationProviderClient;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (isLocationPermissionGranted()) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, this);
        }
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

    @Override
    public void onSuccess(Location location) {
        if (location != null) {
            try {
                JSONArray locationArr = new JSONArray();
                locationArr.put(location.getLongitude());
                locationArr.put(location.getLatitude());
                RDTApplication.getInstance().getContext().allSettings().put(LAST_KNOWN_LOCATION,
                        locationArr.toString());
            } catch (JSONException e) {
                Timber.e(e);
            }
        }
    }
}
