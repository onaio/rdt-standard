package io.ona.rdt.robolectric;

/**
 * Created by Vincent Karuri on 20/11/2019
 */

import android.os.Build;

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.ona.rdt.BuildConfig;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public abstract class BaseRobolectricTest {


}