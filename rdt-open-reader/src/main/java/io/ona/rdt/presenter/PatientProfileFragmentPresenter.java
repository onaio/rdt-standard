package io.ona.rdt.presenter;

import android.app.Activity;

import java.util.List;

import io.ona.rdt.contract.PatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.interactor.PatientProfileFragmentInteractor;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 15/01/2020
 */
public class PatientProfileFragmentPresenter implements PatientProfileFragmentContract.Presenter {

    private PatientProfileFragmentContract.View patientProfileFragment;
    private PatientProfileFragmentInteractor patientProfileFragmentInteractor;

    public PatientProfileFragmentPresenter(PatientProfileFragmentContract.View patientProfileFragment) {
        this.patientProfileFragment = patientProfileFragment;
        this.patientProfileFragmentInteractor = new PatientProfileFragmentInteractor();
    }

    public void launchForm(Activity activity, Patient patient) {
        try {
            patientProfileFragmentInteractor.launchForm(activity, RDT_TEST_FORM, patient);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    public List<RDTTestDetails> getRDTTestDetailsByBaseEntityId(String baseEntityId) {
        return patientProfileFragmentInteractor.getRDTTestDetailsByBaseEntityId(baseEntityId);
    }
}
