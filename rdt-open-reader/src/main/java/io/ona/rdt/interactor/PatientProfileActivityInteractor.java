package io.ona.rdt.interactor;

import org.json.JSONObject;

import io.ona.rdt.callback.OnFormSavedCallback;

/**
 * Created by Vincent Karuri on 17/02/2020
 */
public class PatientProfileActivityInteractor {

    private PatientRegisterFragmentInteractor patientRegisterFragmentInteractor;

    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        getPatientRegisterFragmentInteractor().saveForm(jsonForm, callback);
    }

    public PatientRegisterFragmentInteractor getPatientRegisterFragmentInteractor() {
        if (patientRegisterFragmentInteractor == null) {
            patientRegisterFragmentInteractor = new PatientRegisterFragmentInteractor();
        }
        return patientRegisterFragmentInteractor;
    }
}
