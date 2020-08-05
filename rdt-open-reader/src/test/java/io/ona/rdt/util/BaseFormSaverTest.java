package io.ona.rdt.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.ona.rdt.TestUtils;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.RDTApplicationPresenter;

import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 16/07/2020
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class, JsonFormUtils.class, AssetHandler.class, RDTJsonFormUtils.class})
public abstract class BaseFormSaverTest {

    protected final String UNIQUE_ID = "unique_id";

    @Mock
    private RDTApplication rdtApplication;
    @Mock
    protected org.smartregister.Context drishtiContext;
    @Mock
    private Context context;
    @Mock
    protected EventClientRepository eventClientRepository;
    @Mock
    protected AllSettings settings;
    @Mock
    private ClientProcessorForJava clientProcessor;
    @Mock
    private UniqueIdRepository uniqueIdRepository;

    @Mock
    private StepStateConfig stepStateConfig;

    @Captor
    protected ArgumentCaptor<FormLaunchArgs> formLaunchArgsArgumentCaptor;

    public static final String PATIENT_NAME = "Mr. Patient";
    public static final String PATIENT_GENDER = "Male";
    public static final String PATIENT_BASE_ENTITY_ID = "2b66831d";
    public static final String PATIENT_ID = "patient id";
    public static final int PATIENT_AGE = 20;

    private Map<String, String > properties = new HashMap();

    public static final Patient expectedPatient = new Patient(PATIENT_NAME, PATIENT_GENDER, PATIENT_BASE_ENTITY_ID, PATIENT_ID);

    protected static JSONArray formFields;
    protected static JSONObject formJsonObj;


    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        formJsonObj = new JSONObject(TestUtils.getPatientRegistrationJsonForm(getPatientRegistrationEventType()));
        formFields = getFormFields(formJsonObj);
    }

    @Test
    public void testSaveEventClientShouldSaveEventAndClient() throws Exception {
        Whitebox.invokeMethod(getFormSaver(), "saveEventClient", formJsonObj, getPatientRegistrationEventType(), getRDTPatientBindType());
        verify(eventClientRepository).addorUpdateClient(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
        verify(eventClientRepository).addEvent(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
    }

    @Test
    public void testCloseRDTIdShouldCloseRDTId() throws Exception {
        Whitebox.invokeMethod(getFormSaver(), "closeIDs", getDBEvent());
        verifyIDsAreClosed(getNumOfIDsToClose());
    }

    @Test
    public void testProcessAndSaveFormShouldProcessClient() throws Exception {
        mockStaticMethods();
        processFormAndVerifyIDsAreClosed();
        verify(clientProcessor, times(1)).processClient(any(List.class));
    }

    private void processFormAndVerifyIDsAreClosed() throws Exception {
        formJsonObj.put(ENCOUNTER_TYPE, getRDTTestEncounterType());
        Whitebox.invokeMethod(getFormSaver(), "processAndSaveForm", formJsonObj);
        verifyIDsAreClosed(getNumOfIDsToClose());
    }

    protected void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);
        doReturn(context).when(rdtApplication).getApplicationContext();

        RDTApplicationPresenter applicationPresenter = mock(RDTApplicationPresenter.class);

        properties = new HashMap();
        properties.put("property1", "property1");
        properties.put("property2", "property2");
        properties.put("property3", "property3");
        doReturn(properties).when(applicationPresenter).getPhoneProperties();

        PowerMockito.when(rdtApplication.getPresenter()).thenReturn(applicationPresenter);

        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn(RDT_ID).when(jsonObject).optString(eq(RDT_ID_KEY));
        Mockito.doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        // mock repositories
        PowerMockito.when(drishtiContext.getEventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(drishtiContext.getUniqueIdRepository()).thenReturn(uniqueIdRepository);
        PowerMockito.when(drishtiContext.allSharedPreferences()).thenReturn(mock(AllSharedPreferences.class));

        // mock client processor
        mockStatic(ClientProcessorForJava.class);
        PowerMockito.when(ClientProcessorForJava.getInstance(context)).thenReturn(clientProcessor);

        // mock JsonFormUtils
        mockStatic(JsonFormUtils.class);
        when(JsonFormUtils.getMultiStepFormFields(any(JSONObject.class))).thenReturn(new JSONArray());
        when(JsonFormUtils.getJSONObject(any(JSONObject.class), anyString())).thenReturn(new JSONObject());
        when(JsonFormUtils.getString(any(JSONObject.class), anyString())).thenReturn(PATIENT_BASE_ENTITY_ID);
        when(JsonFormUtils.createBaseClient(any(JSONArray.class), any(FormTag.class), anyString())).thenReturn(new Client(UUID.randomUUID().toString()));
        when(JsonFormUtils.createEvent(any(JSONArray.class), any(JSONObject.class), any(FormTag.class), anyString(), anyString(), anyString())).thenReturn(getEvent());
        when(JsonFormUtils.fields(any(JSONObject.class))).thenReturn(formFields);

        // mock RDTJsonFormUtils
        mockStatic(RDTJsonFormUtils.class);
        when(RDTJsonFormUtils.getFormTag()).thenReturn(new FormTag());

        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when( AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(TestUtils.PATIENT_REGISTRATION_JSON_FORM);
    }

    private static JSONArray getFormFields(JSONObject formJsonObj) throws JSONException {
        return formJsonObj.getJSONObject("step1").getJSONArray("fields");
    }

    private void verifyIDsAreClosed(int times) {
        verify(uniqueIdRepository, times(times)).close(eq(UNIQUE_ID));
    }

    protected org.smartregister.domain.Event getDBEvent() {
        org.smartregister.domain.Event dbEvent = new org.smartregister.domain.Event();
        org.smartregister.domain.Obs obs = new org.smartregister.domain.Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(RDT_ID);
        dbEvent.addObs(obs);
        return dbEvent;
    }

    protected Event getEvent() {
        Event event = new Event();
        event.setObs(new ArrayList<>());
        Obs obs = new Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(RDT_ID);
        event.getObs().add(obs);
        return event;
    }


    protected abstract String getPatientRegistrationEventType();

    protected abstract FormSaver getFormSaver();

    protected abstract String getRDTPatientBindType();

    protected abstract int getNumOfIDsToClose();

    protected abstract String getRDTTestEncounterType();
}
