package io.ona.rdt.interactor;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
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
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.FormLaunchArgs;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt.util.Constants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.Constants.Encounter.PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Encounter.PCR_RESULT;
import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.FormFields.RESPIRATORY_SAMPLE_ID;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.Constants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static io.ona.rdt.util.Constants.Table.RDT_TESTS;
import static io.ona.rdt.util.Utils.isCovidApp;
import static io.ona.rdt.util.Utils.isMalariaApp;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class, JsonFormUtils.class, AssetHandler.class})
public class PatientRegisterFragmentInteractorTest {

    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context drishtiContext;
    @Mock
    private Context context;
    @Mock
    private EventClientRepository eventClientRepository;
    @Mock
    private ClientProcessorForJava clientProcessor;
    @Mock
    private UniqueIdRepository uniqueIdRepository;

    @Mock
    private StepStateConfig stepStateConfig;

    @Captor
    private ArgumentCaptor<FormLaunchArgs> formLaunchArgsArgumentCaptor;

    private static final String PATIENT_NAME = "Mr. Patient";
    private static final String PATIENT_GENDER = "Male";
    private static final String PATIENT_BASE_ENTITY_ID = "2b66831d";
    private static final String PATIENT_ID = "patient id";
    private static final int PATIENT_AGE = 20;
    private Map<String, String > properties = new HashMap();

    public static final Patient expectedPatient = new Patient(PATIENT_NAME, PATIENT_GENDER, PATIENT_BASE_ENTITY_ID, PATIENT_ID);


    private PatientRegisterFragmentInteractor interactor;

    public static final String PATIENT_REGISTRATION_JSON_FORM = "{\"count\":\"1\",\"entity_id\": \"" + PATIENT_BASE_ENTITY_ID + "\",\"encounter_type\":\"patient_registration\", \"metadata\": {},\"step1\":{\"title\":\"New client record\",\"display_back_button\":\"true\",\"previous_label\":\"SAVE AND EXIT\"," +
            "\"bottom_navigation\":\"true\",\"bottom_navigation_orientation\":\"vertical\",\"next_type\":\"submit\",\"submit_label\":\"SAVE\",\"next_form\":\"json.form\\/patient-registration-form.json\"," +
            "\"fields\":[{\n" +
            "        \"key\": \"patient_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please enter patient ID\"\n" +
            "        }\n" + ",\"value\":\"" + PATIENT_ID + "\"" +
            "      }, {\"key\":\"patient_name_label\",\"type\":\"label\",\"text\":\"Name\",\"text_color\":\"#000000\"},{\"key\":\"patient_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\"," +
            "\"openmrs_entity_id\":\"first_name\",\"type\":\"edit_text\",\"edit_type\":\"name\",\"v_required\":{\"value\":\"true\",\"err\":\"Please specify patient name\"}," +
            "\"v_regex\":{\"value\":\"[^([0-9]*)$]*\",\"err\":\"Please enter a valid name\"},\"value\":\"" + PATIENT_NAME + "\"},{\"key\":\"patient_age_label\",\"type\":\"label\",\"text\":\"Age\",\"text_color\":\"#000000\"}," +
            "{\"key\":\"patient_age\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person_attribute\",\"openmrs_entity_id\":\"age\",\"type\":\"edit_text\",\"v_required\":{\"value\":\"true\",\"err\":\"Please specify patient age\"}," +
            "\"v_numeric_integer\":{\"value\":\"true\",\"err\":\"Age must be a rounded number\"},\"step\":\"step1\",\"is-rule-check\":true,\"value\":\"" + PATIENT_AGE + "\"},{\"key\":\"sex\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\"," +
            "\"openmrs_entity_id\":\"gender\",\"type\":\"native_radio\",\"label\":\"Sex\",\"options\":[{\"key\":\"Female\",\"text\":\"Female\"},{\"key\":\"Male\",\"text\":\"Male\"}],\"v_required\":{\"value\":\"true\"," +
            "\"err\":\"Please specify sex\"},\"value\":\"" + PATIENT_GENDER + "\"},{\"key\":\"is_fever\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"native_radio\",\"label\":\"Signs of fever in the last 3 days?\"," +
            "\"options\":[{\"key\":\"Yes\",\"text\":\"Yes\"},{\"key\":\"No\",\"text\":\"No\"}],\"v_required\":{\"value\":\"true\",\"err\":\"Has patient had a fever in last 3 days\"}," +
            "\"step\":\"step1\",\"is-rule-check\":true,\"value\":\"No\"},{\"key\":\"is_malaria\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"native_radio\"," +
            "\"label\":\"Had malaria in the last year?\",\"options\":[{\"key\":\"Yes\",\"text\":\"Yes\"},{\"key\":\"No\",\"text\":\"No\"}],\"v_required\":{\"value\":\"true\",\"err\":\"Has patient had malaria in the last year\"}," +
            "\"value\":\"No\"},{\"key\":\"is_treated\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"native_radio\",\"label\":\"Have been treated for malaria within last 3 months?\"," +
            "\"options\":[{\"key\":\"Yes\",\"text\":\"Yes\"},{\"key\":\"No\",\"text\":\"No\"}],\"v_required\":{\"value\":\"true\",\"err\":\"Has patient been treated for malaria within last 3 months\"},\"value\":\"No\"}," +
            "{\"key\":\"other_symptoms_label\",\"type\":\"label\",\"text\":\"Other symptoms?\",\"text_color\":\"#000000\"},{\"key\":\"other_symptoms\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\"," +
            "\"openmrs_entity_id\":\"\",\"type\":\"edit_text\",\"v_regex\":{\"value\":\"[^([0-9]*)$]*\",\"err\":\"Please enter a valid symptom\"},\"value\":\"\"},{\"key\":\"estimated_dob\",\"openmrs_entity_parent\":\"\"," +
            "\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"birthdate_estimated\",\"type\":\"hidden\",\"value\":\"true\"},{\"key\":\"dob\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\",\"openmrs_entity_id\":\"birthdate\"," +
            "\"type\":\"hidden\",\"value\":\"\"},{\"key\":\"conditional_save\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"conditional_save\",\"type\":\"hidden\"," +
            "\"calculation\":{\"rules-engine\":{\"ex-rules\":{\"rules-file\":\"conditional_save_rules.yml\"}}},\"value\":\"1\"}]}}";

    private static JSONArray formFields;
    private static JSONObject formJsonObj;

    @BeforeClass
    public static void init() throws JSONException {
        formFields = getFormFields(new JSONObject(PATIENT_REGISTRATION_JSON_FORM));
        formJsonObj = new JSONObject(PATIENT_REGISTRATION_JSON_FORM);
    }

    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        interactor = new PatientRegisterFragmentInteractor();
    }

    @Test
    public void testPopulateApproxDOBShouldPopulateCorrectDate() throws Exception {
        Whitebox.invokeMethod(interactor, "populateApproxDOB", formFields);
        for (int i = 0; i < formFields.length(); i++) {
            JSONObject field = formFields.getJSONObject(i);
            if (Constants.FormFields.DOB.equals(field.get(KEY))) {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR) - PATIENT_AGE;
                assertEquals(year + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH), field.get(VALUE));
            }
        }
    }

    public void testLaunchFormShouldPrepopulateFieldsAndLaunchForm() throws JSONException {
        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        Activity activity = mock(Activity.class);
        JSONObject jsonForm = new JSONObject();
        final String FORM_NAME = "form";

        doReturn(jsonForm).when(formUtils).getFormJsonObject(anyString(), any(Activity.class));
        Whitebox.setInternalState(interactor, "formUtils", formUtils);
        interactor.launchForm(activity, FORM_NAME, null);

        verify(formUtils).prePopulateFormFields(eq(jsonForm), isNull(Patient.class), eq(any(new ArrayList<String>().getClass())));
        verify(formUtils).startJsonForm(eq(jsonForm), eq(activity), eq(REQUEST_CODE_GET_JSON));
    }


    @Test
    public void testLaunchFormShouldFetchUniqueIdBeforeFormLaunch() throws JSONException {
        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        Activity activity = mock(Activity.class);
        final String FORM_NAME = "form";
        Patient patient = mock(Patient.class);

        Whitebox.setInternalState(interactor, "formUtils", formUtils);
        interactor.launchForm(activity, FORM_NAME, patient);
        if (isMalariaApp()) {
            verify(formUtils).getNextUniqueIds(formLaunchArgsArgumentCaptor.capture(), eq(interactor), eq(1));
        } else if (isCovidApp()) {
            verify(formUtils).getNextUniqueIds(formLaunchArgsArgumentCaptor.capture(), eq(interactor), eq(2));
        }

        FormLaunchArgs args = formLaunchArgsArgumentCaptor.getValue();
        assertEquals(args.getActivity(), activity);
        assertEquals(args.getPatient(), patient);
    }

    @Test
    public void testSaveEventClientShouldSaveEventAndClient() throws Exception {
        Whitebox.invokeMethod(interactor, "saveEventClient", formJsonObj, PATIENT_REGISTRATION, RDT_PATIENTS);
        verify(eventClientRepository).addorUpdateClient(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
        verify(eventClientRepository).addEvent(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
    }

    @Test
    public void testPopulatePhoneMetadataShouldPopulateCorrectMetadata() throws Exception {
        Event event = new Event();
        Whitebox.invokeMethod(interactor, "populatePhoneMetadata", event);
        for (Obs obs : event.getObs()) {
            assertEquals(obs.getFieldCode(), obs.getValue());
        }
    }

    @Test
    public void testCloseRDTIdShouldCloseRDTId() throws Exception {
        Whitebox.invokeMethod(interactor, "closeIDs", getDBEvent());
        verifyIDsAreClosed();
    }

    @Test
    public void testProcessAndSaveFormShouldProcessClient() throws Exception {
        mockStaticMethods();
        Whitebox.setInternalState(interactor, "clientProcessor", clientProcessor);
        Whitebox.invokeMethod(interactor, "processAndSaveForm", formJsonObj);

        formJsonObj.put(ENCOUNTER_TYPE, RDT_TEST);
        Whitebox.invokeMethod(interactor, "processAndSaveForm", formJsonObj);
        verifyIDsAreClosed();

        formJsonObj.put(ENCOUNTER_TYPE, COVID_RDT_TEST);
        Whitebox.invokeMethod(interactor, "processAndSaveForm", formJsonObj);

        if (isMalariaApp()) {
            verify(uniqueIdRepository, times(1)).close(eq("rdt_id"));
        } else if (isCovidApp()) {
            verify(uniqueIdRepository, times(2)).close(eq("respiratory_sample_id"));
            verify(uniqueIdRepository, times(2)).close(eq("rdt_id"));
        }
        verify(clientProcessor, times(3)).processClient(any(List.class));
    }

    @Test
    public void testGetBindTypeShouldGetCorrectBindType() throws Exception {
        assertEquals(RDT_PATIENTS, Whitebox.invokeMethod(interactor, "getBindType", PATIENT_REGISTRATION));
        assertEquals(RDT_TESTS, Whitebox.invokeMethod(interactor, "getBindType", RDT_TEST));
        assertEquals(PCR_RESULTS, Whitebox.invokeMethod(interactor, "getBindType", PCR_RESULT));
        assertEquals(COVID_PATIENTS, Whitebox.invokeMethod(interactor, "getBindType", COVID_PATIENT_REGISTRATION));
        assertEquals(COVID_RDT_TESTS, Whitebox.invokeMethod(interactor, "getBindType", COVID_RDT_TEST));
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        RDTApplicationPresenter applicationPresenter = mock(RDTApplicationPresenter.class);

        properties = new HashMap();
        properties.put("property1", "property1");
        properties.put("property2", "property2");
        properties.put("property3", "property3");
        doReturn(properties).when(applicationPresenter).getPhoneProperties();

        PowerMockito.when(rdtApplication.getPresenter()).thenReturn(applicationPresenter);

        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
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

        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when( AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(PATIENT_REGISTRATION_JSON_FORM);
    }

    private static JSONArray getFormFields(JSONObject formJsonObj) throws JSONException {
        return formJsonObj.getJSONObject("step1").getJSONArray("fields");
    }

    private void verifyIDsAreClosed() throws Exception {
        if (isMalariaApp()) {
            verify(uniqueIdRepository, times(1)).close(eq("rdt_id"));
        } else if (isCovidApp())  {
            verify(uniqueIdRepository, times(1)).close(eq("rdt_id"));
            verify(uniqueIdRepository, times(1)).close(eq("respiratory_sample_id"));
        }
    }

    private org.smartregister.domain.db.Event getDBEvent() {
        org.smartregister.domain.db.Event dbEvent = mock(org.smartregister.domain.db.Event.class);
        org.smartregister.domain.db.Obs obs = new org.smartregister.domain.db.Obs();
        obs.setValue("rdt_id");
        doReturn(obs).when(dbEvent).findObs(any(), anyBoolean(), eq(RDT_ID));
        obs = new org.smartregister.domain.db.Obs();
        obs.setValue("respiratory_sample_id");
        doReturn(obs).when(dbEvent).findObs(any(), anyBoolean(), eq(RESPIRATORY_SAMPLE_ID));
        return dbEvent;
    }

    private Event getEvent() {
        Event event = new Event();
        event.setObs(new ArrayList<>());
        Obs obs = new Obs();
        obs.setValue("rdt_id");
        obs.setFieldCode("rdt_id");
        event.getObs().add(obs);
        obs = new Obs();
        obs.setValue("respiratory_sample_id");
        obs.setFieldCode("respiratory_sample_id");
        event.getObs().add(obs);
        return event;
    }
}
