package io.ona.rdt.activity;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import io.ona.rdt.R;
import io.ona.rdt.adapter.ProfileFragmentAdapter;
import io.ona.rdt.contract.CovidPatientProfileActivityContract;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileActivity extends PatientProfileActivity implements CovidPatientProfileActivityContract.View {

    @Override
    protected void attachPatientProfileFragment() {
        ((ViewPager2) findViewById(R.id.covid_patient_profile_fragment_container))
                .setAdapter(new ProfileFragmentAdapter(this));
    }

    @Override
    protected int getContentViewId() {
        return (R.layout.activity_covid_patient_profile);
    }

    @Override
    protected PatientProfileActivityPresenter createPresenter() {
        return new CovidPatientProfileActivityPresenter(this);
    }

    @Override
    protected Fragment getPatientProfileFragment() {
        return new CovidPatientProfileFragment();
    }
}
