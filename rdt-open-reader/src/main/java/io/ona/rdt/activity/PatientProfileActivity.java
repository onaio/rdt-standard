package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.smartregister.view.activity.MultiLanguageActivity;

import io.ona.rdt.R;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.fragment.PatientProfileFragment;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Utils.updateLocale;

public class PatientProfileActivity extends MultiLanguageActivity implements PatientProfileActivityContract.View {

    protected PatientProfileActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this, Utils.getSharedPreference(this));
        super.onCreate(savedInstanceState);
        presenter = getPresenter();
        setContentView(getContentViewId());
        attachPatientProfileFragment();
    }

    protected void attachPatientProfileFragment() {
        replaceFragment(createPatientProfileFragment(), false);
    }

    public Fragment createPatientProfileFragment() {
        Fragment patientProfileFragment = getPatientProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, getIntent().getParcelableExtra(PATIENT));
        patientProfileFragment.setArguments(bundle);
        return patientProfileFragment;
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.replace(getFragmentId(), fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK && data != null) {
            String jsonForm = data.getStringExtra("json");
            Timber.d(jsonForm);
            presenter.saveForm(jsonForm, null);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    protected int getFragmentId() {
        return R.id.patient_profile_fragment_container;
    }

    protected int getContentViewId() {
        return (R.layout.activity_patient_profile);
    }

    protected PatientProfileActivityPresenter getPresenter() {
        if (presenter == null) {
            presenter = createPresenter();
        }
        return presenter;
    }

    protected PatientProfileActivityPresenter createPresenter() {
        return new PatientProfileActivityPresenter(this);
    }

    protected Fragment getPatientProfileFragment() {
        return new PatientProfileFragment();
    }
}
