package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import io.ona.rdt.R;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.CovidPatientProfileActivityContract;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileActivity extends FragmentActivity implements CovidPatientProfileActivityContract.View, OnFormSavedCallback {

    private CovidPatientProfileActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_patient_profile);
        attachPatientProfileFragment();
        presenter = new CovidPatientProfileActivityPresenter(this);
    }

    private void attachPatientProfileFragment() {
        CovidPatientProfileFragment patientProfileFragment = new CovidPatientProfileFragment();
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
        fragmentTransaction.replace(R.id.covid_patient_profile_fragment_container, fragment);
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
