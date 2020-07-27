package io.ona.rdt.robolectric.shadow;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.InstallCallbackInterface;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

/**
 * Created by Vincent Karuri on 23/07/2020
 */

@Implements(BaseLoaderCallback.class)
public class BaseLoaderCallbackShadow extends Shadow {

    @Implementation
    public void onPackageInstall(final int operation, final InstallCallbackInterface callback) {

    }
}
