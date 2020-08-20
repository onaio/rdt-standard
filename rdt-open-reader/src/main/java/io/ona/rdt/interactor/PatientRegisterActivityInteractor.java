package io.ona.rdt.interactor;

import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.FormLauncherAndSaver;

import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public class PatientRegisterActivityInteractor extends FormLauncherAndSaver {

    /**
     * Get the patient for whom the RDT is to be conducted
     *
     * @param jsonFormObject The patient form JSON
     * @return the initialized Patient if proceeding to RDT capture otherwise return null patient
     * @throws JSONException
     */
    public Patient getPatientForRDT(JSONObject jsonFormObject) {
        Patient rdtPatient = null;
        if (Constants.Encounter.PATIENT_REGISTRATION.equals(jsonFormObject.optString(Constants.FormFields.ENCOUNTER_TYPE))) {
            JSONArray formFields = JsonFormUtils.fields(jsonFormObject);
            JSONObject conditionalSave = FormUtils.getFieldJSONObject(formFields, Constants.FormFields.CONDITIONAL_SAVE);
            if (conditionalSave != null && conditionalSave.optInt(JsonFormUtils.VALUE) == 1) {
                rdtPatient = createPatient(getString(jsonFormObject, Constants.FormFields.ENTITY_ID), formFields);
            }
        }
        return rdtPatient;
    }

    protected Patient createPatient(String baseEntityId, JSONArray formFields) {
        if (baseEntityId == null) {
            return null;
        }
        String name = getStrField(formFields, Constants.FormFields.PATIENT_NAME);
        String sex = getStrField(formFields, Constants.DBConstants.SEX);
        String patientId = getStrField(formFields, Constants.DBConstants.PATIENT_ID);

        Patient patient = new Patient(name, sex, baseEntityId, patientId);

        return isValidPatient(patient) ? patient : null;
    }

    protected boolean isValidPatient(Patient patient) {
        return StringUtils.isNotBlank(patient.getPatientName())
                && StringUtils.isNotBlank(patient.getPatientSex())
                && StringUtils.isNotBlank(patient.getPatientId())
                && StringUtils.isNotBlank(patient.getBaseEntityId());
    }

    protected String getStrField(JSONArray formFields, String fieldName) {
        JSONObject field = FormUtils.getFieldJSONObject(formFields, fieldName);
        return field == null ? null : field.optString(JsonFormUtils.VALUE);
    }
}
