package io.ona.rdt.robolectric.listener;

import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.android.volley.toolbox.ImageLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import io.ona.rdt.listener.RDTImageListener;
import io.ona.rdt.robolectric.RobolectricTest;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 07/08/2020
 */
public class RDTImageListenerTest extends RobolectricTest {

    private RDTImageListener imageListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageListener = new RDTImageListener(mock(RemoteViews.class), 0, 0, 0);
    }

    @Test
    public void testOnResponse() {
        ImageLoader.ImageContainer imageContainer = mock(ImageLoader.ImageContainer.class);
        Bitmap bitmap = mock(Bitmap.class);
        doReturn(bitmap).when(imageContainer).getBitmap();
        imageListener.onResponse(imageContainer, false);
    }
}
