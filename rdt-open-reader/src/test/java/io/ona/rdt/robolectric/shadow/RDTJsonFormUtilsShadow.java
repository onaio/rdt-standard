package io.ona.rdt.robolectric.shadow;

import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 10/08/2020
 */

@Implements(RDTJsonFormUtils.class)
public class RDTJsonFormUtilsShadow {

    @Implementation
    public static void saveStaticImagesToDisk(final Context context, CompositeImage compositeImage, final OnImageSavedCallback onImageSavedCallBack) {
    }
}
