package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.PatientProfileFragment;
import io.ona.rdt.presenter.PatientProfileFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.util.BaseFormSaverTest.expectedPatient;
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
public class PatientProfileFragmentTest extends RobolectricTest {

    @Mock
    private PatientProfileFragmentPresenter patientProfileFragmentPresenter;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testOnViewClickedShouldPerformAppropriateAction() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, expectedPatient);
        FragmentScenario<PatientProfileFragment> fragmentScenario =
                FragmentScenario.launchInContainer(PatientProfileFragment.class,
                        bundle, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            View view = mock(View.class);
            doReturn(R.id.btn_profile_record_rdt_test).when(view).getId();
            ReflectionHelpers.setField(fragment, "patientProfileFragmentPresenter", patientProfileFragmentPresenter);
            ReflectionHelpers.setField(fragment, "currPatient", expectedPatient);

            fragment.onClick(view);
            verify( patientProfileFragmentPresenter).launchForm(eq(fragment.getActivity()), patientArgumentCaptor.capture());
            Patient actualPatient = patientArgumentCaptor.getValue();
            assertNotNull(actualPatient);
            assertEquals(expectedPatient.getPatientName(), actualPatient.getPatientName());
            assertEquals(expectedPatient.getPatientSex(), actualPatient.getPatientSex());
            assertEquals(expectedPatient.getBaseEntityId(), actualPatient.getBaseEntityId());
        });
    }
}
