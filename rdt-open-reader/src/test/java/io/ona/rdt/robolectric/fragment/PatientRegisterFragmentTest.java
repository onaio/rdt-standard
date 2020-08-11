package io.ona.rdt.robolectric.fragment;

import android.content.Intent;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;

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
public class PatientRegisterFragmentTest extends FragmentRobolectricTest {

    private FragmentScenario<PatientRegisterFragment> fragmentScenario;
    private PatientRegisterFragment patientRegisterFragment;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        fragmentScenario =
                FragmentScenario.launchInContainer(PatientRegisterFragment.class,
                        null, R.style.AppTheme, null);
        fragmentScenario.onFragment(fragment -> {
            patientRegisterFragment = fragment;
        });
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        assertNotNull(patientRegisterFragment.getPresenter());
        assertEquals(RDTApplicationPresenter.getRegisterTableName(),
                ReflectionHelpers.getField(patientRegisterFragment, "tablename"));
        assertTrue(patientRegisterFragment.isRefreshList());
    }

    @Test
    public void testOnViewClickedShouldLaunchPatientProfile() {
        Patient patient = getPatient();
        View view = mock(View.class);
        doReturn(patient).when(view).getTag(eq(R.id.patient_tag));
        ReflectionHelpers.callInstanceMethod(patientRegisterFragment, "onViewClicked",
                ReflectionHelpers.ClassParameter.from(View.class, view));

        Intent expectedIntent = new Intent(patientRegisterFragment.getActivity(), PatientProfileActivity.class);
        expectedIntent.putExtra(PATIENT, patient);
        Intent actualIntent = shadowOf(patientRegisterFragment.getActivity()).getNextStartedActivity();
        assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void testOnClickShouldPerformCorrectActionOnClick() {
        PatientRegisterFragmentPresenter presenter = mock(PatientRegisterFragmentPresenter.class);
        ReflectionHelpers.setField(patientRegisterFragment, "presenter", presenter);

        View view = mock(View.class);
        doReturn(R.id.btn_register_patient).when(view).getId();
        patientRegisterFragment.onClick(view);
        verify(presenter).launchForm(eq(patientRegisterFragment.getActivity()), eq(PATIENT_REGISTRATION_FORM), isNull());

        Patient patient = getPatient();
        doReturn(patient).when(view).getTag(eq(R.id.patient_tag));
        doReturn(R.id.btn_record_rdt_test).when(view).getId();
        patientRegisterFragment.onClick(view);
        verify(presenter).launchForm(eq(patientRegisterFragment.getActivity()), eq(RDT_TEST_FORM), eq(patient));
    }

    private Patient getPatient() {
        return new Patient("name", "sex", "entity_id", "patient_id");
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}
