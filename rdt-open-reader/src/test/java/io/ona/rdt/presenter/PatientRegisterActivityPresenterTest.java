package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt.util.RDTJsonFormUtils;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.FormSaverTest.PATIENT_REGISTRATION_JSON_FORM;
import static io.ona.rdt.util.FormSaverTest.expectedPatient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 02/08/2019
 */
@PrepareForTest({RDTJsonFormUtils.class})
public class PatientRegisterActivityPresenterTest extends PowerMockTest {

    private PatientRegisterActivityContract.View activity;
    private PatientRegisterActivityPresenter presenter;

    @Mock
    private PatientRegisterActivityInteractor interactor;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        activity = spy(new PatientRegisterActivityStub());
        presenter = new PatientRegisterActivityPresenter(activity);
    }

    @Test
    public void testSaveFormShouldSaveForm() throws JSONException {
        Whitebox.setInternalState(presenter, "interactor", interactor);
        doReturn(expectedPatient).when(interactor).getPatientForRDT(any(JSONObject.class));
        mockStatic(RDTJsonFormUtils.class);

        presenter.saveForm(PATIENT_REGISTRATION_JSON_FORM, activity);
        verify(interactor).saveForm(any(JSONObject.class), eq(activity));
        verify(interactor).getPatientForRDT(any(JSONObject.class));
        verify(interactor).launchForm(eq((Activity) activity), eq(RDT_TEST_FORM), patientArgumentCaptor.capture());

        Patient rdtPatient = patientArgumentCaptor.getValue();
        assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), expectedPatient.getPatientName());
        assertEquals(rdtPatient.getPatientSex(), expectedPatient.getPatientSex());
        assertEquals(rdtPatient.getBaseEntityId(), expectedPatient.getBaseEntityId());

        PowerMockito.verifyStatic(RDTJsonFormUtils.class);
        RDTJsonFormUtils.appendEntityId(any(JSONObject.class));
    }
}
