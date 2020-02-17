package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import io.ona.rdt.R;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.fragment.PatientProfileFragment;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;

public class PatientProfileActivity extends FragmentActivity implements PatientProfileActivityContract.View, OnFormSavedCallback {

    private PatientProfileActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PatientProfileActivityPresenter(this);
        setContentView(R.layout.activity_patient_profile);
        attachPatientProfileFragment();
    }

    private void attachPatientProfileFragment() {
        PatientProfileFragment patientProfileFragment = new PatientProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, getIntent().getParcelableExtra(PATIENT));
        patientProfileFragment.setArguments(bundle);
        replaceFragment(patientProfileFragment, false);
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(R.id.patient_profile_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK && data != null) {
            String jsonForm = data.getStringExtra("json");
            Timber.d(jsonForm);
            presenter.saveForm(jsonForm, this);
        }
    }

    @Override
    public void onFormSaved() {

    }
}
