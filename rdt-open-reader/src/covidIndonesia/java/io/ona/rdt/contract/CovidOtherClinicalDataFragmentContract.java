package io.ona.rdt.contract;

import android.app.Activity;

import io.ona.rdt.domain.Patient;

/**
 * Created by Vincent Karuri on 10/09/2020
 */
public interface CovidOtherClinicalDataFragmentContract {

    interface View {

    }

    interface Presenter {
        void launchForm(Activity activity, String formName, Patient patient);
    }
}
