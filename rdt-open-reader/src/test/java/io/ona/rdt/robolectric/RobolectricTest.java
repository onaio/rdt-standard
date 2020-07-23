package io.ona.rdt.robolectric;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import io.ona.rdt.robolectric.shadow.ClientCoreUtilsShadow;
import io.ona.rdt.robolectric.shadow.FirebaseCrashlyticsShadow;
import io.ona.rdt.robolectric.shadow.FormDataRepositoryShadow;
import io.ona.rdt.robolectric.shadow.CameraBridgeViewBaseShadow;
import io.ona.rdt.robolectric.shadow.UtilsShadow;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ClientCoreUtilsShadow.class, FirebaseCrashlyticsShadow.class,
        FormDataRepositoryShadow.class, UtilsShadow.class, CameraBridgeViewBaseShadow.class})
public abstract class  RobolectricTest {
}
