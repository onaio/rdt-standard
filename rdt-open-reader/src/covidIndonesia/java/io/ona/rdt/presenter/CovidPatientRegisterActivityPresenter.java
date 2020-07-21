package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.interactor.CovidPatientRegisterActivityInteractor;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;

/**
 * Created by Vincent Karuri on 17/07/2020
 */
public class CovidPatientRegisterActivityPresenter extends PatientRegisterActivityPresenter {

    public CovidPatientRegisterActivityPresenter(PatientRegisterActivityContract.View activity) {
        super(activity);
    }

    @Override
    protected PatientRegisterActivityInteractor getInteractor() {
        return new CovidPatientRegisterActivityInteractor();
    }
}
