package io.ona.rdt.util;

import android.app.Activity;
import android.content.Intent;

import com.google.gson.Gson;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.jsonmapping.Location;
import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.domain.jsonmapping.util.TreeNode;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.activity.CovidJsonFormActivity;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import timber.log.Timber;

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

    public static final Set<String> FACILITY_SET = new HashSet<>(Arrays.asList(CovidConstants.FormFields.FACILITY_NAME,
            CovidConstants.FormFields.HEALTH_FACILITY_NAME, CovidConstants.FormFields.NAME_OF_HEALTH_FACILITY));

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
            fillPatientData(field, uniqueID);
        }

        // pre-populate the patient detail name
        if (CovidConstants.FormFields.PATIENT_INFO_NAME.equals(field.getString(KEY))) {
            fillPatientData(field, patient.getPatientName());
        }

        // pre-populate the patient detail dob
        if (CovidConstants.FormFields.PATIENT_INFO_DOB.equals(field.getString(KEY))) {
            fillPatientData(field, patient.getDob());
        }

        // pre-populate the sampler name
        if (CovidConstants.FormFields.SAMPLER_NAME.equals(field.getString(KEY))) {
            field.put(JsonFormConstants.VALUE, getLoggedInUserPreferredName());
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
        } else if (FACILITY_SET.contains(key)) {
            field.put(JsonFormConstants.OPTIONS_FIELD_NAME, getLocations());
        }
    }

    @Override
    protected Set<String> initializeFormsThatRequireEntityId() {
        return new HashSet<>(Arrays.asList(CovidConstants.Encounter.COVID_PATIENT_REGISTRATION));
    }

    public static void fillPatientData(JSONObject field, String value) throws JSONException {
        if (field == null) {
            return;
        }
        JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        if (options.length() > 0) {
            JSONObject dobObject = options.getJSONObject(0);
            dobObject.put(JsonFormConstants.TEXT, value);
        }
    }

    private void prePopulateSampleShipmentDetailsFormFields(JSONObject field) throws JSONException {
        // pre-populate the sender name
        if (CovidConstants.FormFields.SENDER_NAME.equals(field.getString(KEY))) {
            field.put(JsonFormConstants.VALUE, getLoggedInUserPreferredName());
        }
    }

    private String getLoggedInUserPreferredName() {
        final AllSharedPreferences allSharedPreference = RDTApplication.getInstance().getContext().allSharedPreferences();
        return allSharedPreference.getANMPreferredName(allSharedPreference.fetchRegisteredANM());
    }

    private JSONArray getLocations() throws JSONException {

        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        LocationTree locationTree = new Gson().fromJson(allSharedPreferences.getPreference(CovidConstants.Preference.LOCATION_TREE), LocationTree.class);
        if (locationTree == null) {
            Timber.e("Location tree is null!");
            return new JSONArray();
        }
        LinkedHashMap<String, TreeNode<String, Location>> locationMap = locationTree.getLocationsHierarchy();
        List<String> locations = filterLocations(locationMap);

        JSONArray jsonArray = new JSONArray();
        for (String location : locations) {
            JSONObject option = new JSONObject();
            option.put(JsonFormConstants.KEY, location);
            option.put(JsonFormConstants.TEXT, location);
            option.put(JsonFormConstants.OPENMRS_ENTITY, "");
            option.put(JsonFormConstants.OPENMRS_ENTITY_ID, "");
            jsonArray.put(option);
        }
        return jsonArray;
    }

    private List<String> filterLocations(LinkedHashMap<String, TreeNode<String, Location>> map) {
        List<String> locations = new ArrayList<>();
        Map.Entry<String, TreeNode<String, Location>> entry = map.entrySet().iterator().next();
        for (Map.Entry<String, TreeNode<String, Location>> childEntry : entry.getValue().getChildren().entrySet()) {
            TreeNode<String, Location> childNode = childEntry.getValue();
            locations.add(childNode.getLabel());
        }
        return locations;
    }
}
