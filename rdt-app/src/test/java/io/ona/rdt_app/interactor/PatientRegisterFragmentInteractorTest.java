package io.ona.rdt_app.interactor;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.RDTJsonFormUtils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTApplication.class, ClientProcessorForJava.class})
public class PatientRegisterFragmentInteractorTest {

    @Mock
    RDTApplication rdtApplication;
    @Mock
    private org.smartregister.Context applicationContext;
    @Mock
    private Context context;
    @Mock
    private EventClientRepository eventClientRepository;

    @Mock
    private ClientProcessorForJava clientProcessor;

    private PatientRegisterFragmentInteractor interactorSpy;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getPatientForRDTReturnsValidPatient() throws JSONException {

        String patientName = "Mr. Patient";
        String patientGender = "Male";
        String jsonForm = "{\"count\":\"1\",\"encounter_type\":\"patient_registration\",\"step1\":{\"title\":\"New client record\",\"display_back_button\":\"true\",\"previous_label\":\"SAVE AND EXIT\"," +
                "\"bottom_navigation\":\"true\",\"bottom_navigation_orientation\":\"vertical\",\"next_type\":\"submit\",\"submit_label\":\"SAVE\",\"next_form\":\"json.form\\/patient-registration-form.json\"," +
                "\"fields\":[{\"key\":\"patient_name_label\",\"type\":\"label\",\"text\":\"Name\",\"text_color\":\"#000000\"},{\"key\":\"patient_name\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\"," +
                "\"openmrs_entity_id\":\"first_name\",\"type\":\"edit_text\",\"edit_type\":\"name\",\"v_required\":{\"value\":\"true\",\"err\":\"Please specify patient name\"}," +
                "\"v_regex\":{\"value\":\"[^([0-9]*)$]*\",\"err\":\"Please enter a valid name\"},\"value\":\"" + patientName + "\"},{\"key\":\"patient_age_label\",\"type\":\"label\",\"text\":\"Age\",\"text_color\":\"#000000\"}," +
                "{\"key\":\"patient_age\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person_attribute\",\"openmrs_entity_id\":\"age\",\"type\":\"edit_text\",\"v_required\":{\"value\":\"true\",\"err\":\"Please specify patient age\"}," +
                "\"v_numeric_integer\":{\"value\":\"true\",\"err\":\"Age must be a rounded number\"},\"step\":\"step1\",\"is-rule-check\":true,\"value\":\"3\"},{\"key\":\"sex\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"person\"," +
                "\"openmrs_entity_id\":\"gender\",\"type\":\"native_radio\",\"label\":\"Sex\",\"options\":[{\"key\":\"Female\",\"text\":\"Female\"},{\"key\":\"Male\",\"text\":\"Male\"}],\"v_required\":{\"value\":\"true\"," +
                "\"err\":\"Please specify sex\"},\"value\":\"" + patientGender + "\"},{\"key\":\"is_fever\",\"openmrs_entity_parent\":\"\",\"openmrs_entity\":\"\",\"openmrs_entity_id\":\"\",\"type\":\"native_radio\",\"label\":\"Signs of fever in the last 3 days?\"," +
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

        PowerMockito.mockStatic(RDTApplication.class);
        PowerMockito.mockStatic(ClientProcessorForJava.class);

        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(applicationContext);
        PowerMockito.when(applicationContext.getEventClientRepository()).thenReturn(eventClientRepository);
        PowerMockito.when(ClientProcessorForJava.getInstance(context)).thenReturn(clientProcessor);
        interactorSpy = PowerMockito.spy(new PatientRegisterFragmentInteractor());

        JSONObject jsonFormObject = new JSONObject(jsonForm);
        RDTJsonFormUtils.appendEntityId(jsonFormObject);
        Patient rdtpatient = interactorSpy.getPatientForRDT(jsonFormObject);

        Assert.assertNotNull(rdtpatient);
        Assert.assertEquals(rdtpatient.getPatientName(), patientName);
        Assert.assertEquals(rdtpatient.getPatientSex(), patientGender);
    }

}
