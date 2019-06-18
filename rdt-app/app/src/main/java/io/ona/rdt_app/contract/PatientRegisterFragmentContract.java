package io.ona.rdt_app.contract;

import org.json.JSONException;

import io.ona.rdt_app.callback.OnFormSavedCallback;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter {
        void saveForm(String jsonForm, OnFormSavedCallback onFormSavedCallback) throws JSONException;

        String getMainCondition();
    }

    interface View {
        void initializeAdapter();
    }
}


