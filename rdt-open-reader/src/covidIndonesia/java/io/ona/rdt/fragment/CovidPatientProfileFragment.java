package io.ona.rdt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import io.ona.rdt.R;
import io.ona.rdt.contract.CovidPatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.CovidPatientProfileFragmentPresenter;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SUPPORT_INVESTIGATION_FORM;
import static io.ona.rdt.util.RDTJsonFormUtils.getPatientSexAndId;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_covid_patient_profile, container, false);
        addListeners(rootLayout);
        populatePatientDetails(rootLayout);
        return rootLayout;
    }

    private void populatePatientDetails(final View rootLayout) {
        currPatient = getArguments().getParcelable(PATIENT);
        TextView tvPatientName = rootLayout.findViewById(R.id.covid_profile_patient_name);
        TextView tvPatientSexAndId = rootLayout.findViewById(R.id.covid_profile_sex_and_id);
        tvPatientName.setText(currPatient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(currPatient));
    }

    private void addListeners(final View rootLayout) {
        rootLayout.findViewById(R.id.tv_covid_delivery_details).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_rdt).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_sample_collection).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_support_investigation).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_symptoms_and_history).setOnClickListener(this);
        rootLayout.findViewById(R.id.covid_previous_step_text).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_covid_back_to_patient_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_covid_back_to_patient_register:
            case R.id.covid_previous_step_text:
                getActivity().onBackPressed();
                break;
            default:
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
            case R.id.tv_covid_support_investigation:
                formName = SUPPORT_INVESTIGATION_FORM;
                break;
            case R.id.tv_covid_symptoms_and_history:
                formName = PATIENT_DIAGNOSTICS_FORM;
                break;
        }
        return formName;
    }
}
