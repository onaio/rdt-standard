package io.ona.rdt_app.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

import io.ona.rdt_app.activity.PatientRegisterActivity;
import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.contract.PatientRegisterActivityContract;
import io.ona.rdt_app.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt_app.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt_app.model.Patient;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class PatientRegisterActivityPresenter implements BaseRegisterContract.Presenter, PatientRegisterActivityContract.Presenter {

    PatientRegisterActivityContract.View view;
    PatientRegisterActivityInteractor interactor;

    public PatientRegisterActivityPresenter(PatientRegisterActivityContract.View view) {
        this.view = view;
        interactor = new PatientRegisterActivityInteractor();
    }

    @Override
    public void registerViewConfigurations(List<String> list) {
        // TODO: implement this
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
        // TODO: implement this
    }

    @Override
    public void onDestroy(boolean b) {
        // TODO: implement this
    }

    @Override
    public void updateInitials() {
        // TODO: implement this
    }

    @Override
    public Patient getRDTPatient(JSONObject jsonFormObject) throws JSONException {
        return interactor.getPatientForRDT(jsonFormObject);
    }

    @Override
    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        interactor.saveForm(jsonForm, callback);
    }
}
