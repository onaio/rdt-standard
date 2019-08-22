package io.ona.rdt_app.contract;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.model.Patient;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View {
        void openDrawerLayout();

        void closeDrawerLayout();
    }

    interface Presenter {
        Patient getRDTPatient(JSONObject jsonFormObject) throws JSONException;

        void saveForm(JSONObject jsonForm, OnFormSavedCallback callback);
    }
}
