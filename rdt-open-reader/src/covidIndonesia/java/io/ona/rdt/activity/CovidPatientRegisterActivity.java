package io.ona.rdt.activity;

import org.smartregister.view.fragment.BaseRegisterFragment;

import io.ona.rdt.fragment.CovidPatientRegisterFragment;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterActivity extends PatientRegisterActivity {

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new CovidPatientRegisterFragment();
    }
}
