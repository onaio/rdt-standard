package io.ona.rdt_app.activity;

import android.os.Bundle;
import android.util.Log;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;

import static io.ona.rdt_app.util.RDTJsonFormUtils.convertByteArrayToBitmap;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureActivity extends RDTCaptureActivity {

    private static final String TAG = CustomRDTCaptureActivity.class.getName();

    private CustomRDTCapturePresenter presenter;
    private String baseEntityId;
    private String providerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new CustomRDTCapturePresenter(this);
        providerID = RDTApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        baseEntityId = getIntent().getStringExtra(ENTITY_ID);
    }

    @Override
    protected void useCapturedImage(byte[] captureByteArray, byte[] windowByteArray, ImageProcessor.InterpretationResult interpretationResult, long timeTaken) {
        Log.i(TAG, "Processing captured image");
        presenter.saveImage(this, convertByteArrayToBitmap(captureByteArray), providerID, baseEntityId, this);
    }
}
