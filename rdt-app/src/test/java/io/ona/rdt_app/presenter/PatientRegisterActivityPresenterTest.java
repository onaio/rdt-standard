package io.ona.rdt_app.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import io.ona.rdt_app.contract.PatientRegisterActivityContract;
import io.ona.rdt_app.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.JSON_FORM;
import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.expectedPatient;
import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 02/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormUtils.class})
public class PatientRegisterActivityPresenterTest {

    private PatientRegisterActivityContract.View activity;
    private PatientRegisterActivityPresenter presenter;
    private Activity context = mock(Activity.class);
    private PatientRegisterActivityInteractor interactor;

    @Captor
    private ArgumentCaptor<Patient> patientArgumentCaptor;

    @Before
    public void setUp() {
        activity = spy(new PatientRegisterActivityStub());
        presenter = new PatientRegisterActivityPresenter(activity, context);
        interactor = new PatientRegisterActivityInteractor();
    }

    @Test
    public void testSaveFormShouldSaveForm() throws JSONException {
        Whitebox.setInternalState(presenter, "interactor", interactor);
        when(interactor.getPatientForRDT(any(JSONObject.class))).thenReturn(expectedPatient);
        mockStatic(RDTJsonFormUtils.class);

        presenter.saveForm(JSON_FORM, null);
        verify(interactor).saveForm(any(JSONObject.class), eq(activity));
        verify(interactor).getPatientForRDT(any(JSONObject.class));
        verify(interactor).launchForm(eq(context), eq(RDT_TEST_FORM), patientArgumentCaptor.capture());

        Patient rdtPatient = patientArgumentCaptor.getValue();
        assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), expectedPatient.getPatientName());
        assertEquals(rdtPatient.getPatientSex(), expectedPatient.getPatientSex());
        assertEquals(rdtPatient.getBaseEntityId(), expectedPatient.getBaseEntityId());

        PowerMockito.verifyStatic();
        RDTJsonFormUtils.appendEntityId(any(JSONObject.class));
    }
}
