package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;

/**
 * Created by Vincent Karuri on 05/08/2020
 */
public class CovidPatientRegisterFragmentPresenter extends PatientRegisterFragmentPresenter {

    public CovidPatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        super(patientRegisterFragment);
    }

    protected PatientRegisterFragmentInteractor createInteractor() {
        return new CovidPatientRegisterFragmentInteractor();
    }
}
