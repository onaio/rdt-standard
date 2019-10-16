package io.ona.rdt_app.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt_app.R;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.contract.CustomRDTCaptureContract;

import io.ona.rdt_app.domain.CompositeImage;
import io.ona.rdt_app.domain.LineReadings;
import io.ona.rdt_app.domain.ParcelableImageMetadata;
import io.ona.rdt_app.domain.UnParcelableImageMetadata;

import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static io.ona.rdt_app.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
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

        presenter.saveImage(this, buildCompositeImage(captureResult, interpretationResult, timeTaken), this);
    }

    private CompositeImage buildCompositeImage(ImageProcessor.CaptureResult captureResult, ImageProcessor.InterpretationResult interpretationResult, long timeTaken) {

        final byte[] fullImage = ImageUtil.matToRotatedByteArray(captureResult.resultMat);
        final byte[] croppedImage = ImageUtil.matToRotatedByteArray(interpretationResult.resultMat);

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(interpretationResult)
                .withBoundary(captureResult.boundary.toArray());

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withBaseEntityId(baseEntityId)
                .withProviderId(providerID)
                .withTimeTaken(timeTaken)
                .withFlashOn(captureResult.flashEnabled)
                .withLineReadings(new LineReadings(interpretationResult.topLine, interpretationResult.middleLine, interpretationResult.bottomLine))
                .withCassetteBoundary(presenter.formatPoints(unParcelableImageMetadata.getBoundary()));

        CompositeImage compositeImage = new CompositeImage();
        compositeImage.withFullImage(convertByteArrayToBitmap(fullImage))
                .withCroppedImage(convertByteArrayToBitmap(croppedImage))
                .withParcelableImageMetadata(parcelableImageMetadata)
                .withUnParcelableImageMetadata(unParcelableImageMetadata);

        return compositeImage;
    }

    @Override
    public void onImageSaved(CompositeImage compositeImage) {
        hideProgressDialogFromFG(this);
        if (compositeImage != null) {
            ParcelableImageMetadata parcelableImageMetadata = compositeImage.getParcelableImageMetadata();
            Intent resultIntent = new Intent();
            resultIntent.putExtra(PARCELABLE_IMAGE_METADATA, parcelableImageMetadata);
            setResult(RESULT_OK, resultIntent);
        } else {
            Log.e(TAG, "Could not save image due to incomplete data");
        }
        finish();
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
