package io.ona.rdt.interactor;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.FormSaver;

import static io.ona.rdt.util.FormSaverTest.PATIENT_REGISTRATION_JSON_FORM;
import static io.ona.rdt.util.FormSaverTest.expectedPatient;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class})
public class PatientRegisterActivityInteractorTest {

    private PatientRegisterActivityInteractor interactor;
    private static JSONObject formJsonObj;

    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    protected EventClientRepository eventClientRepository;

    @BeforeClass
    public static void init() throws JSONException {
        formJsonObj = new JSONObject(PATIENT_REGISTRATION_JSON_FORM);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        interactor = new PatientRegisterActivityInteractor();
    }

    @Test
    public void getPatientForRDTReturnsValidPatient() throws JSONException {
        Patient rdtPatient = interactor.getPatientForRDT(formJsonObj);
        assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), expectedPatient.getPatientName());
        assertEquals(rdtPatient.getPatientSex(), expectedPatient.getPatientSex());
        assertEquals(rdtPatient.getBaseEntityId(), expectedPatient.getBaseEntityId());
        assertEquals(rdtPatient.getPatientId(), expectedPatient.getPatientId());
    }

    @Test
    public void testSaveFormShouldSaveForm() {
        JSONObject jsonForm = mock(JSONObject.class);
        OnFormSavedCallback callback = mock(OnFormSavedCallback.class);

        FormSaver formSaver =  mock(FormSaver.class);
        Whitebox.setInternalState(interactor, "formSaver", formSaver);
        interactor.saveForm(jsonForm, callback);
        verify(formSaver).saveForm(eq(jsonForm), eq(callback));
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
