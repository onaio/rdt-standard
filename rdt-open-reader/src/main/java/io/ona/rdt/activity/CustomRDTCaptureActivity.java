package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.CustomRDTCaptureContract;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.UnParcelableImageMetadata;
import io.ona.rdt.presenter.CustomRDTCapturePresenter;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static io.ona.rdt.util.RDTJsonFormUtils.convertByteArrayToBitmap;
import static io.ona.rdt.util.Utils.hideProgressDialogFromFG;
import static io.ona.rdt.util.Utils.showProgressDialogInFG;
import static io.ona.rdt.util.Utils.updateLocale;
import static io.ona.rdt.widget.CustomRDTCaptureFactory.CAPTURE_TIMEOUT;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureActivity extends RDTCaptureActivity implements CustomRDTCaptureContract.View {

    private static final String TAG = CustomRDTCaptureActivity.class.getName();

    private CustomRDTCapturePresenter presenter;
    private String baseEntityId;
    private String providerID;
    private boolean isManualCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        super.onCreate(savedInstanceState);
        hideProgressDialog();
        presenter = new CustomRDTCapturePresenter(this);
        providerID = RDTApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        baseEntityId = getIntent().getStringExtra(ENTITY_ID);
        isManualCapture = false;
        showManualCaptureBtnDelayed(getIntent().getLongExtra(CAPTURE_TIMEOUT, 0));
    }

    @Override
    public void useCapturedImage(ImageProcessor.CaptureResult captureResult, ImageProcessor.InterpretationResult interpretationResult, long timeTaken) {
        Log.i(TAG, "Processing captured image");
        showProgressDialogInFG(this, R.string.saving_image, R.string.please_wait);
        presenter.saveImage(this, buildCompositeImage(captureResult, interpretationResult, timeTaken), this);
    }

    private CompositeImage buildCompositeImage(ImageProcessor.CaptureResult captureResult, ImageProcessor.InterpretationResult interpretationResult, long timeTaken) {

        final byte[] fullImage = ImageUtil.matToRotatedByteArray(captureResult.resultMat);
        byte[] croppedImage = fullImage;

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(interpretationResult);

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withBaseEntityId(baseEntityId)
                .withProviderId(providerID)
                .withTimeTaken(timeTaken)
                .withFlashOn(captureResult.flashEnabled)
                .withLineReadings(new LineReadings(false, false, false))
                .withManualCapture(isManualCapture)
                .withCassetteBoundary("(0, 0), (0, 0), (0, 0), (0, 0)");

        if (!isManualCapture) {
            croppedImage = ImageUtil.matToRotatedByteArray(interpretationResult.resultMat);
            unParcelableImageMetadata.withBoundary(captureResult.boundary.toArray());
            parcelableImageMetadata.withLineReadings(new LineReadings(interpretationResult.topLine, interpretationResult.middleLine, interpretationResult.bottomLine))
                    .withCassetteBoundary(presenter.formatPoints(unParcelableImageMetadata.getBoundary()));
        }


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

    private void showManualCaptureBtnDelayed(long milliseconds) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               modifyLayout();
            }
        }, milliseconds);
    }

    private void modifyLayout() {
        ImageButton btnManualCapture = mImageQualityView.findViewById(R.id.btn_manual_capture);
        btnManualCapture.setVisibility(View.VISIBLE);
        btnManualCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageQualityView.captureImage();
                isManualCapture = true;
            }
        });
        mImageQualityView.findViewById(R.id.textInstruction).setVisibility(View.GONE);
        mImageQualityView.findViewById(R.id.img_quality_feedback_view).setVisibility(View.GONE);
        mImageQualityView.findViewById(R.id.manual_capture_instructions).setVisibility(View.VISIBLE);
    }
}
