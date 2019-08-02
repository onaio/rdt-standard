package io.ona.rdt_app.interactor;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
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
import org.smartregister.domain.UniqueId;
import org.smartregister.exception.JsonFormMissingStepCountException;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.JsonFormUtils;

import java.util.Calendar;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.FormLaunchArgs;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt_app.util.Constants.REQUEST_CODE_GET_JSON;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class})
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
    @Captor
    private ArgumentCaptor<FormLaunchArgs> formLaunchArgsArgumentCaptor;

    private final String PATIENT_NAME = "Mr. Patient";
    private final String PATIENT_GENDER = "Male";
    private final int PATIENT_AGE = 20;
    private PatientRegisterFragmentInteractor interactor;

    private final String jsonForm = "{\"count\":\"1\",\"encounter_type\":\"patient_registration\",\"step1\":{\"title\":\"New client record\",\"display_back_button\":\"true\",\"previous_label\":\"SAVE AND EXIT\"," +
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

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockStaticMethods();
        interactor = new PatientRegisterFragmentInteractor();
    }

    @Test
    public void getPatientForRDTReturnsValidPatient() throws JSONException {
        JSONObject jsonFormObject = new JSONObject(jsonForm);
        RDTJsonFormUtils.appendEntityId(jsonFormObject);
        Patient rdtPatient = interactor.getPatientForRDT(jsonFormObject);

        Assert.assertNotNull(rdtPatient);
        assertEquals(rdtPatient.getPatientName(), PATIENT_NAME);
        assertEquals(rdtPatient.getPatientSex(), PATIENT_GENDER);
        Assert.assertNotNull(rdtPatient.getBaseEntityId());
    }

    @Test
    public void testPopulateApproxDOB() throws Exception {
        JSONObject formJsonObj = new JSONObject(jsonForm);
        Whitebox.invokeMethod(interactor, "populateApproxDOB", formJsonObj);
        JSONArray fields = JsonFormUtils.fields(formJsonObj);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            if (Constants.DOB.equals(field.get(KEY))) {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR) - PATIENT_AGE;
                assertEquals(year + "-" + today.get(Calendar.MONTH) + "-" + today.get(Calendar.DAY_OF_MONTH), field.get(VALUE));
            }
        }
    }

    @Test
    public void testLaunchForm() throws JSONException {
        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        Activity activity = mock(Activity.class);
        final String FORM_NAME = "form";
        Patient patient = mock(Patient.class);

        Whitebox.setInternalState(interactor, "formUtils", formUtils);
        interactor.launchForm(activity, FORM_NAME, patient);
        verify(formUtils).getNextUniqueId(formLaunchArgsArgumentCaptor.capture(), eq(interactor));

        FormLaunchArgs args = formLaunchArgsArgumentCaptor.getValue();
        assertEquals(args.getActivity(), activity);
        assertEquals(args.getPatient(), patient);
    }

    @Test
    public void testOnUniqueIdFetched() throws JSONException, JsonFormMissingStepCountException {
        FormLaunchArgs args = new FormLaunchArgs();
        Activity activity = mock(Activity.class);
        Patient patient = mock(Patient.class);
        JSONObject jsonForm = new JSONObject();
        args.withActivity(activity)
            .withFormJsonObj(jsonForm)
            .withPatient(patient);

        UniqueId uniqueId = new UniqueId("id", "openmrs_id", null, null, null);

        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        Whitebox.setInternalState(interactor, "formUtils", formUtils);
        interactor.onUniqueIdFetched(args, uniqueId);

        verify(formUtils).prePopulateFormFields(eq(jsonForm), eq(patient), eq("openmrs_id"), eq(7));
        verify(formUtils).startJsonForm(eq(jsonForm), eq(activity), eq(REQUEST_CODE_GET_JSON));
    }

    private void mockStaticMethods() {
        PowerMockito.mockStatic(RDTApplication.class);
        PowerMockito.mockStatic(ClientProcessorForJava.class);

        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(applicationContext);
        PowerMockito.when(applicationContext.getEventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(applicationContext.getUniqueIdRepository()).thenReturn(mock(UniqueIdRepository.class));
        PowerMockito.when(ClientProcessorForJava.getInstance(context)).thenReturn(clientProcessor);
    }
}
