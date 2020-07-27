package io.ona.rdt.robolectric.fragment;

import android.content.Intent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.util.Constants.Form.PATIENT_REGISTRATION_FORM;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.robolectric.Shadows.shadowOf;

/**
 * Created by Vincent Karuri on 24/07/2020
 */
public class PatientRegisterFragmentTest extends RobolectricTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        FragmentScenario<PatientRegisterFragment> fragmentScenario =
                FragmentScenario.launchInContainer(PatientRegisterFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertNotNull(fragment.getPresenter());
            assertEquals(RDTApplicationPresenter.getRegisterTableName(),
                    ReflectionHelpers.getField(fragment, "tablename"));
            assertTrue(fragment.isRefreshList());
        });
    }

    @Test
    public void testOnViewClickedShouldLaunchPatientProfile() {
        FragmentScenario<PatientRegisterFragment> fragmentScenario =
                FragmentScenario.launchInContainer(PatientRegisterFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
            View view = mock(View.class);
            doReturn(patient).when(view).getTag(eq(R.id.patient_tag));
            ReflectionHelpers.callInstanceMethod(fragment, "onViewClicked",
                    ReflectionHelpers.ClassParameter.from(View.class, view));

            Intent expectedIntent = new Intent(fragment.getActivity(), PatientProfileActivity.class);
            expectedIntent.putExtra(PATIENT, patient);
            Intent actualIntent = shadowOf(fragment.getActivity()).getNextStartedActivity();
            assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        });
    }

    @Test
    public void testOnClickShouldPerformCorrectActionOnClick() {
        FragmentScenario<PatientRegisterFragment> fragmentScenario =
                FragmentScenario.launchInContainer(PatientRegisterFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            PatientRegisterFragmentPresenter presenter = mock(PatientRegisterFragmentPresenter.class);
            ReflectionHelpers.setField(fragment, "presenter", presenter);

            View view = mock(View.class);
            doReturn(R.id.btn_register_patient).when(view).getId();
            fragment.onClick(view);
            verify(presenter).launchForm(eq(fragment.getActivity()), eq(PATIENT_REGISTRATION_FORM), isNull());

            Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
            doReturn(patient).when(view).getTag(eq(R.id.patient_tag));
            doReturn(R.id.btn_record_rdt_test).when(view).getId();
            fragment.onClick(view);
            verify(presenter).launchForm(eq(fragment.getActivity()), eq(RDT_TEST_FORM), eq(patient));
        });
    }
}
