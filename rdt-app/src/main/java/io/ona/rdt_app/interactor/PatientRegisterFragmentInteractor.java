package io.ona.rdt_app.interactor;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vijay.jsonwizard.utils.FormUtils;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.LocationProperty;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.exception.JsonFormMissingStepCountException;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.DateTimeTypeConverter;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PropertiesConverter;

import java.util.Calendar;
import java.util.Collections;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.model.Patient;

import static io.ona.rdt_app.util.Constants.CONDITIONAL_SAVE;
import static io.ona.rdt_app.util.Constants.DETAILS;
import static io.ona.rdt_app.util.Constants.DOB;
import static io.ona.rdt_app.util.Constants.ENCOUNTER_TYPE;
import static io.ona.rdt_app.util.Constants.ENTITY_ID;
import static io.ona.rdt_app.util.Constants.METADATA;
import static io.ona.rdt_app.util.Constants.PATIENTS;
import static io.ona.rdt_app.util.Constants.PATIENT_AGE;
import static io.ona.rdt_app.util.Constants.PATIENT_NAME;
import static io.ona.rdt_app.util.Constants.PATIENT_REGISTRATION;
import static io.ona.rdt_app.util.Constants.RDT_TESTS;
import static io.ona.rdt_app.util.Constants.SEX;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public class PatientRegisterFragmentInteractor {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
            .registerTypeAdapter(LocationProperty.class, new PropertiesConverter()).create();
    private final String TAG = PatientRegisterFragmentInteractor.class.getName();
    private EventClientRepository eventClientRepository;
    private ClientProcessorForJava clientProcessor;

    public PatientRegisterFragmentInteractor() {
        eventClientRepository = RDTApplication.getInstance().getContext().getEventClientRepository();
        clientProcessor = ClientProcessorForJava.getInstance(RDTApplication.getInstance().getApplicationContext());
    }

    public void saveForm(final JSONObject jsonForm, final OnFormSavedCallback onFormSavedCallback) {

        class SaveFormTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final String encounterType = jsonForm.getString(ENCOUNTER_TYPE);
                    populateApproxDOB(jsonForm);

                    String bindType = PATIENT_REGISTRATION.equals(encounterType) ? PATIENTS : RDT_TESTS;
                    EventClient eventClient = saveEventClient(jsonForm, encounterType, bindType);
                    clientProcessor.processClient(Collections.singletonList(eventClient));
                } catch (Exception e) {
                    Log.e(TAG, "Error saving event", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                onFormSavedCallback.onFormSaved();
            }
        }
        new SaveFormTask().execute();
    }

    private void populateApproxDOB(JSONObject jsonForm) throws JSONException {
        JSONArray fields = JsonFormUtils.fields(jsonForm);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            int age = 0;
            if (PATIENT_AGE.equals(field.get(KEY))) {
                age = field.getInt(VALUE);
            }
            if (DOB.equals(field.get(KEY))) {
                long currentTime = System.currentTimeMillis();
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(currentTime);
                int birthYear = calendar.get(Calendar.YEAR) - age;
                String date = birthYear + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                field.put(VALUE, date);
            }
        }
    }

    private EventClient saveEventClient(JSONObject jsonForm, String encounterType, String bindType) throws JSONException, JsonFormMissingStepCountException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        JSONObject metadata = getJSONObject(jsonForm, METADATA);
        String entityId = getString(jsonForm, ENTITY_ID);

        FormTag formTag = new FormTag();
        formTag.providerId = "";
        formTag.locationId = "";
        formTag.teamId = "";
        formTag.team = "";

        Client client = JsonFormUtils.createBaseClient(fields, formTag, entityId);
        JSONObject clientJson = new JSONObject(gson.toJson(client));
        org.smartregister.domain.db.Client dbClient = gson.fromJson(clientJson.toString(), org.smartregister.domain.db.Client.class);
        if (PATIENT_REGISTRATION.equals(encounterType)) {
            eventClientRepository.addorUpdateClient(entityId, clientJson);
        }

        String providerId = RDTApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM();
        Event event = JsonFormUtils.createEvent(fields, metadata, formTag, entityId, encounterType, bindType);
        event.setProviderId(providerId);

        JSONObject eventJson = new JSONObject(gson.toJson(event));
        eventJson.put(DETAILS, getJSONObject(jsonForm, DETAILS));
        eventClientRepository.addEvent(entityId, eventJson);
        org.smartregister.domain.db.Event dbEvent = gson.fromJson(eventJson.toString(), org.smartregister.domain.db.Event.class);

        return new EventClient(dbEvent, dbClient);
    }

    /**
     * Get the patient for whom the RDT is to be conducted
     *
     * @param jsonFormObject The patient form JSON
     * @return the initialized Patient if proceeding to RDT capture otherwise return null patient
     * @throws JSONException
     */
    public Patient getPatientForRDT(JSONObject jsonFormObject) throws JSONException {
        Patient rdtPatient = null;
        if (PATIENT_REGISTRATION.equals(jsonFormObject.optString(ENCOUNTER_TYPE))) {
            JSONArray formFields = JsonFormUtils.fields(jsonFormObject);
            JSONObject fieldJsonObject;
            for (int i = 0; i < formFields.length(); i++) {
                fieldJsonObject = formFields.getJSONObject(i);
                if (CONDITIONAL_SAVE.equals(fieldJsonObject.optString(KEY)) &&
                        Integer.parseInt(fieldJsonObject.optString(VALUE)) == 1) {
                    String name = FormUtils.getFieldJSONObject(formFields, PATIENT_NAME).optString(VALUE);
                    String sex = FormUtils.getFieldJSONObject(formFields, SEX).optString(VALUE);
                    String baseEntityId = getString(jsonFormObject, ENTITY_ID).split("-")[0];
                    rdtPatient = new Patient(name, sex, baseEntityId);
                }
            }
        }
        return rdtPatient;
    }

}
