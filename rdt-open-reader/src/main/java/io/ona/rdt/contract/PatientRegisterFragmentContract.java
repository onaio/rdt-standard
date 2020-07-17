package io.ona.rdt.contract;

import android.app.Activity;

import org.smartregister.view.contract.BaseRegisterFragmentContract;

import io.ona.rdt.domain.Patient;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter extends BaseRegisterFragmentContract.Presenter {
        String getMainCondition();

        void launchForm(Activity activity, String formName, Patient patient);
    }

    interface View extends BaseRegisterFragmentContract.View {
        void initializeAdapter();
        void launchPatientProfile(Patient patient);
        String getRegisterTableName();
    }
}


