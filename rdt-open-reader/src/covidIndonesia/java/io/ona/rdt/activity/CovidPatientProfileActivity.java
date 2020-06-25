package io.ona.rdt.activity;

import androidx.fragment.app.Fragment;
import io.ona.rdt.R;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.CovidPatientProfileActivityContract;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileActivity extends PatientProfileActivity implements CovidPatientProfileActivityContract.View {

    @Override
    protected int getFragmentId() {
        return R.id.covid_patient_profile_fragment_container;
    }

    @Override
    protected int getContentViewId() {
        return (R.layout.activity_covid_patient_profile);
    }

    @Override
    protected PatientProfileActivityPresenter getPresenter() {
        return new CovidPatientProfileActivityPresenter(this);
    }

    @Override
    protected Fragment getPatientProfileFragment() {
        return new CovidPatientProfileFragment();
    }
}
