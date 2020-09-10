package io.ona.rdt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.ona.rdt.R;
import io.ona.rdt.contract.CovidPatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.CovidPatientProfileFragmentPresenter;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM;

/**
 * Created by Vincent Karuri on 12/06/2020
 */
public class CovidPatientProfileFragment extends Fragment implements View.OnClickListener, CovidPatientProfileFragmentContract.View {

    private Patient currPatient;
    private CovidPatientProfileFragmentPresenter patientProfileFragmentPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patientProfileFragmentPresenter = new CovidPatientProfileFragmentPresenter(this);
        currPatient = getArguments().getParcelable(PATIENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_covid_patient_profile, container, false);
        addListeners(rootLayout);
        return rootLayout;
    }

    private void addListeners(final View rootLayout) {
        rootLayout.findViewById(R.id.tv_covid_delivery_details).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_rdt).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_sample_collection).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_support_investigation).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_symptoms_and_history).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_covid_support_investigation) {
            launchOtherClinicalDataFragment();
        } else {
            patientProfileFragmentPresenter.launchForm(getActivity(), getFormName(v), currPatient);
        }
    }


    private String getFormName(View view) {
        String formName = null;
        switch (view.getId()) {
            case R.id.tv_covid_delivery_details:
                formName = SAMPLE_DELIVERY_DETAILS_FORM;
                break;
            case R.id.tv_covid_rdt:
                formName = COVID_RDT_TEST_FORM;
                break;
            case R.id.tv_covid_sample_collection:
                formName = SAMPLE_COLLECTION_FORM;
                break;
            case R.id.tv_covid_symptoms_and_history:
                formName = PATIENT_DIAGNOSTICS_FORM;
                break;
        }
        return formName;
    }

    private void launchOtherClinicalDataFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PATIENT, currPatient);

        CovidOtherClinicalDataFragment covidOtherClinicalDataFragment = new CovidOtherClinicalDataFragment();
        covidOtherClinicalDataFragment.setArguments(bundle);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.replace(R.id.patient_profile_fragment_container, covidOtherClinicalDataFragment);
        fragmentTransaction.commit();
    }
}
