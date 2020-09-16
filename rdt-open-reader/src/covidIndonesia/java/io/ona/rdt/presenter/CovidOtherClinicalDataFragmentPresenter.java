package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt.contract.CovidOtherClinicalDataFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidOtherClinicalDataFragmentInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 10/09/2020
 */
public class CovidOtherClinicalDataFragmentPresenter implements CovidOtherClinicalDataFragmentContract.Presenter {

    private CovidOtherClinicalDataFragmentContract.View view;
    private CovidOtherClinicalDataFragmentInteractor interactor;

    public CovidOtherClinicalDataFragmentPresenter(CovidOtherClinicalDataFragmentContract.View view) {
        this.view = view;
        interactor = new CovidOtherClinicalDataFragmentInteractor();
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
