package io.ona.rdt.robolectric;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.ona.rdt.robolectric.shadow.BaseLoaderCallbackShadow;
import io.ona.rdt.robolectric.shadow.CameraBridgeViewBaseShadow;
import io.ona.rdt.robolectric.shadow.ClientCoreUtilsShadow;
import io.ona.rdt.robolectric.shadow.FirebaseCrashlyticsShadow;
import io.ona.rdt.robolectric.shadow.FormDataRepositoryShadow;
import io.ona.rdt.robolectric.shadow.JsonFormFragmentPresenterShadow;
import io.ona.rdt.robolectric.shadow.JsonFormFragmentShadow;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.robolectric.shadow.SQLiteDatabaseShadow;
import io.ona.rdt.robolectric.shadow.SQLiteOpenHelperShadow;
import io.ona.rdt.robolectric.shadow.UtilsShadow;

@RunWith(AndroidJUnit4.class)
@Config(shadows = {ClientCoreUtilsShadow.class, FirebaseCrashlyticsShadow.class,
        FormDataRepositoryShadow.class, UtilsShadow.class, CameraBridgeViewBaseShadow.class,
        BaseLoaderCallbackShadow.class, OpenSRPContextShadow.class, JsonFormFragmentShadow.class,
        JsonFormFragmentPresenterShadow.class, SQLiteOpenHelperShadow.class,
        SQLiteDatabaseShadow.class})
public abstract class RobolectricTest {
}
