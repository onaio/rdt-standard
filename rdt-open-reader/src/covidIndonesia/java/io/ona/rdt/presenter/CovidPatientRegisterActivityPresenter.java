package io.ona.rdt.presenter;

import java.lang.ref.WeakReference;

import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientRegisterActivityInteractor;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt.util.CovidRDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 17/07/2020
 */
public class CovidPatientRegisterActivityPresenter extends PatientRegisterActivityPresenter {

    public CovidPatientRegisterActivityPresenter(PatientRegisterActivityContract.View activity) {
        super(activity);
    }

    @Override
    protected PatientRegisterActivityInteractor createInteractor() {
        return new CovidPatientRegisterActivityInteractor();
    }

    @Override
    protected void launchPostRegistrationView(Patient patient) {
        CovidRDTJsonFormUtils.launchPatientProfile(patient, new WeakReference<>(getActivity()));
    }
}
