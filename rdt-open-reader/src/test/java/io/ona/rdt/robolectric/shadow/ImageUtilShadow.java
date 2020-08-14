package io.ona.rdt.robolectric.shadow;

import android.content.Context;

import org.mockito.Mock;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import edu.washington.cs.ubicomplab.rdt_reader.utils.ImageUtil;

/**
 * Created by Vincent Karuri on 13/08/2020
 */

@Implements(ImageUtil.class)
public class ImageUtilShadow extends Shadow {

    private static MockCounter mockCounter = new MockCounter();

    @Implementation
    public static void saveImage(final Context context, final byte[] byteArray, final long timeTaken, final boolean testResult, final OnImageSavedCallBack onImageSavedCallBack) {
        mockCounter.setCount(1);
    }

    public static MockCounter getMockCounter() {
        return mockCounter;
    }
}
