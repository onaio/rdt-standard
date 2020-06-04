package io.ona.rdt.interactor;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.presenter.CustomRDTCapturePresenter;
import io.ona.rdt.util.RDTJsonFormUtils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormUtils.class})
public class CustomRDTCaptureInteractorTest {

    private CustomRDTCaptureInteractor interactor;

    @Before
    public void setUp() {
        interactor = new CustomRDTCaptureInteractor(mock(CustomRDTCapturePresenter.class));
    }

    @Test
    public void testSaveImageShouldSaveImage() {
        mockStaticClasses();

        Context context = mock(Context.class);
        CompositeImage compositeImage = mock(CompositeImage.class);
        OnImageSavedCallback callback = mock(OnImageSavedCallback.class);

        interactor.saveImage(context, compositeImage, callback);
        verifyStatic(RDTJsonFormUtils.class, times(1));
        RDTJsonFormUtils.saveStaticImagesToDisk(eq(context), eq(compositeImage), eq(callback));
    }

    private void mockStaticClasses() {
        mockStatic(RDTJsonFormUtils.class);
    }
}
