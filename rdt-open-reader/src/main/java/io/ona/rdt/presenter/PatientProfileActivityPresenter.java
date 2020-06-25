package io.ona.rdt.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.interactor.PatientProfileActivityInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 17/02/2020
 */
public class PatientProfileActivityPresenter implements PatientProfileActivityContract.Presenter {

    protected PatientProfileActivityInteractor interactor;
    protected PatientProfileActivityContract.View activity;

    public PatientProfileActivityPresenter(PatientProfileActivityContract.View activity) {
        this.interactor = new PatientProfileActivityInteractor();
        this.activity = activity;
    }

    @Override
    public void saveForm(String jsonForm, OnFormSavedCallback callback) {
        try {
            interactor.saveForm(new JSONObject(jsonForm), callback);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
