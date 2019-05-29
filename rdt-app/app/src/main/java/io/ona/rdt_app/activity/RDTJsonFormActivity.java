package io.ona.rdt_app.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.vijay.jsonwizard.activities.JsonFormActivity;

import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;

import static com.vijay.jsonwizard.utils.PermissionUtils.PHONE_STATE_PERMISSION;
import static edu.washington.cs.ubicomplab.rdt_reader.Constants.REQUEST_CAMERA_PERMISSION;

public class RDTJsonFormActivity extends JsonFormActivity {

    private Bundle savedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        new ImageUtil().requestCameraPermission(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if (requestCode == REQUEST_CAMERA_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.propertyManager.grantPhoneStatePermission();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED, resultIntent);
                showToast("RDT image capture requires camera permission");
                finish();
            }
        } else if (requestCode == PHONE_STATE_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initializeFormFragment();
            } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                showToast("Phone state permissions are required to submit this form");
                finish();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showToast(final String text) {
        final Activity activity = this;
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
