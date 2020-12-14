package io.ona.rdt.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import io.ona.rdt.R;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.fragment.PatientProfileFragment;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Utils.updateLocale;

public class PatientProfileActivity extends FragmentActivity implements PatientProfileActivityContract.View {

    protected PatientProfileActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this);
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
        patientProfileFragment.setArguments(getPatientBundle());
        return patientProfileFragment;
    }

    protected Bundle getPatientBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, getIntent().getParcelableExtra(PATIENT));
        return bundle;
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
        if (requestCode == REQUEST_CODE_GET_JSON && resultCode == JsonFormConstants.RESULT_CODE.RUNTIME_EXCEPTION_OCCURRED) {
            Bundle bundle = data.getExtras();
            throw  (RuntimeException) bundle.getSerializable(JsonFormConstants.RESULT_INTENT.RUNTIME_EXCEPTION);
        } else if (requestCode == REQUEST_CODE_GET_JSON && resultCode == Activity.RESULT_OK && data != null) {
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, getHomeActivityClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    protected Class<? extends PatientRegisterActivity> getHomeActivityClass() {
        return PatientRegisterActivity.class;
    }
}
