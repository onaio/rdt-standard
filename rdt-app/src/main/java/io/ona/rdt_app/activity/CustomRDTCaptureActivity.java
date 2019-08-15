package io.ona.rdt_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.contract.CustomRDTCaptureContract;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;

import static edu.washington.cs.ubicomplab.rdt_reader.Constants.SAVED_IMAGE_RESULT;
import static io.ona.rdt_app.util.Constants.SAVED_IMG_ID_AND_TIME_STAMP;
import static io.ona.rdt_app.util.RDTJsonFormUtils.convertByteArrayToBitmap;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureActivity extends RDTCaptureActivity implements CustomRDTCaptureContract.View {

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
        presenter.saveImage(this, convertByteArrayToBitmap(captureByteArray), providerID, baseEntityId, interpretTestResult(interpretationResult), this);
    }

    @Override
    public void onImageSaved(String imageMetaData) {
        if (imageMetaData != null) {
            Map<String, String> keyVals = new HashMap();
            String[] vals = imageMetaData.split(",");
            keyVals.put(SAVED_IMG_ID_AND_TIME_STAMP, vals[0] + "," + vals[1]);
            keyVals.put(SAVED_IMAGE_RESULT, vals[2]);
            setResult(RESULT_OK, getResultIntent(keyVals));
        } else {
            Log.e(TAG, "Could not save null image path");
        }
        finish();
    }
}
