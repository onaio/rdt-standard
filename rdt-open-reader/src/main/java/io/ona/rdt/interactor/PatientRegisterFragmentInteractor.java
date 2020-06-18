package io.ona.rdt.interactor;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Client;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.domain.LocationProperty;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.DateTimeTypeConverter;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PropertiesConverter;

import java.util.Calendar;
import java.util.Collections;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.util.FormLauncher;
import timber.log.Timber;

import static com.vijay.jsonwizard.utils.Utils.showToast;
import static io.ona.rdt.util.Constants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.Constants.Encounter.PATIENT_DIAGNOSTICS;
import static io.ona.rdt.util.Constants.Encounter.PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Encounter.PCR_RESULT;
import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.Constants.Encounter.SAMPLE_DELIVERY_DETAILS;
import static io.ona.rdt.util.Constants.Encounter.SUPPORT_INVESTIGATION;
import static io.ona.rdt.util.Constants.FormFields.DETAILS;
import static io.ona.rdt.util.Constants.FormFields.DOB;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.FormFields.ENTITY_ID;
import static io.ona.rdt.util.Constants.FormFields.METADATA;
import static io.ona.rdt.util.Constants.FormFields.PATIENT_AGE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.FormFields.RESPIRATORY_SAMPLE_ID;
import static io.ona.rdt.util.Constants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.Constants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.Constants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static io.ona.rdt.util.Constants.Table.RDT_TESTS;
import static io.ona.rdt.util.Constants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.Constants.Table.SAMPLE_DELIVERY_RECORDS;
import static io.ona.rdt.util.Constants.Table.SUPPORT_INVESTIGATIONS;
import static io.ona.rdt.util.RDTJsonFormUtils.getFormTag;
import static io.ona.rdt.util.Utils.isCovidApp;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public class PatientRegisterFragmentInteractor extends FormLauncher {

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
            .registerTypeAdapter(LocationProperty.class, new PropertiesConverter()).create();

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
                    processAndSaveForm(jsonForm);
                } catch (Exception e) {
                    Timber.e(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                Context context = RDTApplication.getInstance();
                showToast(context, context.getString(R.string.form_successfully_saved));
                if (onFormSavedCallback != null) {
                    onFormSavedCallback.onFormSaved();
                }
            }
        }
        new SaveFormTask().execute();
    }

    private void processAndSaveForm(JSONObject jsonForm) throws Exception {
        populateApproxDOB(JsonFormUtils.fields(jsonForm));
        final String encounterType = jsonForm.getString(ENCOUNTER_TYPE);
        String bindType = getBindType(encounterType);
        EventClient eventClient = saveEventClient(jsonForm, encounterType, bindType);
        if (RDT_TESTS.equals(bindType) || COVID_RDT_TESTS.equals(bindType)) {
            closeIDs(eventClient.getEvent());
        }
        clientProcessor.processClient(Collections.singletonList(eventClient));
    }


    private String getBindType(String encounterType) {
        String bindType = "";
        switch (encounterType) {
            case PATIENT_REGISTRATION:
                bindType = RDT_PATIENTS;
                break;
            case RDT_TEST:
                bindType = RDT_TESTS;
                break;
            case PCR_RESULT:
                bindType = PCR_RESULTS;
                break;
            case COVID_PATIENT_REGISTRATION:
                bindType = COVID_PATIENTS;
                break;
            case COVID_RDT_TEST:
                bindType = COVID_RDT_TESTS;
                break;
            case PATIENT_DIAGNOSTICS:
                bindType = PATIENT_DIAGNOSTIC_RESULTS;
                break;
            case SAMPLE_COLLECTION:
                bindType = SAMPLE_COLLECTIONS;
                break;
            case SAMPLE_DELIVERY_DETAILS:
                bindType = SAMPLE_DELIVERY_RECORDS;
                break;
            case SUPPORT_INVESTIGATION:
                bindType = SUPPORT_INVESTIGATIONS;
                break;
        }
        return bindType;
    }

    private void populateApproxDOB(JSONArray fields) throws JSONException {
        int age = 0;
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            if (PATIENT_AGE.equals(field.get(KEY))) {
                age = field.getInt(VALUE);
            }
            if (DOB.equals(field.get(KEY))) {
                Calendar calendar = Calendar.getInstance();
                int birthYear = calendar.get(Calendar.YEAR) - age;
                String date = birthYear + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
                field.put(VALUE, date);
            }
        }
    }

    private void closeIDs(org.smartregister.domain.db.Event dbEvent) {
        // close rdt id
        org.smartregister.domain.db.Obs idObs = dbEvent.findObs(null, false, RDT_ID);
        if (idObs != null) {
            String rdtId = idObs.getValue() == null ? "" : idObs.getValue().toString();
            RDTApplication.getInstance().getContext().getUniqueIdRepository().close(rdtId);
        }
        // close respiratory sample id
        if (isCovidApp()) {
            idObs = dbEvent.findObs(null, false, RESPIRATORY_SAMPLE_ID);
            if (idObs != null) {
                String rdtId = idObs.getValue() == null ? "" : idObs.getValue().toString();
                RDTApplication.getInstance().getContext().getUniqueIdRepository().close(rdtId);
            }
        }
    }

    private EventClient saveEventClient(JSONObject jsonForm, String encounterType, String bindType) throws JSONException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        JSONObject metadata = getJSONObject(jsonForm, METADATA);
        String entityId = getString(jsonForm, ENTITY_ID);
        FormTag formTag = getFormTag();

        Client client = JsonFormUtils.createBaseClient(fields, formTag, entityId);
        JSONObject clientJson = new JSONObject(gson.toJson(client));
        org.smartregister.domain.db.Client dbClient = gson.fromJson(clientJson.toString(), org.smartregister.domain.db.Client.class);
        if (PATIENT_REGISTRATION.equals(encounterType) || COVID_PATIENT_REGISTRATION.equals(encounterType)) {
            eventClientRepository.addorUpdateClient(entityId, clientJson);
        }

        Event event = JsonFormUtils.createEvent(fields, metadata, formTag, entityId, encounterType, bindType);
        populatePhoneMetadata(event);

        JSONObject eventJson = new JSONObject(gson.toJson(event));
        eventJson.put(DETAILS, getJSONObject(jsonForm, DETAILS));
        eventClientRepository.addEvent(entityId, eventJson);
        org.smartregister.domain.db.Event dbEvent = gson.fromJson(eventJson.toString(), org.smartregister.domain.db.Event.class);

        return new EventClient(dbEvent, dbClient);
    }

    private void populatePhoneMetadata(Event event) {
        for (Map.Entry<String, String> phoneProperty : RDTApplication.getInstance().getPresenter().getPhoneProperties().entrySet()) {
            Obs obs = new Obs();
            obs.setFieldCode(phoneProperty.getKey());
            obs.setValue(phoneProperty.getValue());
            obs.setFormSubmissionField(phoneProperty.getKey());
            event.addObs(obs);
        }
    }
}
