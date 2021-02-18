package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.testing.FragmentScenario;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.PatientProfileFragment;
import io.ona.rdt.presenter.PatientProfileFragmentPresenter;
import io.ona.rdt.robolectric.util.BaseFormSaverTest;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class PatientProfileFragmentTest extends FragmentRobolectricTest {

    @Mock
    private PatientProfileFragmentPresenter patientProfileFragmentPresenter;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    private FragmentScenario<PatientProfileFragment> fragmentScenario;
    private PatientProfileFragment patientProfileFragment;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, BaseFormSaverTest.expectedPatient);
        fragmentScenario =
                FragmentScenario.launchInContainer(PatientProfileFragment.class,
                        bundle, R.style.AppTheme, null);
        fragmentScenario.onFragment(fragment -> {
            patientProfileFragment = fragment;
        });
    }

    @Test
    public void testOnViewClickedShouldPerformAppropriateAction() {
        View view = mock(View.class);
        doReturn(R.id.btn_profile_record_rdt_test).when(view).getId();
        ReflectionHelpers.setField(patientProfileFragment, "patientProfileFragmentPresenter", patientProfileFragmentPresenter);
        ReflectionHelpers.setField(patientProfileFragment, "currPatient", BaseFormSaverTest.expectedPatient);

        patientProfileFragment.onClick(view);
        verify(patientProfileFragmentPresenter).launchForm(eq(patientProfileFragment.getActivity()), patientArgumentCaptor.capture());
        Patient actualPatient = patientArgumentCaptor.getValue();
        assertNotNull(actualPatient);
        assertEquals(BaseFormSaverTest.expectedPatient.getPatientName(), actualPatient.getPatientName());
        assertEquals(BaseFormSaverTest.expectedPatient.getPatientSex(), actualPatient.getPatientSex());
        assertEquals(BaseFormSaverTest.expectedPatient.getBaseEntityId(), actualPatient.getBaseEntityId());
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}
