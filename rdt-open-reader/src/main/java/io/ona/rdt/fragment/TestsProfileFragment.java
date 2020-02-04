package io.ona.rdt.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.FormattedRDTTestDetails;

import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Test.BLOODSPOT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.INVALID;
import static io.ona.rdt.util.Constants.Test.MICROSCOPY;
import static io.ona.rdt.util.Constants.Test.NEGATIVE;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.RDT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public class TestsProfileFragment extends Fragment implements View.OnClickListener {

    private FormattedRDTTestDetails formattedRDTTestDetails;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formattedRDTTestDetails = getArguments().getParcelable(RDT_TEST_DETAILS);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_tests_profile, container, false);
        rootLayout.findViewById(R.id.btn_back_to_patient_profile).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_back_to_profile_text).setOnClickListener(this);
        populateRDTTestDetails(rootLayout);

        return rootLayout;
    }

    private void populateRDTTestDetails(View rootLayout) {
        ((TextView) rootLayout.findViewById(R.id.tests_profile_rdt_id))
                .setText(formattedRDTTestDetails.getFormattedRDTId());

        ((TextView) rootLayout.findViewById(R.id.rdt_test_name_and_date).findViewById(R.id.tv_results_label))
                .setText(formattedRDTTestDetails.getFormattedRDTType());

        ((TextView) rootLayout.findViewById(R.id.rdt_test_name_and_date).findViewById(R.id.tv_results_date))
                .setText(formattedRDTTestDetails.getFormattedRDTTestDate());

        populateRDTTestResults(formattedRDTTestDetails, rootLayout);

        RDTApplication.getInstance().getParasiteProfileRepository().getParasiteProfiles("12202K", PCR_RESULTS, RDT_Q_PCR);
        RDTApplication.getInstance().getParasiteProfileRepository().getParasiteProfiles("12202K", PCR_RESULTS, BLOODSPOT_Q_PCR);
        RDTApplication.getInstance().getParasiteProfileRepository().getParasiteProfiles("12202K", MICROSCOPY_RESULTS, MICROSCOPY);
    }

    private void populateRDTTestResults(FormattedRDTTestDetails formattedRDTTestDetails, View rootLayout) {
        TextView pvResult = rootLayout.findViewById(R.id.tv_rdt_pv_result);
        TextView pfResult = rootLayout.findViewById(R.id.tv_rdt_pf_result);
        switch (formattedRDTTestDetails.getTestResult()) {
            case (NEGATIVE):
                pvResult.setText(R.string.pv_negative_result);
                pfResult.setText(R.string.pf_negative_result);
                break;
            case (INVALID):
                pfResult.setText(R.string.invalid_result);
                pvResult.setVisibility(View.GONE);
                break;
            case (POSITIVE):
                setTestPositiveStatus(pfResult, pvResult);
                break;
            default:
                // do nothing
        }
    }

    private void setTestPositiveStatus(TextView pfResult, TextView pvResult) {
        String[] parasites = formattedRDTTestDetails.getFormattedTestResults().split(",");
        if (parasites.length < 2) {
            pfResult.setText(parasites[0]);
            String pfPositive = getResources().getString(R.string.pf_positive_result);
            if (pfPositive.equals(parasites[0])) {
                pvResult.setText(R.string.pv_negative_result);
            } else {
                pvResult.setText(R.string.pf_negative_result);
            }
        } else {
            pfResult.setText(parasites[0]);
            pvResult.setText(parasites[1]);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_to_profile_text:
            case R.id.btn_back_to_patient_profile:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                // do nothing
        }
    }
}
