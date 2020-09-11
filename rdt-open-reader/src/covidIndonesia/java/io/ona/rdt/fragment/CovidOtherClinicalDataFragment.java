package io.ona.rdt.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import io.ona.rdt.R;
import io.ona.rdt.contract.CovidOtherClinicalDataFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.CovidOtherClinicalDataFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidOtherClinicalDataFragment extends Fragment implements CovidOtherClinicalDataFragmentContract.View,
        View.OnClickListener {

    private CovidOtherClinicalDataFragmentPresenter presenter;
    private Patient currPatient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActivity().findViewById(R.id.patient_profile_tabbed_fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.patient_profile_fragment_container).setVisibility(View.VISIBLE);

        presenter = new CovidOtherClinicalDataFragmentPresenter(this);
        currPatient = getArguments().getParcelable(Constants.FormFields.PATIENT);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_covid_other_clinical_data, container, false);
        addListeners(rootLayout);
        return rootLayout;
    }

    @Override
    public void onDestroyView() {
        getActivity().findViewById(R.id.patient_profile_fragment_container).setVisibility(View.GONE);
        getActivity().findViewById(R.id.patient_profile_tabbed_fragment_container).setVisibility(View.VISIBLE);
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        presenter.launchForm(getActivity(), getFormName(v), currPatient);
    }

    private void addListeners(final View rootLayout) {
        rootLayout.findViewById(R.id.tv_covid_xray).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_covid_wbc).setOnClickListener(this);
    }

    private String getFormName(View view) {
        String formName = null;
        switch (view.getId()) {
            case R.id.tv_covid_wbc:
                formName = CovidConstants.Form.WBC_FORM;
                break;
            case R.id.tv_covid_xray:
                formName = CovidConstants.Form.XRAY_FORM;
                break;
        }
        return formName;
    }
}


