package io.ona.rdt_app.interactor;

import com.vijay.jsonwizard.utils.FormUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.FormLauncher;

import static io.ona.rdt_app.util.Constants.CONDITIONAL_SAVE;
import static io.ona.rdt_app.util.Constants.ENCOUNTER_TYPE;
import static io.ona.rdt_app.util.Constants.ENTITY_ID;
import static io.ona.rdt_app.util.Constants.PATIENT_NAME;
import static io.ona.rdt_app.util.Constants.PATIENT_REGISTRATION;
import static io.ona.rdt_app.util.Constants.SEX;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public class PatientRegisterActivityInteractor extends FormLauncher {
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
                String baseEntityId = getString(jsonFormObject, ENTITY_ID).split("-")[0];
                rdtPatient = new Patient(name, sex, baseEntityId);
            }
        }
        return rdtPatient;
    }

    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        new PatientRegisterFragmentInteractor().saveForm(jsonForm, callback);
    }
}
