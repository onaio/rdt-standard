package io.ona.rdt_app.activity;

import android.os.Bundle;
import android.util.Log;

import org.opencv.core.Mat;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;

import static io.ona.rdt_app.util.RDTJsonFormUtils.convertByteArrayToBitmap;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureActivity extends RDTCaptureActivity {

    private static final String TAG = CustomRDTCaptureActivity.class.getName();

    private CustomRDTCapturePresenter presenter;
    private String baseEntityId;
    private String providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CustomRDTCapturePresenter(this);
    }

    @Override
    protected void useCapturedImage(Mat result, ImageProcessor.InterpretationResult interpretationResult) {
        Log.i(TAG, "Processing captured image");
        byte[] byteArray = ImageUtil.matToRotatedByteArray(result);
        presenter.saveImage(convertByteArrayToBitmap(byteArray), providerId, baseEntityId, this);
    }
}
