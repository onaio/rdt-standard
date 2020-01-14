package io.ona.rdt.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import io.ona.rdt.R;
import io.ona.rdt.fragment.PatientProfileFragment;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;

public class PatientProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_profile);
        attachPatientProfileFragment();
    }

    private void attachPatientProfileFragment() {
        PatientProfileFragment patientProfileFragment = new PatientProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, getIntent().getParcelableExtra(PATIENT));
        patientProfileFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.patient_profile_fragment_container, patientProfileFragment);
        fragmentTransaction.commit();
    }
}
