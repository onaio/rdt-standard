package io.ona.rdt.robolectric.shadow;

import org.opencv.core.Mat;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

/**
 * Created by Vincent Karuri on 07/08/2020
 */

@Implements(Mat.class)
public class MatShadow extends Shadow {

    @Implementation
    public void __constructor__() {

    }
}
