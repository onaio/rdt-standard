package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.PatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientProfileFragmentInteractor;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 27/07/2020
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class})
public class PatientProfileFragmentPresenterTest {

    @Mock
    private PatientProfileFragmentInteractor interactor;
    @Mock
    PatientProfileFragmentContract.View fragment;
    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    protected EventClientRepository eventClientRepository;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    private PatientProfileFragmentPresenter presenter;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        presenter = new PatientProfileFragmentPresenter(fragment);
    }

    @Test
    public void testLaunchFormShouldLaunchRDTTestForm() throws JSONException {
        Whitebox.setInternalState(presenter, "patientProfileFragmentInteractor", interactor);
        Activity activity = mock(Activity.class);
        Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
        presenter.launchForm(activity, patient);
        verify(interactor).launchForm(eq(activity), eq(RDT_TEST_FORM), patientArgumentCaptor.capture());

        Patient actualPatient = patientArgumentCaptor.getValue();
        assertEquals(patient, actualPatient);
    }

    private void mockStaticMethods() {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.getEventClientRepository()).thenReturn(eventClientRepository);
    }
}
