package io.ona.rdt.robolectric;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

import android.os.Build;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.CredentialsHelper;
import org.smartregister.view.activity.DrishtiApplication;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.shadow.BaseLoaderCallbackShadow;
import io.ona.rdt.robolectric.shadow.CameraBridgeViewBaseShadow;
import io.ona.rdt.robolectric.shadow.ClientCoreUtilsShadow;
import io.ona.rdt.robolectric.shadow.ClientProcessorForJavaShadow;
import io.ona.rdt.robolectric.shadow.FirebaseCrashlyticsShadow;
import io.ona.rdt.robolectric.shadow.FormDataRepositoryShadow;
import io.ona.rdt.robolectric.shadow.GpsDialogShadow;
import io.ona.rdt.robolectric.shadow.ImageUtilShadow;
import io.ona.rdt.robolectric.shadow.JsonFormFragmentPresenterShadow;
import io.ona.rdt.robolectric.shadow.JsonFormFragmentShadow;
import io.ona.rdt.robolectric.shadow.MatShadow;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.robolectric.shadow.SQLiteDatabaseShadow;
import io.ona.rdt.robolectric.shadow.SQLiteOpenHelperShadow;

@RunWith(AndroidJUnit4.class)
@Config(sdk = Build.VERSION_CODES.O_MR1,
        shadows = {ClientCoreUtilsShadow.class, FirebaseCrashlyticsShadow.class,
        FormDataRepositoryShadow.class, CameraBridgeViewBaseShadow.class,
        BaseLoaderCallbackShadow.class, OpenSRPContextShadow.class, JsonFormFragmentShadow.class,
        JsonFormFragmentPresenterShadow.class, SQLiteOpenHelperShadow.class,
        SQLiteDatabaseShadow.class, GpsDialogShadow.class, MatShadow.class,
        ClientProcessorForJavaShadow.class, ImageUtilShadow.class})
public abstract class RobolectricTest {

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        CredentialsHelper credentialsHelper = Mockito.mock(CredentialsHelper.class);
        Mockito.doReturn("password".getBytes()).when(credentialsHelper).getCredentials(ArgumentMatchers.anyString(), ArgumentMatchers.anyString());
        ReflectionHelpers.setField(DrishtiApplication.getInstance(), "credentialsHelper", credentialsHelper);
    }

    @After
    public void tearDown() {
        Context context = RDTApplication.getInstance().getContext();
        Mockito.reset(context.getUniqueIdRepository());
        Mockito.reset(context.getEventClientRepository());
        Mockito.reset(ClientProcessorForJava.getInstance(Mockito.mock(android.content.Context.class)));
        OpenSRPContextShadow.setAllSettings(null);
        OpenSRPContextShadow.setUniqueIdRepository(null);
        ReflectionHelpers.setField(context, "uniqueIdRepository", null);
        ImageUtilShadow.getMockCounter().setCount(0);
    }
}
