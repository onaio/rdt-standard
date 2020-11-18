package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;

import static io.ona.rdt.util.Constants.DBConstants.AGE;
import static io.ona.rdt.util.Constants.DBConstants.DOB;
import static io.ona.rdt.util.Constants.DBConstants.FIRST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.LAST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.PATIENT_ID;
import static io.ona.rdt.util.Constants.DBConstants.SEX;

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
        return new String[] {"relationalid", FIRST_NAME, LAST_NAME, AGE, SEX, PATIENT_ID, DOB};
    }
}
