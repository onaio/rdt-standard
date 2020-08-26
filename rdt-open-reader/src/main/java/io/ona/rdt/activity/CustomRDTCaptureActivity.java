package io.ona.rdt.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import edu.washington.cs.ubicomplab.rdt_reader.activity.RDTCaptureActivity;
import edu.washington.cs.ubicomplab.rdt_reader.core.RDTCaptureResult;
import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;
import io.ona.rdt.R;
import io.ona.rdt.contract.CustomRDTCaptureContract;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.presenter.CustomRDTCapturePresenter;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static io.ona.rdt.util.Utils.hideProgressDialogFromFG;
import static io.ona.rdt.util.Utils.showProgressDialogInFG;
import static io.ona.rdt.util.Utils.updateLocale;
import static io.ona.rdt.widget.UWRDTCaptureFactory.CAPTURE_TIMEOUT;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureActivity extends RDTCaptureActivity implements CustomRDTCaptureContract.View {

    private static final String TAG = CustomRDTCaptureActivity.class.getName();

    private CustomRDTCapturePresenter presenter;
    private String baseEntityId;
    private boolean isManualCapture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
        super.onCreate(savedInstanceState);
        hideProgressDialog();
        presenter = new CustomRDTCapturePresenter(this);
        baseEntityId = getIntent().getStringExtra(ENTITY_ID);
        isManualCapture = false;
        showManualCaptureBtnDelayed(getIntent().getLongExtra(CAPTURE_TIMEOUT, 0));
    }

    @Override
    public void useCapturedImage(RDTCaptureResult rdtCaptureResult, RDTInterpretationResult rdtInterpretationResult, long timeTaken) {
        Log.i(TAG, "Processing captured image");
        showProgressDialogInFG(this, R.string.saving_image, R.string.please_wait);
        presenter.saveImage(this, presenter.buildCompositeImage(rdtCaptureResult, rdtInterpretationResult, timeTaken), this);
    }


    @Override
    public void onImageSaved(CompositeImage compositeImage) {
        hideProgressDialogFromFG(this);
        if (compositeImage != null) {
            ParcelableImageMetadata parcelableImageMetadata = compositeImage.getParcelableImageMetadata();
            parcelableImageMetadata.setManualCapture(isManualCapture);

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
        handler.postDelayed(() -> modifyLayout(), milliseconds);
    }

    private void modifyLayout() {
        ImageButton btnManualCapture = mImageQualityView.findViewById(R.id.btn_manual_capture);
        btnManualCapture.setVisibility(View.VISIBLE);
        btnManualCapture.setOnClickListener(v -> {
            mImageQualityView.captureImage();
            isManualCapture = true;
        });
        mImageQualityView.findViewById(R.id.textInstruction).setVisibility(View.GONE);
        mImageQualityView.findViewById(R.id.img_quality_feedback_view).setVisibility(View.GONE);
        mImageQualityView.findViewById(R.id.manual_capture_instructions).setVisibility(View.VISIBLE);
    }

    @Override
    public String getBaseEntityId() {
        return baseEntityId;
    }

    @Override
    public boolean isManualCapture() {
        return isManualCapture;
    }
}
