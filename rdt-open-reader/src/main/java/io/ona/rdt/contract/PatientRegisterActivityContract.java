package io.ona.rdt.contract;

import android.app.Activity;

import org.json.JSONException;
import org.smartregister.view.contract.BaseRegisterContract;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.Patient;


/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {

    interface View extends OnFormSavedCallback {
        void openDrawerLayout();
        void closeDrawerLayout();
    }

    interface Presenter extends BaseRegisterContract.Presenter  {
        void launchForm(Activity activity, String formName, Patient patient) throws JSONException;
        void saveForm(String jsonForm, OnFormSavedCallback callback);
    }
}
