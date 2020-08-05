package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 05/08/2020
 */
public class CovidPatientRegisterFragmentPresenter extends PatientRegisterFragmentPresenter {

    public CovidPatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        super(patientRegisterFragment);
    }

    @Override
    protected PatientRegisterFragmentInteractor getInteractor() {
        return new CovidPatientRegisterFragmentInteractor();
    }
}
