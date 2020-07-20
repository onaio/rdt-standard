package io.ona.rdt.robolectric.activity;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.ona.rdt.robolectric.shadow.ClientCoreUtilsShadow;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ClientCoreUtilsShadow.class})
public abstract class  RobolectricTest {
}
