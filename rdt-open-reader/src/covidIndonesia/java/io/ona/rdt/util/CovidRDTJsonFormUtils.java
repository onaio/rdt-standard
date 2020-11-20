package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.jsonmapping.Location;
import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.domain.jsonmapping.util.TreeNode;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.R;
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
    public void prePopulateFormFields(Context context, JSONObject jsonForm, Patient patient, String uniqueID) throws JSONException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            switch (jsonForm.getString(ENCOUNTER_TYPE)) {
                case COVID_RDT_TEST:
                    prePopulateRDTFormFields(context, field, uniqueID);
                    break;
                case SAMPLE_COLLECTION:
                    prePopulateSampleCollectionFormFields(context, field, uniqueID, patient);
                    break;
                case CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS:
                    prePopulateSampleShipmentDetailsFormFields(field);
                    break;
            }
            prePopulateRDTPatientFields(patient, field);
        }
    }

    @Override
    protected void prePopulateRDTFormFields(Context context, JSONObject field, String uniqueID) throws JSONException {
        super.prePopulateRDTFormFields(context, field, uniqueID);
        // pre-populate available RDTs
        if (Constants.RDTType.RDT_TYPE.equals(field.getString(JsonFormUtils.KEY))) {
            try {
                DeviceDefinitionProcessor deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(context);
                JSONArray availableRDTsArr = Utils.createOptionsBlock(appendOtherOption(deviceDefinitionProcessor.getDeviceIDToDeviceNameMap()), "", "");
                field.put(JsonFormConstants.OPTIONS_FIELD_NAME, availableRDTsArr);
            } catch (IOException | FHIRParserException e) {
                Timber.e(e);
            }
        }
    }

    public static Map<String, String> appendOtherOption(Map<String, String> optionsMap) {
        optionsMap.put(CovidConstants.FormFields.OTHER_KEY, CovidConstants.FormFields.OTHER_VALUE);
        return optionsMap;
    }

    private void prePopulateSampleCollectionFormFields(Context context, JSONObject field, String uniqueID, Patient patient) throws JSONException {
        switch (field.getString(KEY)) {
            case LBL_RESPIRATORY_SAMPLE_ID:
                // pre-populate respiratory sample id labels
                field.put(JsonFormConstants.TEXT, context.getString(R.string.sample_id_prompt) + uniqueID);
                break;
            case COVID_SAMPLE_ID:
                // pre-populate respiratory sample id field
                field.put(JsonFormConstants.VALUE, uniqueID);
                break;
            case CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID:
                // pre-populate the patient detail unique id
                fillPatientData(field, uniqueID);
                break;
            case CovidConstants.FormFields.PATIENT_INFO_NAME:
                // pre-populate the patient detail name
                fillPatientData(field, patient.getPatientName());
                break;
            case CovidConstants.FormFields.PATIENT_INFO_DOB:
                // pre-populate the patient detail dob
                fillPatientData(field, patient.getDob());
                break;
            case CovidConstants.FormFields.SAMPLER_NAME:
                // pre-populate the sampler name
                field.put(JsonFormConstants.VALUE, getLoggedInUserPreferredName());
                break;
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

        JSONArray jsonArray = new JSONArray();
        if (locationTree == null) {
            Timber.e("Location tree is null!");
            return jsonArray;
        }

        LinkedHashMap<String, TreeNode<String, Location>> locationMap = locationTree.getLocationsHierarchy();
        List<String> locations = filterLocations(locationMap);
        for (String location : locations) {
            jsonArray.put(createOption(location, location));
        }
        jsonArray.put(createOption("other", "Other"));
        return jsonArray;
    }

    private JSONObject createOption(String key, String value) throws JSONException {
        JSONObject option = new JSONObject();
        option.put(JsonFormConstants.KEY, key);
        option.put(JsonFormConstants.TEXT, value);
        option.put(JsonFormConstants.OPENMRS_ENTITY, "");
        option.put(JsonFormConstants.OPENMRS_ENTITY_ID, "");
        return option;
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
