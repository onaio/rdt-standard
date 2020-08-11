package io.ona.rdt.robolectric.listener;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import io.ona.rdt.listener.RDTImageListener;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;

/**
 * Created by Vincent Karuri on 07/08/2020
 */

@Config(shadows = {RDTJsonFormUtilsShadow.class})
public class RDTImageListenerTest extends RobolectricTest {

    private RDTImageListener imageListener;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        imageListener = new RDTImageListener(Mockito.mock(ImageView.class), "entity_id", 0, 0);
    }

    @Test
    public void testOnResponse() {
        ImageLoader.ImageContainer imageContainer = Mockito.mock(ImageLoader.ImageContainer.class);
        Bitmap bitmap = Mockito.mock(Bitmap.class);
        Mockito.doReturn(bitmap).when(imageContainer).getBitmap();
        imageListener.onResponse(imageContainer, false);
    }
}
