package io.ona.rdt.presenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.CovidPatientProfileActivityContract;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.interactor.CovidPatientProfileActivityInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 17/02/2020
 */
public class CovidPatientProfileActivityPresenter implements PatientProfileActivityContract.Presenter {

    private CovidPatientProfileActivityInteractor interactor;
    private CovidPatientProfileActivityContract.View activity;

    public CovidPatientProfileActivityPresenter(CovidPatientProfileActivityContract.View activity) {
        this.interactor = new CovidPatientProfileActivityInteractor();
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
