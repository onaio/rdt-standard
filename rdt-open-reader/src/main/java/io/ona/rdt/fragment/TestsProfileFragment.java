package io.ona.rdt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.contract.TestsProfileFragmentContract;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.presenter.TestsProfileFragmentPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Format.PROFILE_DATE_FORMAT;
import static io.ona.rdt.util.Constants.Table.MICROSCOPY_RESULTS;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Test.BLOODSPOT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.INVALID;
import static io.ona.rdt.util.Constants.Test.MICROSCOPY;
import static io.ona.rdt.util.Constants.Test.NEGATIVE;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_Q_PCR;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
import static io.ona.rdt.util.Utils.convertDate;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public class TestsProfileFragment extends Fragment implements View.OnClickListener, TestsProfileFragmentContract.View {

    private FormattedRDTTestDetails formattedRDTTestDetails;
    private TestsProfileFragmentPresenter presenter;
    private View rootLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formattedRDTTestDetails = getArguments().getParcelable(RDT_TEST_DETAILS);
        presenter = new TestsProfileFragmentPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootLayout = inflater.inflate(R.layout.fragment_tests_profile, container, false);
        rootLayout.findViewById(R.id.btn_back_to_patient_profile).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_back_to_profile_text).setOnClickListener(this);
        populateRDTTestDetails(rootLayout);
        populateExperimentTypeField(rootLayout.findViewById(R.id.rdt_microscopy_results), MICROSCOPY);
        populateExperimentTypeField(rootLayout.findViewById(R.id.blood_spot_qpcr_results), BLOODSPOT_Q_PCR);
        populateExperimentTypeField(rootLayout.findViewById(R.id.rdt_qpcr_results), RDT_Q_PCR);

        return rootLayout;
    }

    private void populateRDTTestDetails(View rootLayout) {
        ((TextView) rootLayout.findViewById(R.id.tests_profile_rdt_id))
                .setText(formattedRDTTestDetails.getFormattedRDTId());

        View testNameAndDate = rootLayout.findViewById(R.id.rdt_test_name_and_date);
        ((TextView) testNameAndDate.findViewById(R.id.tv_results_label))
                .setText(formattedRDTTestDetails.getFormattedRDTType());
        ((TextView) testNameAndDate.findViewById(R.id.tv_results_date))
                .setText(formattedRDTTestDetails.getFormattedRDTTestDate());

        populateRDTTestResults();

        String[] fragmentedRdtIdStr = formattedRDTTestDetails.getFormattedRDTId().split(" ");
        String rdtId = fragmentedRdtIdStr[fragmentedRdtIdStr.length - 1];
        getParasiteProfiles(rdtId, PCR_RESULTS, RDT_Q_PCR);
        getParasiteProfiles(rdtId, PCR_RESULTS, BLOODSPOT_Q_PCR);
        getParasiteProfiles(rdtId, MICROSCOPY_RESULTS, MICROSCOPY);
    }

    private void populateRDTTestResults() {
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
            pfResult.setText(parasites[0].trim());
            pvResult.setText(parasites[1].trim());
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

    public synchronized void getParasiteProfiles(String rdtId, String tableName, String experimentType) {
        class GetParasiteProfilesTask extends AsyncTask<Void, Void, List<ParasiteProfileResult>> {
            @Override
            protected List<ParasiteProfileResult> doInBackground(Void... voids) {
                return presenter.getParasiteProfiles(rdtId, tableName, experimentType);
            }

            @Override
            protected void onPostExecute(List<ParasiteProfileResult> parasiteProfileResults) {
                onParasiteProfileFetched(parasiteProfileResults);
            }
        }
        new GetParasiteProfilesTask().execute();
    }

    @Override
    public synchronized void onParasiteProfileFetched(List<ParasiteProfileResult> parasiteProfileResults) {
        if (parasiteProfileResults.isEmpty()) {
            return;
        }
        try {
            ParasiteProfileResult parasiteProfileResult = parasiteProfileResults.get(0);
            switch (parasiteProfileResult.getExperimentType()) {
                case MICROSCOPY:
                    populateParasiteProfileResults(rootLayout.findViewById(R.id.rdt_microscopy_results), parasiteProfileResult);
                    break;
                case BLOODSPOT_Q_PCR:
                    populateParasiteProfileResults(rootLayout.findViewById(R.id.blood_spot_qpcr_results), parasiteProfileResult);
                    break;
                case RDT_Q_PCR:
                    populateParasiteProfileResults(rootLayout.findViewById(R.id.rdt_qpcr_results), parasiteProfileResult);
                    break;
                default:
                    // do nothing
            }
        } catch (ParseException e) {
            Timber.e(e);
        }
    }

    private void populateExperimentTypeField(View parasiteProfile, String experimentType) {
        String formattedExperimentType = formattedExperimentType(experimentType);
        ((TextView) parasiteProfile.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_label))
                .setText(formattedExperimentType);
    }

    private void populateParasiteProfileResults(View parasiteProfile, ParasiteProfileResult parasiteProfileResult) throws ParseException {
        populateFormattedParasiteProfile(parasiteProfile, parasiteProfileResult);

        String humanReadableDate = convertDate(parasiteProfileResult.getExperimentDate(),
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", PROFILE_DATE_FORMAT);
        ((TextView) parasiteProfile.findViewById(R.id.label_and_date).findViewById(R.id.tv_results_date))
                .setText(humanReadableDate);
    }

    private String formattedExperimentType(String experimentType) {
        String formattedExperimentType = "";
        switch (experimentType) {
            case RDT_Q_PCR:
                formattedExperimentType =  formattedRDTTestDetails.getFormattedRDTType() + Q_PCR ;
                break;
            case BLOODSPOT_Q_PCR:
                formattedExperimentType = getResources().getString(R.string.blood_spot) + Q_PCR;
                break;
            case MICROSCOPY:
                formattedExperimentType = getResources().getString(R.string.microscopy);
                break;
            default:
                // do nothing
        }
        return formattedExperimentType;
    }

    private void populateFormattedParasiteProfile(View parasiteProfile, ParasiteProfileResult parasiteProfileResult) {
        TextView tvFalciparum = parasiteProfile.findViewById(R.id.tv_qpcr_falciparum);
        tvFalciparum.setText(tvFalciparum.getText() + capitalize(parasiteProfileResult.getpFalciparum()));

        TextView tvVivax = parasiteProfile.findViewById(R.id.tv_qpcr_vivax);
        tvVivax.setText(tvVivax.getText() + capitalize(parasiteProfileResult.getpVivax()));

        TextView tvMalariae = parasiteProfile.findViewById(R.id.tv_qpcr_malariae);
        tvMalariae.setText(tvMalariae.getText() + capitalize(parasiteProfileResult.getpMalariae()));

        TextView tvOvale = parasiteProfile.findViewById(R.id.tv_qpcr_ovale);
        tvOvale.setText(tvOvale.getText() + capitalize(parasiteProfileResult.getpOvale()));

        TextView tvGameto = parasiteProfile.findViewById(R.id.tv_qpcr_gameto);
        tvGameto.setText(tvGameto.getText() + capitalize(parasiteProfileResult.getPfGameto()));
    }
}
