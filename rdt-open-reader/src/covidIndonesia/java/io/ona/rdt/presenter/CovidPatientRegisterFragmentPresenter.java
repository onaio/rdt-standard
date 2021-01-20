package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt.util.Constants;

/**
 * Created by Vincent Karuri on 05/08/2020
 */
public class CovidPatientRegisterFragmentPresenter extends PatientRegisterFragmentPresenter {

    public CovidPatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        super(patientRegisterFragment);
    }

    @Override
    protected PatientRegisterFragmentInteractor createInteractor() {
        return new CovidPatientRegisterFragmentInteractor();
    }

    @Override
    protected String[] getMainColumns() {
        return new String[] {"relationalid", Constants.DBConstants.FIRST_NAME, Constants.DBConstants.LAST_NAME, Constants.DBConstants.AGE, Constants.DBConstants.SEX, Constants.DBConstants.PATIENT_ID, Constants.DBConstants.DOB};
    }
}
