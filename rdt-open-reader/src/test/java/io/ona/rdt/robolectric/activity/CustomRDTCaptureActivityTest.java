package io.ona.rdt.robolectric.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;

import com.vijay.jsonwizard.utils.Utils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import edu.washington.cs.ubicomplab.rdt_reader.core.RDTCaptureResult;
import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;
import edu.washington.cs.ubicomplab.rdt_reader.views.ImageQualityView;
import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.presenter.CustomRDTCapturePresenter;
import io.ona.rdt.robolectric.RobolectricTest;

import static android.app.Activity.RESULT_OK;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static io.ona.rdt.widget.UWRDTCaptureFactory.CAPTURE_TIMEOUT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 23/07/2020
 */
public class CustomRDTCaptureActivityTest extends RobolectricTest {

    private ActivityController<CustomRDTCaptureActivity> controller;
    private CustomRDTCaptureActivity customRDTCaptureActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Intent intent = new Intent();
        intent.putExtra(ENTITY_ID, "entity_id");
        intent.putExtra(CAPTURE_TIMEOUT, 1l);
        controller = Robolectric.buildActivity(CustomRDTCaptureActivity.class, intent);
        customRDTCaptureActivity = controller.create()
                .resume()
                .get();
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        Utils.showProgressDialog(R.string.please_wait_title, R.string.please_wait_message, customRDTCaptureActivity);
        controller.create();
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), customRDTCaptureActivity
                .getResources().getConfiguration().locale.getLanguage());
        assertNotNull(ReflectionHelpers.getField(customRDTCaptureActivity, "presenter"));
        assertEquals("entity_id", customRDTCaptureActivity.getBaseEntityId());
        assertFalse(customRDTCaptureActivity.isManualCapture());
        assertFalse(Utils.getProgressDialog().isShowing());
    }

    @Test
    public void testModifyLayoutShouldShowManualCaptureButton() {
        View mImageQualityView = ReflectionHelpers.getField(customRDTCaptureActivity, "mImageQualityView");
        View tvInstructions = mImageQualityView.findViewById(R.id.textInstruction);
        View tvFeedback = mImageQualityView.findViewById(R.id.img_quality_feedback_view);
        View tvManualCaptureInstructions = mImageQualityView.findViewById(R.id.manual_capture_instructions);
        ImageButton btnManualCapture = mImageQualityView.findViewById(R.id.btn_manual_capture);

        assertEquals(View.VISIBLE, tvInstructions.getVisibility());
        assertEquals(View.VISIBLE, tvFeedback.getVisibility());
        assertEquals(View.GONE, tvManualCaptureInstructions.getVisibility());
        assertEquals(View.GONE, btnManualCapture.getVisibility());

        ReflectionHelpers.callInstanceMethod(customRDTCaptureActivity, "modifyLayout");
        assertEquals(View.GONE, tvInstructions.getVisibility());
        assertEquals(View.GONE, tvFeedback.getVisibility());
        assertEquals(View.VISIBLE, tvManualCaptureInstructions.getVisibility());
        assertEquals(View.VISIBLE, btnManualCapture.getVisibility());
    }

    @Test
    public void testCaptureButtonShouldSetManualCaptureBooleanAndCaptureImageOnClick() {
        assertFalse(customRDTCaptureActivity.isManualCapture());

        ReflectionHelpers.callInstanceMethod(customRDTCaptureActivity, "modifyLayout");

        View mImageQualityView = ReflectionHelpers.getField(customRDTCaptureActivity, "mImageQualityView");
        ImageButton btnManualCapture = mImageQualityView.findViewById(R.id.btn_manual_capture);
        ImageQualityView imageQualityView = mock(ImageQualityView.class);

        ReflectionHelpers.setField(customRDTCaptureActivity, "mImageQualityView", imageQualityView);
        btnManualCapture.performClick();

        assertTrue(customRDTCaptureActivity.isManualCapture());
        verify(imageQualityView).captureImage();
    }

    @Test
    public void testUseCapturedImageShouldSaveImage() {
        assertFalse(Utils.getProgressDialog().isShowing());

        CompositeImage compositeImage = mock(CompositeImage.class);
        CustomRDTCapturePresenter presenter = mock(CustomRDTCapturePresenter.class);
        doReturn(compositeImage).when(presenter).buildCompositeImage(any(RDTCaptureResult.class),
                any(RDTInterpretationResult.class), anyLong());
        ReflectionHelpers.setField(customRDTCaptureActivity, "presenter", presenter);

        customRDTCaptureActivity.useCapturedImage(mock(RDTCaptureResult.class), mock(RDTInterpretationResult.class), 0l);

        verify(presenter).saveImage(eq(customRDTCaptureActivity), eq(compositeImage), eq(customRDTCaptureActivity));
        assertTrue(Utils.getProgressDialog().isShowing());
    }

    @Test
    public void testOnImageSavedShouldAddCompositeImageAsCaptureResult() {
        Utils.showProgressDialog(R.string.please_wait_title, R.string.please_wait_message, customRDTCaptureActivity);

        ReflectionHelpers.setField(customRDTCaptureActivity, "isManualCapture", true);

        CompositeImage compositeImage = new CompositeImage();
        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        compositeImage.setParcelableImageMetadata(parcelableImageMetadata);

        ShadowActivity shadowActivity = shadowOf(customRDTCaptureActivity);
        customRDTCaptureActivity.onImageSaved(compositeImage);

        Intent resultIntent = shadowActivity.getResultIntent();
        assertEquals(parcelableImageMetadata, resultIntent.getParcelableExtra(PARCELABLE_IMAGE_METADATA));
        assertEquals(RESULT_OK, shadowActivity.getResultCode());
        assertTrue(customRDTCaptureActivity.isFinishing());
        assertFalse(Utils.getProgressDialog().isShowing());
    }
}
