package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowActivity;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.activity.RDTExpirationDateActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.shadow.AllSharedPreferencesShadow;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE_RESULT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Vincent Karuri on 23/07/2020
 */
@Config(sdk = Build.VERSION_CODES.O_MR1, shadows = {AllSharedPreferencesShadow.class})
public class RDTExpirationActivityTest extends ActivityRobolectricTest {

    private ActivityController<RDTExpirationDateActivity> controller;
    private RDTExpirationDateActivity rdtExpirationDateActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        controller = Robolectric.buildActivity(RDTExpirationDateActivity.class);
        rdtExpirationDateActivity = controller.create()
                .resume()
                .get();
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), rdtExpirationDateActivity
                .getResources().getConfiguration().locale.getLanguage());
    }

    @Test
    public void testStateTransitionsShouldUpdateCurrActivityInApplication() {
        RDTApplication rdtApplication = RDTApplication.getInstance();
        assertEquals(rdtExpirationDateActivity, rdtApplication.getCurrentActivity());
        controller.pause();
        assertNull(rdtApplication.getCurrentActivity());
        controller.resume();
        assertEquals(rdtExpirationDateActivity, rdtApplication.getCurrentActivity());
        controller.destroy();
        assertNull(rdtApplication.getCurrentActivity());
    }

    @Test
    public void testOnBackPressedShouldSetResultCancelled() {
        ShadowActivity shadowActivity = shadowOf(rdtExpirationDateActivity);
        rdtExpirationDateActivity.onBackPressed();
        assertEquals(RESULT_CANCELED, shadowActivity.getResultCode());
        assertTrue(rdtExpirationDateActivity.isFinishing());
    }

    @Test
    public void testOnResultShouldSetExpDateValidationResult() {
        ShadowActivity shadowActivity = shadowOf(rdtExpirationDateActivity);
        ReflectionHelpers.callInstanceMethod(rdtExpirationDateActivity, "onResult",
                ReflectionHelpers.ClassParameter.from(String.class, "12/12/2012"),
                ReflectionHelpers.ClassParameter.from(boolean.class, false));

        Intent resultIntent = shadowActivity.getResultIntent();
        assertTrue(resultIntent.getBooleanExtra(EXPIRATION_DATE_RESULT, false));
        assertEquals("12/12/2012", resultIntent.getStringExtra(EXPIRATION_DATE));
        assertEquals(RESULT_OK, shadowActivity.getResultCode());
        assertTrue(rdtExpirationDateActivity.isFinishing());
    }

    @Override
    public Activity getActivity() {
        return rdtExpirationDateActivity;
    }
}
