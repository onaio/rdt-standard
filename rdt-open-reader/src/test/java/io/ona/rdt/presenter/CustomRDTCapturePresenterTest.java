package io.ona.rdt.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.view.activity.DrishtiApplication;

import edu.washington.cs.ubicomplab.rdt_reader.core.RDTCaptureResult;
import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;
import edu.washington.cs.ubicomplab.rdt_reader.utils.ImageUtil;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.contract.CustomRDTCaptureContract;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.UnParcelableImageMetadata;
import io.ona.rdt.interactor.CustomRDTCaptureInteractor;
import io.ona.rdt.util.RDTJsonFormUtils;

import static io.ona.rdt.TestUtils.getTestFilePath;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ImageUtil.class, RDTJsonFormUtils.class, RDTApplication.class, org.smartregister.Context.class, DrishtiApplication.class})
public class CustomRDTCapturePresenterTest {

    private CustomRDTCapturePresenter presenter;
    private CustomRDTCaptureActivityStub activity;

    @Mock
    private Bitmap image;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Before
    public void setUp() {
        activity = new CustomRDTCaptureActivityStub();
        presenter = new CustomRDTCapturePresenter(activity);
    }

    @Test
    public void testSaveImageShouldSaveImage() {
        CustomRDTCaptureInteractor interactor = mock(CustomRDTCaptureInteractor.class);
        Context context = mock(Context.class);
        CompositeImage compositeImage = mock(CompositeImage.class);
        OnImageSavedCallback callback = mock(OnImageSavedCallback.class);

        Whitebox.setInternalState(presenter, "interactor", interactor);
        presenter.saveImage(context, compositeImage, callback);
        verify(interactor).saveImage(eq(context), eq(compositeImage), eq(callback));
    }

    @Test
    public void testFormatPointsShouldCorrectlyFormatPoints() {
        Point[] points = new Point[] {new Point(1, 2), new Point(-1, 3), new Point(4, 5), new Point(7, 9)};
        assertEquals("(1.0, 2.0), (-1.0, 3.0), (4.0, 5.0), (7.0, 9.0)", presenter.formatPoints(points));
    }

    @Test
    public void testBuildCompositeImageShouldBuildCorrectCompositeImage() {
        mockStaticMethods();

        CustomRDTCaptureContract.View activity = mock(CustomRDTCaptureContract.View.class);
        doReturn("base_entity_id").when(activity).getBaseEntityId();
        doReturn(true).when(activity).isManualCapture();
        Whitebox.setInternalState(presenter, "activity", activity);

        RDTCaptureResult rdtCaptureResult = mock(RDTCaptureResult.class);
        Whitebox.setInternalState(rdtCaptureResult, "boundary", mock(MatOfPoint2f.class));
        Whitebox.setInternalState(rdtCaptureResult, "flashEnabled", true);
        RDTInterpretationResult rdtInterpretationResult = mock(RDTInterpretationResult.class);
        Whitebox.setInternalState(rdtInterpretationResult, "topLine", true);
        Whitebox.setInternalState(rdtInterpretationResult, "bottomLine", false);
        Whitebox.setInternalState(rdtInterpretationResult, "middleLine", true);
        CompositeImage compositeImage = presenter.buildCompositeImage(rdtCaptureResult, rdtInterpretationResult, 0l);

        ParcelableImageMetadata parcelableImageMetadata = compositeImage.getParcelableImageMetadata();
        assertEquals("base_entity_id", parcelableImageMetadata.getBaseEntityId());
        assertEquals(0l, parcelableImageMetadata.getCaptureDuration());
        assertTrue(parcelableImageMetadata.isFlashOn());
        assertEquals("(0, 0), (0, 0), (0, 0), (0, 0)", parcelableImageMetadata.getCassetteBoundary());

        LineReadings lineReadings = parcelableImageMetadata.getLineReadings();
        assertFalse(lineReadings.isBottomLine());
        assertFalse(lineReadings.isMiddleLine());
        assertFalse(lineReadings.isTopLine());

        UnParcelableImageMetadata unParcelableImageMetadata = compositeImage.getUnParcelableImageMetadata();
        assertEquals("anm_id", parcelableImageMetadata.getProviderId());
        assertEquals(rdtInterpretationResult, unParcelableImageMetadata.getRdtInterpretationResult());

        assertEquals(image, compositeImage.getCroppedImage());
        assertEquals(image, compositeImage.getFullImage());

        doReturn(false).when(activity).isManualCapture();
        Whitebox.setInternalState(presenter, "activity", activity);
        Whitebox.setInternalState(rdtCaptureResult, "flashEnabled", false);
        rdtInterpretationResult = mock(RDTInterpretationResult.class);
        Whitebox.setInternalState(rdtInterpretationResult, "topLine", true);
        Whitebox.setInternalState(rdtInterpretationResult, "bottomLine", false);
        Whitebox.setInternalState(rdtInterpretationResult, "middleLine", true);
        compositeImage = presenter.buildCompositeImage(rdtCaptureResult, rdtInterpretationResult, 0l);

        lineReadings = compositeImage.getParcelableImageMetadata().getLineReadings();
        assertFalse(lineReadings.isBottomLine());
        assertTrue(lineReadings.isMiddleLine());
        assertTrue(lineReadings.isTopLine());
    }

    private void mockStaticMethods() {
        mockStatic(ImageUtil.class);

        mockStatic(RDTJsonFormUtils.class);
        PowerMockito.when(RDTJsonFormUtils.convertByteArrayToBitmap(AdditionalMatchers.or(any(), isNull()))).thenReturn(image);

        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        AllSharedPreferences sharedPreferences = mock(AllSharedPreferences.class);
        doReturn("anm_id").when(sharedPreferences).fetchRegisteredANM();
        doReturn(sharedPreferences).when(drishtiContext).allSharedPreferences();

        // Drishti
        mockStatic(DrishtiApplication.class);
        PowerMockito.when(DrishtiApplication.getAppDir()).thenReturn(getTestFilePath());
    }
}
