package io.ona.rdt.interactor;

import org.json.JSONObject;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 17/02/
 */
public class CovidPatientProfileActivityInteractor extends PatientProfileActivityInteractor {
    private FormSaver formSaver = new CovidFormSaver();

    @Override
    public void saveForm(JSONObject jsonForm, OnFormSavedCallback onFormSavedCallback) {
        formSaver.saveForm(jsonForm, onFormSavedCallback);
    }
}

