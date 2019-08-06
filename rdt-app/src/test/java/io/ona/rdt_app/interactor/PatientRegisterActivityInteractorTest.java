package io.ona.rdt_app.interactor;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.expectedPatient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class})
public class PatientRegisterActivityInteractorTest {

    private PatientRegisterActivityInteractor interactor;
    private static JSONArray formFields;
    private static JSONObject formJsonObj;

    @BeforeClass
    public static void init() throws JSONException {
        formFields = getFormFields(new JSONObject(PatientRegisterFragmentInteractorTest.JSON_FORM));
        formJsonObj = new JSONObject(PatientRegisterFragmentInteractorTest.JSON_FORM);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        interactor = new PatientRegisterActivityInteractor();
    }

    @Test
    public void getPatientForRDTReturnsValidPatient() throws JSONException {
        Patient rdtPatient = interactor.getPatientForRDT(formJsonObj);
        assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), expectedPatient.getPatientName());
        assertEquals(rdtPatient.getPatientSex(), expectedPatient.getPatientSex());
        assertEquals(rdtPatient.getBaseEntityId(), expectedPatient.getBaseEntityId());
    }

    private static JSONArray getFormFields(JSONObject formJsonObj) throws JSONException {
        return formJsonObj.getJSONObject("step1").getJSONArray("fields");
    }
}
