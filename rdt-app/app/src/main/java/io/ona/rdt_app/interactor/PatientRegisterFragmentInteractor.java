package io.ona.rdt_app.interactor;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.LocationProperty;
import org.smartregister.domain.db.Client;
import org.smartregister.domain.db.EventClient;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.util.DateTimeTypeConverter;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PropertiesConverter;

import java.util.Collections;
import java.util.UUID;

import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.callback.OnFormSavedCallback;

import static io.ona.rdt_app.util.Constants.DETAILS;
import static io.ona.rdt_app.util.Constants.ENCOUNTER_TYPE;
import static io.ona.rdt_app.util.Constants.METADATA;
import static io.ona.rdt_app.util.Constants.PATIENTS;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public class PatientRegisterFragmentInteractor {

    private EventClientRepository eventClientRepository;
    private ClientProcessorForJava clientProcessor;

    private static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
            .registerTypeAdapter(LocationProperty.class, new PropertiesConverter()).create();

    private final String TAG = PatientRegisterFragmentInteractor.class.getName();

    public PatientRegisterFragmentInteractor() {
        eventClientRepository = RDTApplication.getInstance().getContext().getEventClientRepository();
        clientProcessor = ClientProcessorForJava.getInstance(RDTApplication.getInstance().getApplicationContext());
    }


    public void saveRegistrationForm(final JSONObject jsonForm, final OnFormSavedCallback onFormSavedCallback) {

        class SaveFormTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final String encounterType = jsonForm.getString(ENCOUNTER_TYPE);
                    org.smartregister.domain.db.Event event = saveEvent(jsonForm, encounterType, PATIENTS);
                    String baseEntityId = UUID.randomUUID().toString();
                    event.setBaseEntityId(baseEntityId);
                    clientProcessor.processClient(Collections.singletonList(new EventClient(event, new Client(baseEntityId))));
                } catch (Exception e) {
                    Log.e(TAG, "Error saving patient registration event", e);
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

    private org.smartregister.domain.db.Event saveEvent(JSONObject jsonForm, String encounterType, String bindType) throws JSONException {
        String entityId = getString(jsonForm, ENTITY_ID);
        JSONArray fields = JsonFormUtils.fields(jsonForm);
        JSONObject metadata = getJSONObject(jsonForm, METADATA);

        FormTag formTag = new FormTag();
        formTag.providerId = "";
        formTag.locationId = "";
        formTag.teamId = "";
        formTag.team = "";
        Event event = JsonFormUtils.createEvent(fields, metadata, formTag, entityId, encounterType, bindType);
        JSONObject eventJson = new JSONObject(gson.toJson(event));
        eventJson.put(DETAILS, getJSONObject(jsonForm, DETAILS));
        eventClientRepository.addEvent(entityId, eventJson);

        return gson.fromJson(eventJson.toString(), org.smartregister.domain.db.Event.class);
    }
}
