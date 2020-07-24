package io.ona.rdt.robolectric.fragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;

import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import io.ona.rdt.R;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 24/07/2020
 */
public class RDTJsonFormFragmentTest extends RobolectricTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertNotNull(fragment.getRootLayout());
        });
    }

    @Test
    public void testGetFormFragmentShouldSetStepAndReturnValidJsonFormFragment() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            fragment.setCurrentStep(2);
            RDTJsonFormFragment formFragment = (RDTJsonFormFragment) fragment.getFormFragment("step3");
            assertNotNull(formFragment);
            assertEquals("step3", formFragment.getArguments().getString("stepName"));
            assertEquals(3, fragment.getCurrentStep());
            assertEquals(2, (int) ReflectionHelpers.getField(fragment, "prevStep"));
        });
    }

    @Test
    public void testFormHasSpecialNavigationRulesShouldReturnCorrectStatus() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertTrue(ReflectionHelpers.callInstanceMethod(fragment,
                    "formHasSpecialNavigationRules",
                    ReflectionHelpers.ClassParameter.from(String.class, RDT_TEST)));
            assertFalse(ReflectionHelpers.callInstanceMethod(fragment,
                    "formHasSpecialNavigationRules",
                    ReflectionHelpers.ClassParameter.from(String.class, "form")));
        });
    }

    @Test
    public void testSetNextButtonStateShouldSetCorrectState() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View view = mock(View.class);
            doReturn(mock(GradientDrawable.class)).when(view).getBackground();

            fragment.setNextButtonState(view, false);
            verify(view).setEnabled(eq(false));
            verify(((GradientDrawable) view.getBackground())).setColor(eq(Color.parseColor("#D1D1D1")));

            fragment.setNextButtonState(view, true);
            verify(view).setEnabled(eq(true));
            verify(((GradientDrawable) view.getBackground())).setColor(eq(Color.parseColor("#0192D4")));
        });
    }

    @Test
    public void testNavigateToNextStepShouldNavigateToNextStep() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            JsonFormFragmentPresenter presenter = mock(JsonFormFragmentPresenter.class);
            ReflectionHelpers.setField(fragment, "presenter", presenter);
            fragment.navigateToNextStep();
            verify(presenter).onNextClick(any());
        });
    }

    @Test
    public void testSaveFormShouldSaveForm() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            JsonFormFragmentPresenter presenter = mock(JsonFormFragmentPresenter.class);
            ReflectionHelpers.setField(fragment, "presenter", presenter);
            fragment.saveForm();
            verify(presenter).onSaveClick(any());
        });
    }

    @Test
    public void testbBackClickShouldShowConfirmationDialog() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            fragment.backClick();
            assertNotNull(ShadowAlertDialog.getLatestDialog());
            assertTrue(ShadowAlertDialog.getLatestDialog() instanceof AlertDialog);
        });
    }

    @Test
    public void testNavigationShouldNavigateToCorrectStep() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            ReflectionHelpers.callInstanceMethod(fragment, "initializeBottomNavigation",
                    ReflectionHelpers.ClassParameter.from(JSONObject.class, new JSONObject()),
                    ReflectionHelpers.ClassParameter.from(View.class, fragment.getRootLayout()));
            RDTJsonFormFragmentPresenter presenter = mock(RDTJsonFormFragmentPresenter.class);

            ReflectionHelpers.setField(fragment, "presenter", presenter);
            fragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.previous_button).performClick();
            verify(presenter).onSaveClick(any());

            JsonApi jsonApi = mock(JsonApi.class);
            JSONObject mJsonObject = new JSONObject();
            doReturn(mJsonObject).when(jsonApi).getmJSONObject();
            ReflectionHelpers.setField(fragment, "mJsonApi", jsonApi);
            fragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.next_button).performClick();
            verify(fragment.getFragmentPresenter()).submitOrMoveToNextStep(any());

            try {
                mJsonObject.put(ENCOUNTER_TYPE, RDT_TEST);
                fragment.getRootLayout().findViewById(com.vijay.jsonwizard.R.id.next_button).performClick();
                verify(fragment.getFragmentPresenter()).performNextButtonAction(anyString(), any());
            } catch (JSONException e) {
                Timber.e(e);
            }
        });
    }
}
