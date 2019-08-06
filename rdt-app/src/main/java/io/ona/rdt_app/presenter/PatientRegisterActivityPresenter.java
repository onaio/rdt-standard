package io.ona.rdt_app.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.contract.PatientRegisterActivityContract;
import io.ona.rdt_app.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class PatientRegisterActivityPresenter implements BaseRegisterContract.Presenter, PatientRegisterActivityContract.Presenter {

    private PatientRegisterActivityContract.View view;
    private Activity activity;
    private PatientRegisterActivityInteractor interactor;

    public PatientRegisterActivityPresenter(PatientRegisterActivityContract.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
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
    public void saveForm(String jsonForm, OnFormSavedCallback callback) throws JSONException {
        JSONObject jsonFormObj = new JSONObject(jsonForm);
        interactor.saveForm(jsonFormObj, callback);
        Patient patient = interactor.getPatientForRDT(jsonFormObj);
        if (patient != null) {
            interactor.launchForm(activity, RDT_TEST_FORM, patient);
        }
    }
}
