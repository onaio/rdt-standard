package io.ona.rdt_app.contract;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;


/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View extends OnFormSavedCallback {
        void openDrawerLayout();

        void closeDrawerLayout();
    }

    interface Presenter {

        void saveForm(String jsonForm, OnFormSavedCallback callback) throws JSONException;
    }
}
