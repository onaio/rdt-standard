package io.ona.rdt.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.RDTJsonFormUtils.getPatientSexAndId;

/**
 * Created by Vincent Karuri on 13/01/2020
 */
public class PatientProfileFragment extends Fragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_patient_profile, container, false);
        rootLayout.findViewById(R.id.previous_step_text).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_previous_step).setOnClickListener(this);

        Patient patient = getArguments().getParcelable(PATIENT);
        TextView tvPatientName = rootLayout.findViewById(R.id.profile_patient_name);
        TextView tvPatientSexAndId = rootLayout.findViewById(R.id.profile_sex_and_id);
        tvPatientName.setText(patient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(patient));

        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous_step:
            case R.id.previous_step_text:
                getActivity().onBackPressed();
                break;
            default:
                // do nothing
        }
    }
}
