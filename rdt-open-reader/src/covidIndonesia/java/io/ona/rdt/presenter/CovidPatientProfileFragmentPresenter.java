package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt.contract.CovidPatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientProfileFragmentInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileFragmentPresenter implements CovidPatientProfileFragmentContract.Presenter {

    private CovidPatientProfileFragmentContract.View view;
    private CovidPatientProfileFragmentInteractor interactor;

    public CovidPatientProfileFragmentPresenter(CovidPatientProfileFragmentContract.View view) {
        this.view = view;
        interactor = new CovidPatientProfileFragmentInteractor();
    }

    @Override
    public void launchForm(Activity activity, String formName, Patient patient)  {
        try {
            interactor.launchForm(activity, formName, patient);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
