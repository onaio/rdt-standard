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

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.interactor.CustomRDTCaptureInteractor;
import io.ona.rdt.util.RDTJsonFormUtils;

import static io.ona.rdt.TestUtils.getTestFilePath;
import static org.junit.Assert.assertEquals;
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
    public void testBuildCompositeImage() {
        mockStaticMethods();
        ImageProcessor.CaptureResult captureResult = mock(ImageProcessor.CaptureResult.class);
        Whitebox.setInternalState(captureResult, "boundary", mock(MatOfPoint2f.class));
        presenter.buildCompositeImage(captureResult, mock(ImageProcessor.InterpretationResult.class), 0l);
    }

    private void mockStaticMethods() {
        mockStatic(ImageUtil.class);

        mockStatic(RDTJsonFormUtils.class);
        PowerMockito.when(RDTJsonFormUtils.convertByteArrayToBitmap(AdditionalMatchers.or(any(), isNull()))).thenReturn(mock(Bitmap.class));

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
