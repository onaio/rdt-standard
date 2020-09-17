package io.ona.rdt.util;

import android.app.Activity;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.ona.rdt.activity.CovidJsonFormActivity;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ENCOUNTER_TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.FormFields.PATIENT_AGE;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.LBL_RESPIRATORY_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.PATIENT_SEX;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidRDTJsonFormUtils extends RDTJsonFormUtils {

    public static void launchPatientProfile(Patient patient, WeakReference<Activity> activity) {
        Intent intent = new Intent(activity.get(), CovidPatientProfileActivity.class);
        intent.putExtra(PATIENT, patient);
        activity.get().startActivity(intent, null);
    }

    @Override
    public void prePopulateFormFields(JSONObject jsonForm, Patient patient, String uniqueID) throws JSONException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            switch (jsonForm.getString(ENCOUNTER_TYPE)) {
                case COVID_RDT_TEST:
                    prePopulateRDTFormFields(field, uniqueID);
                    break;
                case SAMPLE_COLLECTION:
                    prePopulateSampleCollectionFormFields(field, uniqueID, patient);
                    break;
                case CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS:
                    prePopulateSampleShipmentDetailsFormFields(field);
                    break;
            }
            prePopulateRDTPatientFields(patient, field);
        }
    }

    private void prePopulateSampleCollectionFormFields(JSONObject field, String uniqueID, Patient patient) throws JSONException {
        // pre-populate respiratory sample id labels
        if (LBL_RESPIRATORY_SAMPLE_ID.equals(field.getString(KEY))) {
            field.put("text", "Sample ID: " + uniqueID);
        }
        // pre-populate respiratory sample id field
        if (COVID_SAMPLE_ID.equals(field.getString(KEY))) {
            field.put(JsonFormConstants.VALUE, uniqueID);
        }

        // pre-populate the patient detail unique id
        if (CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID.equals(field.getString(KEY))) {
            fillPatientData(field, "Unique Id: ", uniqueID);
        }

        // pre-populate the patient detail name
        if (CovidConstants.FormFields.PATIENT_INFO_NAME.equals(field.getString(KEY))) {
            fillPatientData(field, "Patient Name: ", patient.getPatientName());
        }

        // pre-populate the patient detail dob
        if (CovidConstants.FormFields.PATIENT_INFO_DOB.equals(field.getString(KEY))) {
            fillPatientData(field, "Patient Dob: ", patient.getDob());
        }

        // pre-populate the sampler name
        if (CovidConstants.FormFields.SAMPLER_NAME.equals(field.getString(KEY))) {
            field.put(JsonFormConstants.VALUE, getNameOfSampler());
        }
    }

    @Override
    protected Set<String> initializeFormsThatShouldBePrepopulated() {
        return new HashSet<>(Arrays.asList(SAMPLE_COLLECTION_FORM, COVID_RDT_TEST_FORM,
                PATIENT_DIAGNOSTICS_FORM, CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM));
    }

    @Override
    protected Class getJsonFormActivityClass() {
        return CovidJsonFormActivity.class;
    }

    @Override
    protected void prePopulateRDTPatientFields(Patient patient, JSONObject field) throws JSONException {
        super.prePopulateRDTPatientFields(patient, field);
        String key = field.getString(JsonFormUtils.KEY);
        if (PATIENT_SEX.equals(key)) {
            field.put(JsonFormUtils.VALUE, patient.getPatientSex().toLowerCase());
        } else if (PATIENT_AGE.equals(key)) {
            field.put(JsonFormUtils.VALUE, patient.getAge());
        }
    }

    @Override
    protected Set<String> initializeFormsThatRequireEntityId() {
        return new HashSet<>(Arrays.asList(CovidConstants.Encounter.COVID_PATIENT_REGISTRATION));
    }

    private void fillPatientData(JSONObject field, String prefix, String value) throws JSONException {
        JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        if (options.length() > 0) {
            JSONObject dobObject = options.getJSONObject(0);
            dobObject.put(JsonFormConstants.TEXT, prefix + value);
        }
    }

    private void prePopulateSampleShipmentDetailsFormFields(JSONObject field) throws JSONException {

        // pre-populate the sender name
        if (CovidConstants.FormFields.SENDER_NAME.equals(field.getString(KEY))) {
            field.put(JsonFormConstants.VALUE, getNameOfSampler());
        }
    }

    private String getNameOfSampler() {
        final AllSharedPreferences allSharedPreference = RDTApplication.getInstance().getContext().allSharedPreferences();
        return allSharedPreference.getANMPreferredName(allSharedPreference.fetchRegisteredANM());
    }
}
