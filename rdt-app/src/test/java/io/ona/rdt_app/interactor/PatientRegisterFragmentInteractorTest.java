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
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.exception.JsonFormMissingStepCountException;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.AssetHandler;
import org.smartregister.util.JsonFormUtils;

import java.util.Calendar;
import java.util.UUID;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.Constants;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt_app.util.Constants.PATIENTS;
import static io.ona.rdt_app.util.Constants.PATIENT_REGISTRATION;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class, JsonFormUtils.class, AssetHandler.class})
public class PatientRegisterFragmentInteractorTest {

    @Mock
    private RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context applicationContext;
    @Mock
    private Context context;
    @Mock
    private EventClientRepository eventClientRepository;
    @Mock
    private ClientProcessorForJava clientProcessor;

    private static final String PATIENT_NAME = "Mr. Patient";
    private static final String PATIENT_GENDER = "Male";
    private static final String PATIENT_BASE_ENTITY_ID = "2b66831d";
    private static final int PATIENT_AGE = 20;

    public static final Patient expectedPatient = new Patient(PATIENT_NAME, PATIENT_GENDER, PATIENT_BASE_ENTITY_ID);

    private PatientRegisterFragmentInteractor interactor;

    public static final String JSON_FORM = "{\"count\":\"1\",\"entity_id\": \"" + PATIENT_BASE_ENTITY_ID + "\",\"encounter_type\":\"patient_registration\", \"metadata\": {},\"step1\":{\"title\":\"New client record\",\"display_back_button\":\"true\",\"previous_label\":\"SAVE AND EXIT\"," +
            "\"bottom_navigation\":\"true\",\"bottom_navigation_orientation\":\"vertical\",\"next_type\":\"submit\",\"submit_label\":\"SAVE\",\"next_form\":\"json.form\\/patient-registration-form.json\"," +
            "\"fields\":[{\"key\":\"patient_name_label\",\"type\":\"label\",\"text\":\"Name\",\"text_color\":\"#000000\"},{\"key\":\"patient_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\"," +
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
        formFields = getFormFields(new JSONObject(JSON_FORM));
        formJsonObj = new JSONObject(JSON_FORM);
    }

    @Before
    public void setUp() throws JsonFormMissingStepCountException, JSONException {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        interactor = new PatientRegisterFragmentInteractor();
    }

    @Test
    public void testPopulateApproxDOBShouldPopulateCorrectDate() throws Exception {
        Whitebox.invokeMethod(interactor, "populateApproxDOB", formFields);
        for (int i = 0; i < formFields.length(); i++) {
            JSONObject field = formFields.getJSONObject(i);
            if (Constants.DOB.equals(field.get(KEY))) {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR) - PATIENT_AGE;
                assertEquals(year + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH), field.get(VALUE));
            }
        }
    }

    @Test
    public void testSaveEventClientShouldSaveEventAndClient() throws Exception {
        Whitebox.invokeMethod(interactor, "saveEventClient", formJsonObj, PATIENT_REGISTRATION, PATIENTS);
        verify(eventClientRepository).addorUpdateClient(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
        verify(eventClientRepository).addEvent(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
    }

    private void mockStaticMethods() throws JsonFormMissingStepCountException, JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(applicationContext);

        // mock repositories
        PowerMockito.when(applicationContext.getEventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(applicationContext.getUniqueIdRepository()).thenReturn(mock(UniqueIdRepository.class));
        PowerMockito.when(applicationContext.allSharedPreferences()).thenReturn(mock(AllSharedPreferences.class));

        // mock client processor
        mockStatic(ClientProcessorForJava.class);
        PowerMockito.when(ClientProcessorForJava.getInstance(context)).thenReturn(clientProcessor);

        // mock JsonFormUtils
        mockStatic(JsonFormUtils.class);
        when(JsonFormUtils.getMultiStepFormFields(any(JSONObject.class))).thenReturn(new JSONArray());
        when(JsonFormUtils.getJSONObject(any(JSONObject.class), anyString())).thenReturn(new JSONObject());
        when(JsonFormUtils.getString(any(JSONObject.class), anyString())).thenReturn(PATIENT_BASE_ENTITY_ID);
        when(JsonFormUtils.createBaseClient(any(JSONArray.class), any(FormTag.class), anyString())).thenReturn(new Client(UUID.randomUUID().toString()));
        when(JsonFormUtils.createEvent(any(JSONArray.class), any(JSONObject.class), any(FormTag.class), anyString(), anyString(), anyString())).thenReturn(new Event());
        when(JsonFormUtils.fields(any(JSONObject.class))).thenReturn(formFields);

        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when( AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(JSON_FORM);
    }

    private static JSONArray getFormFields(JSONObject formJsonObj) throws JSONException {
        return formJsonObj.getJSONObject("step1").getJSONArray("fields");
    }
}
