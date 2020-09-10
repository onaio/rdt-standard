package io.ona.rdt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import io.ona.rdt.R;

public class CovidOtherClinicalDataFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().findViewById(R.id.patient_profile_tabbed_fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.patient_profile_fragment_container).setVisibility(View.VISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_covid_other_clinical_data, container, false);
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.patient_profile_fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.patient_profile_tabbed_fragment_container).setVisibility(View.VISIBLE);
        super.onDestroyView();
    }
}