package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormActivityPresenter;
import io.ona.rdt.robolectric.shadow.AllSharedPreferencesShadow;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.utils.PermissionUtils.PHONE_STATE_PERMISSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 22/07/2020
 */
@Config(sdk = Build.VERSION_CODES.O_MR1, shadows = {AllSharedPreferencesShadow.class})
public class RDTJsonFormActivityTest extends JsonFormActivityTest {

    private RDTJsonFormActivity rdtJsonFormActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        rdtJsonFormActivity = Robolectric.buildActivity(RDTJsonFormActivity.class, intent).create()
                .resume()
                .get();
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        assertNotNull(ReflectionHelpers.getField(rdtJsonFormActivity, "formUtils"));
        assertNotNull(ReflectionHelpers.getField(rdtJsonFormActivity, "presenter"));
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), rdtJsonFormActivity
                .getResources().getConfiguration().locale.getLanguage());
    }

    @Test
    public void testSetRdtTypeShouldUpdateRDTType() {
        rdtJsonFormActivity.setRdtType("rdt_type");
        assertEquals("rdt_type", rdtJsonFormActivity.getRdtType());
    }

    @Test
    public void testOnStopShouldDestroyStepStateConfigInstance() {
        StepStateConfig initialStepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();
        assertNotNull(initialStepStateConfig);
        rdtJsonFormActivity.onStop();
        assertNotEquals(initialStepStateConfig, RDTApplication.getInstance().getStepStateConfiguration());
    }

    @Test
    public void testOnRequestPermissionsResultShouldAlertPermissionsAreRequiredIfDenied() {
        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        ReflectionHelpers.setField(rdtJsonFormActivity, "formUtils", formUtils);
        rdtJsonFormActivity.onRequestPermissionsResult(PHONE_STATE_PERMISSION, new String[]{},
                new int[]{PackageManager.PERMISSION_DENIED});
        verify(formUtils).showToast(any(Activity.class),
                eq(rdtJsonFormActivity.getString(R.string.phone_state_permissions_required)));
    }

    @Test
    public void testOnBackPressedShouldDeferToPresenterBackPressLogic() {
        RDTJsonFormActivityPresenter presenter = mock(RDTJsonFormActivityPresenter.class);
        ReflectionHelpers.setField(rdtJsonFormActivity, "presenter", presenter);
        rdtJsonFormActivity.onBackPressed();
        verify(presenter).onBackPress();
    }

    @Test
    public void testOnBackPressShouldSetCurrStepBeforeNavigatingBack() {
        assertEquals(1, RDTJsonFormFragment.getCurrentStep());
        rdtJsonFormActivity.getSupportFragmentManager().beginTransaction()
                .addToBackStack("step2").commit();
        rdtJsonFormActivity.onBackPress();
        assertEquals(2, RDTJsonFormFragment.getCurrentStep());
    }

    @Override
    public Activity getActivity() {
        return rdtJsonFormActivity;
    }
}
