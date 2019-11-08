package io.ona.rdt.presenter;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Point;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.interactor.CustomRDTCaptureInteractor;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CustomRDTCapturePresenterTest {

    private CustomRDTCapturePresenter presenter;
    private CustomRDTCaptureActivityStub activity;

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
}
