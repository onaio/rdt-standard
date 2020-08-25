package io.ona.rdt.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import io.ona.rdt.R;
import io.ona.rdt.adapter.ProfileFragmentAdapter;
import io.ona.rdt.contract.CovidPatientProfileActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.RDTJsonFormUtils.getPatientSexAndId;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileActivity extends PatientProfileActivity implements
        CovidPatientProfileActivityContract.View, View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populatePatientDetails();
        addListeners();
        setUpTabs();
    }

    private void setUpTabs() {
        TabLayout tabLayout = findViewById(R.id.profile_tab_layout);
        ViewPager2 viewPager = findViewById(R.id.covid_patient_profile_fragment_container);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            String tabText = "";
            switch (position) {
                case 0:
                    tabText = getResources().getString(R.string.covid_patient_forms_tab_name);
                    break;
                case 1:
                    tabText = getResources().getString(R.string.covid_patient_history_tab_name);
                    break;
            }
            tab.setText(tabText);
        }).attach();
    }

    private void addListeners() {
        findViewById(R.id.covid_previous_step_text).setOnClickListener(this);
        findViewById(R.id.btn_covid_back_to_patient_register).setOnClickListener(this);
    }

    private void populatePatientDetails() {
        Patient currPatient = getIntent().getParcelableExtra(PATIENT);
        TextView tvPatientName = findViewById(R.id.covid_profile_patient_name);
        TextView tvPatientSexAndId = findViewById(R.id.covid_profile_sex_and_id);
        tvPatientName.setText(currPatient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(currPatient));
    }

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_covid_back_to_patient_register:
            case R.id.covid_previous_step_text:
                onBackPressed();
                break;
        }
    }
}
