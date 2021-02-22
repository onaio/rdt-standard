package io.ona.rdt.util;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Robolectric;

import java.util.List;

import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.robolectric.RobolectricTest;

import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 16/07/2020
 */

public abstract class BaseRDTJsonFormUtilsTest extends RobolectricTest {

    protected final String UNIQUE_ID = "unique_id";
    protected final int AGE = 20;

    @Test
    public void testPrePopulateFormFieldsShouldPopulateCorrectValues() throws JSONException {
        PatientRegisterActivity patientRegisterActivity = Robolectric.buildActivity(PatientRegisterActivity.class)
                .create()
                .resume()
                .get();

        Patient patient = new Patient("patient", "female", "entity_id", "12345", AGE, "01-09-2020");
        patient.setFemaleTranslatedSex("female");

        int numOfPopulatedFields = 0;

        for (String formToPopulate: getFormsToPrepopulate()) {
            JSONObject formToPopulateJsonObj = getJsonForm(patientRegisterActivity, formToPopulate, patient);
            JSONArray fields = getMultiStepFormFields(formToPopulateJsonObj);
            for (int i = 0; i < fields.length(); i++) {
                JSONObject field = fields.getJSONObject(i);
                numOfPopulatedFields = assertFieldsArePopulated(field, patient, numOfPopulatedFields);
            }
        }

        assertAllFieldsArePopulated(numOfPopulatedFields);
        patientRegisterActivity.finish();
    }

    @Test
    public void testAppendEntityIdShouldAppendCorrectNonEmptyId() throws JSONException {
        JSONObject jsonForm = new JSONObject();
        jsonForm.put(JsonFormConstants.ENCOUNTER_TYPE, getPatientRegistrationEvent());
        String entityId = getFormUtils().appendEntityId(jsonForm);
        Assert.assertFalse(StringUtils.isBlank(entityId));
        Assert.assertEquals(entityId, jsonForm.get(Constants.FormFields.ENTITY_ID));
    }

    private JSONObject getJsonForm(PatientRegisterActivity patientRegisterActivity, String formName, Patient patient) throws JSONException {
        return getFormUtils().launchForm(patientRegisterActivity, formName, patient, UNIQUE_ID);
    }

    protected abstract void assertAllFieldsArePopulated(int numOfPopulatedFields);

    protected abstract int assertFieldsArePopulated(JSONObject field, Patient patient, int numOfPopulatedFields) throws JSONException;

    protected abstract RDTJsonFormUtils getFormUtils();

    protected abstract List<String> getFormsToPrepopulate();

    protected abstract String getMockForm();

    protected abstract String getPatientRegistrationEvent();
}
