package io.ona.rdt.robolectric.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.AppExecutors;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ReflectionHelpers;

import java.util.concurrent.Executor;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 24/07/2020
 */
public class RDTJsonFormFragmentTest extends FragmentRobolectricTest {

    private FragmentScenario<RDTJsonFormFragment> fragmentScenario;
    private RDTJsonFormFragment jsonFormFragment;
    private String PRESENTER_FIELD = "presenter";
    private String STEP_3 = "step3";

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Bundle args = new Bundle();
        args.putString(JsonFormConstants.STEPNAME, STEP_3);
        fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        args, R.style.AppTheme, new RDTJsonFormFragmentFactory());
        fragmentScenario.onFragment(fragment -> {
            jsonFormFragment = fragment;
            fragment.setmJsonApi(Mockito.mock(JsonApi.class));
            Whitebox.setInternalState(jsonFormFragment, PRESENTER_FIELD, Mockito.mock(RDTJsonFormFragmentPresenter.class));
        });
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        assertNotNull(jsonFormFragment.getRootLayout());
        assertNotNull(ReflectionHelpers.getField(jsonFormFragment, PRESENTER_FIELD));
    }

    @Test
    public void testGetFormFragmentShouldSetStepAndReturnValidJsonFormFragment() {
        RDTJsonFormFragment formFragment = (RDTJsonFormFragment) jsonFormFragment.getFormFragment(STEP_3);
        assertNotNull(formFragment);
        assertEquals(STEP_3, jsonFormFragment.getCurrentStep());
    }

    @Test
    public void testFormHasSpecialNavigationRulesShouldReturnCorrectStatus() {
        String methodName = "formHasSpecialNavigationRules";
        assertTrue(ReflectionHelpers.callInstanceMethod(jsonFormFragment,
                methodName,
                ReflectionHelpers.ClassParameter.from(String.class, RDT_TEST)));
        assertFalse(ReflectionHelpers.callInstanceMethod(jsonFormFragment,
                methodName,
                ReflectionHelpers.ClassParameter.from(String.class, "form")));
    }

    @Test
    public void testSetNextButtonStateShouldSetCorrectState() {
        View view = Mockito.mock(View.class);
        doReturn(Mockito.mock(GradientDrawable.class)).when(view).getBackground();

        jsonFormFragment.setNextButtonState(view, false);
        verify(view).setEnabled(eq(false));
        verify((GradientDrawable) view.getBackground()).setColor(eq(Color.parseColor("#D1D1D1")));

        jsonFormFragment.setNextButtonState(view, true);
        verify(view).setEnabled(eq(true));
        verify((GradientDrawable) view.getBackground()).setColor(eq(Color.parseColor("#0192D4")));
    }

    @Test
    public void testNavigateToNextStepShouldNavigateToNextStep() {
        JsonFormFragmentPresenter presenter = Mockito.mock(JsonFormFragmentPresenter.class);
        ReflectionHelpers.setField(jsonFormFragment, PRESENTER_FIELD, presenter);
        jsonFormFragment.navigateToNextStep();
        verify(presenter).onNextClick(any());
    }

    @Test
    public void testSaveFormShouldSaveForm() {
        JsonFormFragmentPresenter presenter = Mockito.mock(JsonFormFragmentPresenter.class);
        ReflectionHelpers.setField(jsonFormFragment, PRESENTER_FIELD, presenter);
        jsonFormFragment.saveForm();
        verify(presenter).onSaveClick(any());
    }

    @Test
    public void testBackClickShouldShowConfirmationDialog() {
        jsonFormFragment = Mockito.spy(jsonFormFragment);
        FragmentActivity activity = Mockito.spy(jsonFormFragment.getActivity());

        // show confirmation dialog
        Mockito.doReturn(activity).when(jsonFormFragment).getActivity();
        jsonFormFragment.backClick();
        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        assertNotNull(alertDialog);

        // click yes
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
        Mockito.verify(activity).setResult(Activity.RESULT_OK);
        Mockito.verify(activity).finish();

        // click no
        jsonFormFragment.backClick();
        alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertTrue(alertDialog.isShowing());
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        Assert.assertFalse(alertDialog.isShowing());
    }

    @Test
    public void testNavigationShouldNavigateToCorrectStep() {
        ReflectionHelpers.callInstanceMethod(jsonFormFragment, "initializeBottomNavigation",
                ReflectionHelpers.ClassParameter.from(JSONObject.class, new JSONObject()),
                ReflectionHelpers.ClassParameter.from(View.class, jsonFormFragment.getRootLayout()));
        RDTJsonFormFragmentPresenter presenter = Mockito.mock(RDTJsonFormFragmentPresenter.class);

        ReflectionHelpers.setField(jsonFormFragment, PRESENTER_FIELD, presenter);
        jsonFormFragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.previous_button).performClick();
        verify(presenter).onSaveClick(any());

        JsonApi jsonApi = Mockito.mock(JsonApi.class);
        JSONObject mJsonObject = new JSONObject();
        doReturn(mJsonObject).when(jsonApi).getmJSONObject();
        ReflectionHelpers.setField(jsonFormFragment, "mJsonApi", jsonApi);
        jsonFormFragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.next_button).performClick();
        verify(jsonFormFragment.getFragmentPresenter()).submitOrMoveToNextStep(any());

        try {
            mJsonObject.put(ENCOUNTER_TYPE, RDT_TEST);
            jsonFormFragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.next_button).performClick();
            verify(jsonFormFragment.getFragmentPresenter()).performNextButtonAction(anyString(), any());
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }

    public static class RDTJsonFormFragmentFactory extends FragmentFactory {
        @NonNull
        public Fragment instantiate(@NonNull ClassLoader classLoader, @NonNull String className) {
            RDTJsonFormFragment jsonFormFragment = new RDTJsonFormFragment();
            JsonApi jsonApi = Mockito.mock(JsonApi.class);
            AppExecutors appExecutors = Mockito.mock(AppExecutors.class);
            doReturn(appExecutors).when(jsonApi).getAppExecutors();
            doReturn(Mockito.mock(Executor.class)).when(appExecutors).mainThread();
            doReturn(Mockito.mock(Executor.class)).when(appExecutors).diskIO();
            jsonFormFragment.setmJsonApi(jsonApi);
            ReflectionHelpers.setField(jsonFormFragment, "presenter", Mockito.mock(RDTJsonFormFragmentPresenter.class));
            return jsonFormFragment;
        }
    }
}
