package io.ona.rdt.robolectric.shadow;

/**
 * Created by Vincent Karuri on 23/07/2020
 */

import android.content.Context;
import android.util.AttributeSet;

import org.opencv.android.CameraBridgeViewBase;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowView;

@Implements(CameraBridgeViewBase.class)
public class CameraBridgeViewBaseShadow extends ShadowView {

    @Implementation
    public void  __constructor__(Context context, int cameraId) {
    }

    @Implementation
    public void  __constructor__(Context context, AttributeSet attrs) {
    }

    @Implementation
    public void disableView() {
    }
}
