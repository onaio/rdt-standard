package io.ona.rdt_app.activity;

import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt_app.R;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.contract.CustomRDTCaptureContract;
import io.ona.rdt_app.domain.ImageMetaData;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static io.ona.rdt_app.util.Constants.Test.CASSETTE_BOUNDARY;
import static io.ona.rdt_app.util.Constants.Test.CROPPED_IMG_ID;
import static io.ona.rdt_app.util.Constants.Test.FLASH_ON;
import static io.ona.rdt_app.util.Constants.Test.FULL_IMG_ID_AND_TIME_STAMP;
import static io.ona.rdt_app.util.Constants.Test.RDT_CAPTURE_DURATION;
import static io.ona.rdt_app.util.Constants.Test.TEST_CONTROL_RESULT;
import static io.ona.rdt_app.util.Constants.Test.TEST_PF_RESULT;
import static io.ona.rdt_app.util.Constants.Test.TEST_PV_RESULT;
import static io.ona.rdt_app.util.RDTJsonFormUtils.convertByteArrayToBitmap;
import static io.ona.rdt_app.util.Utils.hideProgressDialogFromFG;
import static io.ona.rdt_app.util.Utils.showProgressDialogInFG;
import static io.ona.rdt_app.util.Utils.updateLocale;
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
        updateLocale(this);
        super.onCreate(savedInstanceState);
        hideProgressDialog();
        presenter = new CustomRDTCapturePresenter(this);
        providerID = RDTApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        baseEntityId = getIntent().getStringExtra(ENTITY_ID);
    }

    @Override
    public void useCapturedImage(ImageProcessor.CaptureResult captureResult, ImageProcessor.InterpretationResult interpretationResult, long timeTaken) {
        Log.i(TAG, "Processing captured image");

        showProgressDialogInFG(this, R.string.saving_image, R.string.please_wait);

        final byte[] fullImage = ImageUtil.matToRotatedByteArray(captureResult.resultMat);
        final byte[] croppedImage = ImageUtil.matToRotatedByteArray(interpretationResult.resultMat);
        ImageMetaData imageMetaData = new ImageMetaData();
        imageMetaData.withFullImage(convertByteArrayToBitmap(fullImage))
                .withBaseEntityId(baseEntityId)
                .withProviderId(providerID)
                .withInterpretationResult(interpretationResult)
                .withTimeTaken(timeTaken)
                .withCroppedImage(convertByteArrayToBitmap(croppedImage))
                .withFlashStatus(captureResult.flashEnabled)
                .withBoundary(captureResult.boundary.toArray());

        presenter.saveImage(this, imageMetaData, this);
    }

    @Override
    public void onImageSaved(ImageMetaData imageMetaData) {
        hideProgressDialogFromFG(this);
        if (imageMetaData != null) {
            Map<String, String> keyVals = new HashMap();
            ImageProcessor.InterpretationResult interpretationResult = imageMetaData.getInterpretationResult();
            keyVals.put(FULL_IMG_ID_AND_TIME_STAMP, imageMetaData.getFullImageId() + "," + imageMetaData.getImageTimeStamp());
            keyVals.put(TEST_CONTROL_RESULT, String.valueOf(interpretationResult.topLine));
            keyVals.put(TEST_PV_RESULT, String.valueOf(interpretationResult.middleLine));
            keyVals.put(TEST_PF_RESULT, String.valueOf(interpretationResult.bottomLine));
            keyVals.put(RDT_CAPTURE_DURATION, String.valueOf(imageMetaData.getCaptureDuration()));
            keyVals.put(FLASH_ON, String.valueOf(imageMetaData.getFlashStatus()));
            keyVals.put(CROPPED_IMG_ID, imageMetaData.getCroppedImageId());
            keyVals.put(CASSETTE_BOUNDARY, presenter.formatPoints(imageMetaData.getBoundary()));
            setResult(RESULT_OK, getResultIntent(keyVals));
        } else {
            Log.e(TAG, "Could not save null image path");
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
