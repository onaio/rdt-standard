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
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessor;
import org.smartregister.util.DateTimeTypeConverter;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.PropertiesConverter;

import java.util.Collections;

import io.ona.rdt_app.application.RDTApplication;

import static io.ona.rdt_app.util.Constants.DETAILS;
import static io.ona.rdt_app.util.Constants.ENCOUNTER_TYPE;
import static io.ona.rdt_app.util.Constants.METADATA;
import static io.ona.rdt_app.util.Constants.PATIENT;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.getJSONObject;
import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public class PatientRegisterFragmentInteractor {

    private EventClientRepository eventClientRepository;
    private ClientProcessor clientProcessor;

    public static final Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .registerTypeAdapter(DateTime.class, new DateTimeTypeConverter())
            .registerTypeAdapter(LocationProperty.class, new PropertiesConverter()).create();

    private final String TAG = PatientRegisterFragmentInteractor.class.getName();

    public PatientRegisterFragmentInteractor() {
        eventClientRepository = RDTApplication.getInstance().getContext().getEventClientRepository();
        clientProcessor = ClientProcessor.getInstance(RDTApplication.getInstance().getApplicationContext());
    }


    public void saveRegistrationForm(final JSONObject jsonForm) {

        class SaveFormTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    final String encounterType = jsonForm.getString(ENCOUNTER_TYPE);
                    JSONObject event = saveEvent(jsonForm, encounterType, PATIENT);
                    clientProcessor.processClient(Collections.singletonList(event));
                } catch (Exception e) {
                    Log.e(TAG, "Error saving patient registration event", e);
                }
                return null;
            }
        }
        new SaveFormTask().execute();
    }

    private JSONObject saveEvent(JSONObject jsonForm, String encounterType, String bindType) throws JSONException {
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

        return eventJson;
    }
}
