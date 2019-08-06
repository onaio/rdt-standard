package io.ona.rdt_app.contract;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt_app.model.Patient;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter {
        String getMainCondition();

        void launchForm(Activity activity, String formName, Patient patient) throws JSONException;
    }

    interface View {
        void initializeAdapter();
    }
}


