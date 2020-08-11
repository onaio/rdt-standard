package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormActivityPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
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
public class RDTJsonFormActivityTest extends ActivityRobolectricTest {

    private ActivityController<RDTJsonFormActivity> controller;
    private RDTJsonFormActivity rdtJsonFormActivity;

    private Intent intent;
    private JSONObject mJSONObject;

    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        mockMethods();
        controller = Robolectric.buildActivity(RDTJsonFormActivity.class, intent);
        rdtJsonFormActivity = controller.create()
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

    private void mockMethods() throws JSONException {
        mJSONObject = new JSONObject();
        mJSONObject.put(STEP1, new JSONObject());
        mJSONObject.put(JsonFormConstants.ENCOUNTER_TYPE, "encounter_type");
        intent = new Intent();
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.JSON, mJSONObject.toString());
    }

    @Override
    public Activity getActivity() {
        return rdtJsonFormActivity;
    }
}
