package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.apache.commons.lang3.StringUtils;
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
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidJsonFormActivity;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.widget.validator.CovidImageViewFactory;
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
                JSONArray availableRDTsArr = Utils.createOptionsBlock(appendOtherOption(deviceDefinitionProcessor.getDeviceIDToDeviceNameMap()), "", "", "");
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

    public static void fillPatientData(JSONObject patientInfoField, String value) throws JSONException {
        if (patientInfoField == null) {
            return;
        }
        JSONArray options = patientInfoField.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
        if (options.length() > 0) {
            JSONObject patientInfo = options.getJSONObject(0);
            patientInfo.put(JsonFormConstants.TEXT, value);
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
            return jsonArray;
        }

        LinkedHashMap<String, TreeNode<String, Location>> locationMap = locationTree.getLocationsHierarchy();
        return Utils.createOptionsBlock(appendOtherOption(filterLocations(locationMap)), "", "", "");
    }

    private Map<String, String> filterLocations(LinkedHashMap<String, TreeNode<String, Location>> map) {
        LinkedHashMap<String, String> locations = new LinkedHashMap<>();
        Map.Entry<String, TreeNode<String, Location>> entry = map.entrySet().iterator().next();
        for (Map.Entry<String, TreeNode<String, Location>> childEntry : entry.getValue().getChildren().entrySet()) {
            TreeNode<String, Location> childNode = childEntry.getValue();
            locations.put(childNode.getLabel(), childNode.getLabel());
        }
        return locations;
    }

    public void populateRDTDetailsConfirmationPage(WidgetArgs widgetArgs, String deviceId) throws JSONException, IOException, FHIRParserException {
        Context context = widgetArgs.getContext();
        if (StringUtils.isBlank(deviceId) || CovidConstants.FormFields.OTHER_KEY.equals(deviceId)) {
            // reset details when other is selected or device id is blank
            writeRDTDetailsToWidgets(widgetArgs, context.getString(R.string.unknown_rdt_selected), "", "");
        } else {
            DeviceDefinitionProcessor deviceDefinitionProcessor = DeviceDefinitionProcessor.getInstance(context);
            String deviceDetails = getFormattedRDTDetails(widgetArgs.getContext(), deviceDefinitionProcessor.extractManufacturerName(deviceId),
                    deviceDefinitionProcessor.extractDeviceName(deviceId));
            JSONObject deviceConfig = deviceDefinitionProcessor.extractDeviceConfig(deviceId);
            writeRDTDetailsToWidgets(widgetArgs, deviceDetails, deviceConfig.optString(CovidConstants.FHIRResource.REF_IMG), deviceConfig.toString());
        }
    }

    private void writeRDTDetailsToWidgets(WidgetArgs widgetArgs, String deviceDetails, String rdtImage, String deviceConfig) throws JSONException {
        Context context = widgetArgs.getContext();
        String rdtDetailsConfirmationPage = getStepStateConfigObj().optString(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        JSONObject deviceDetailsWidget = RDTJsonFormUtils.getField(rdtDetailsConfirmationPage,
                CovidConstants.FormFields.SELECTED_RDT_IMAGE, context);

        // write device details to confirmation page
        deviceDetailsWidget.put(JsonFormConstants.TEXT, deviceDetails);
        deviceDetailsWidget.put(CovidImageViewFactory.BASE64_ENCODED_IMG, rdtImage);

        // save extracted device config
        ((JsonApi) context).writeValue(rdtDetailsConfirmationPage, CovidConstants.FormFields.RDT_CONFIG,
                deviceConfig, "", "", "", false);
    }

    private String getFormattedRDTDetails(Context context, String manufacturer, String deviceName) {
        final String htmlLineBreak = "<br>";
        final String doubleHtmlLineBreak = "<br><br>";

        String formattedMftStr = StringUtils.join(new String[]{context.getString(R.string.manufacturer_name), manufacturer}, htmlLineBreak);
        String formattedDeviceNameStr = StringUtils.join(new String[]{context.getString(R.string.rdt_name), deviceName}, htmlLineBreak);

        return StringUtils.join(new String[]{formattedMftStr, formattedDeviceNameStr}, doubleHtmlLineBreak);
    }
}
