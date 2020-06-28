package io.ona.rdt.fragment;

import android.content.Intent;

import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterFragment extends PatientRegisterFragment {

    @Override
    public void launchPatientProfile(Patient patient) {
        Intent intent = new Intent(getActivity(), CovidPatientProfileActivity.class);
        intent.putExtra(PATIENT, patient);
        startActivity(intent, null);
    }
}
