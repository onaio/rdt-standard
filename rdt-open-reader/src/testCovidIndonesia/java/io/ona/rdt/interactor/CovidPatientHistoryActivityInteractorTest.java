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
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.TestUtils;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.util.FormSaver;

import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
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
public class CovidPatientHistoryActivityInteractorTest {

    private CovidPatientHistoryActivityInteractor interactor;
    private static JSONObject formJsonObj;

    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    protected EventClientRepository eventClientRepository;

    @BeforeClass
    public static void init() throws JSONException {
        formJsonObj = new JSONObject(TestUtils.PATIENT_REGISTRATION_JSON_FORM);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        interactor = new CovidPatientHistoryActivityInteractor();
    }

    @Test
    public void testGetPatientHistoryEntries() throws JSONException {
        List<PatientHistoryEntry> rdtPatient = interactor.getPatientHistoryEntries("", "", "");
        assertNotNull(rdtPatient);
        //assertEquals(rdtPatient.getPatientName(), FormSaverTest.expectedPatient.getPatientName());
        //assertEquals(rdtPatient.getPatientSex(), FormSaverTest.expectedPatient.getPatientSex());
        //assertEquals(rdtPatient.getBaseEntityId(), FormSaverTest.expectedPatient.getBaseEntityId());
        //assertEquals(rdtPatient.getPatientId(), FormSaverTest.expectedPatient.getPatientId());
    }

    @Test
    public void testSaveFormShouldSaveForm() {
        JSONObject jsonForm = mock(JSONObject.class);
        OnFormSavedCallback callback = mock(OnFormSavedCallback.class);

        FormSaver formSaver =  mock(FormSaver.class);
        Whitebox.setInternalState(interactor, "formSaver", formSaver);
        //interactor.saveForm(jsonForm, callback);
        verify(formSaver).saveForm(eq(jsonForm), eq(callback));
    }

    protected Event getEvent() {
        Event event = new Event();
        event.setObs(new ArrayList<>());
        Obs obs = new Obs();
        obs.setValue("unique_id");
        obs.setFieldCode(RDT_ID);
        event.getObs().add(obs);
        return event;
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
