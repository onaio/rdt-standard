package io.ona.rdt_app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import org.opencv.core.Mat;

import edu.washington.cs.ubicomplab.rdt_reader.ImageQualityActivity;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import io.ona.rdt_app.activity.presenter.RDTCapturePresenter;

public class RDTCaptureActivity extends ImageQualityActivity implements ActivityCompat.OnRequestPermissionsResultCallback  {

    private static final String TAG = RDTCaptureActivity.class.getName();
    private RDTCapturePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new RDTCapturePresenter(this);
    }

    @Override
    protected void useCapturedImage(Mat result) {
        Log.i(TAG, "Processing captured image");
        byte[] byteArray = ImageUtil.matToRotatedByteArray(result);
        presenter.saveImage(getApplicationContext(), byteArray, System.currentTimeMillis());
    }
}
