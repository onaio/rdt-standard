package io.ona.rdt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.ona.rdt.R;
import io.ona.rdt.contract.PatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.PatientProfileFragmentPresenter;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.RDTJsonFormUtils.getPatientSexAndId;

/**
 * Created by Vincent Karuri on 13/01/2020
 */
public class PatientProfileFragment extends Fragment implements View.OnClickListener, PatientProfileFragmentContract.View {

    private PatientProfileFragmentPresenter patientProfileFragmentPresenter;
    private Patient currPatient;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.patientProfileFragmentPresenter = new PatientProfileFragmentPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        rootLayout.findViewById(R.id.previous_step_text).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_previous_step).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_profile_record_rdt_test).setOnClickListener(this);

        currPatient = getArguments().getParcelable(PATIENT);
        TextView tvPatientName = rootLayout.findViewById(R.id.profile_patient_name);
        TextView tvPatientSexAndId = rootLayout.findViewById(R.id.profile_sex_and_id);
        tvPatientName.setText(currPatient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(currPatient));

        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_step:
            case R.id.previous_step_text:
                getActivity().onBackPressed();
                break;
            case R.id.btn_profile_record_rdt_test:
                patientProfileFragmentPresenter.launchForm(getActivity(), currPatient);
                break;
            default:
                // do nothing
        }
    }
}
