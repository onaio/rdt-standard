package io.ona.rdt.interactor;

import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.FormLauncher;

import static io.ona.rdt.util.Constants.DBConstants.PATIENT_ID;
import static io.ona.rdt.util.Constants.DBConstants.SEX;
import static io.ona.rdt.util.Constants.Encounter.PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.FormFields.CONDITIONAL_SAVE;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.FormFields.ENTITY_ID;
import static io.ona.rdt.util.Constants.FormFields.PATIENT_NAME;
import static io.ona.rdt.util.Constants.FormFields.RESIDENTIAL_ADDRESS;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public class PatientRegisterActivityInteractor extends FormLauncher {

    private PatientRegisterFragmentInteractor patientRegisterFragmentInteractor;

    /**
     * Get the patient for whom the RDT is to be conducted
     *
     * @param jsonFormObject The patient form JSON
     * @return the initialized Patient if proceeding to RDT capture otherwise return null patient
     * @throws JSONException
     */
    public Patient getPatientForRDT(JSONObject jsonFormObject) {
        Patient rdtPatient = null;
        if (PATIENT_REGISTRATION.equals(jsonFormObject.optString(ENCOUNTER_TYPE))) {
            JSONArray formFields = JsonFormUtils.fields(jsonFormObject);
            JSONObject conditionalSave = FormUtils.getFieldJSONObject(formFields, CONDITIONAL_SAVE);
            if (conditionalSave != null && conditionalSave.optInt(VALUE) == 1){
                String name = FormUtils.getFieldJSONObject(formFields, PATIENT_NAME).optString(VALUE);
                String sex = FormUtils.getFieldJSONObject(formFields, SEX).optString(VALUE);
                String address = FormUtils.getFieldJSONObject(formFields, RESIDENTIAL_ADDRESS).optString(RESIDENTIAL_ADDRESS);
                String baseEntityId = getString(jsonFormObject, ENTITY_ID);
                String patientId = FormUtils.getFieldJSONObject(formFields, PATIENT_ID).optString(VALUE);
                rdtPatient = new Patient(name, sex, baseEntityId, patientId, address);
            }
        }
        return rdtPatient;
    }

    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        getPatientRegisterFragmentInteractor().saveForm(jsonForm, callback);
    }


    public PatientRegisterFragmentInteractor getPatientRegisterFragmentInteractor() {
        if (patientRegisterFragmentInteractor == null) {
            patientRegisterFragmentInteractor = new PatientRegisterFragmentInteractor();
        }
        return patientRegisterFragmentInteractor;
    }
}
