package io.ona.rdt_app.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import io.ona.rdt_app.R;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static com.vijay.jsonwizard.utils.PermissionUtils.PHONE_STATE_PERMISSION;
import static edu.washington.cs.ubicomplab.rdt_reader.Constants.REQUEST_CAMERA_PERMISSION;

public class RDTJsonFormActivity extends JsonFormActivity {

    private RDTJsonFormUtils formUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        formUtils = new RDTJsonFormUtils();
        super.onCreate(savedInstanceState);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            new ImageUtil().requestCameraPermission(this);
        }
        modifyActionBarAppearance();
    }

    private void modifyActionBarAppearance() {
        ActionBar actionBar = getSupportActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.form_fragment_toolbar_color));
        actionBar.setBackgroundDrawable(colorDrawable);
    }

    @Override
    public void initializeFormFragment() {
        RDTJsonFormFragment jsonFormFragment = (RDTJsonFormFragment) RDTJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction().add(com.vijay.jsonwizard.R.id.container, jsonFormFragment).commitAllowingStateLoss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Intent resultIntent = new Intent();
            setResult(RESULT_CANCELED, resultIntent);
            formUtils.showToast(this, getApplicationContext().getString(R.string.camera_permissions_required));
            finish();
        } else if (requestCode == PHONE_STATE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
            Intent resultIntent = new Intent();
            setResult(RESULT_CANCELED, resultIntent);
            formUtils.showToast(this, getApplicationContext().getString(R.string.phone_state_permissions_required));
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void showPermissionDeniedDialog() {
        // do nothing
    }
}
