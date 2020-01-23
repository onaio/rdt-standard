package io.ona.rdt.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.adapter.RDTTestListAdapter;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.PatientProfileFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.RDTTestDetails;
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
        addListeners(rootLayout);
        populatePatientDetails(rootLayout);
        populateRDTList(rootLayout);

        return rootLayout;
    }

    private void addListeners(final View rootLayout) {
        rootLayout.findViewById(R.id.previous_step_text).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_previous_step).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_profile_record_rdt_test).setOnClickListener(this);
    }

    private void populatePatientDetails(final View rootLayout) {
        currPatient = getArguments().getParcelable(PATIENT);
        TextView tvPatientName = rootLayout.findViewById(R.id.profile_patient_name);
        TextView tvPatientSexAndId = rootLayout.findViewById(R.id.profile_sex_and_id);
        tvPatientName.setText(currPatient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(currPatient));
    }

    private void populateRDTList(final View rootLayout) {
        RecyclerView rdtTestList = rootLayout.findViewById(R.id.rdt_tests_list);
        rdtTestList.setHasFixedSize(true);
        rdtTestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rdtTestList.setAdapter(new RDTTestListAdapter(getRDTTestDetails()));
    }

    private List<RDTTestDetails> getRDTTestDetails() {
        List<String> testResults = new ArrayList<>();
        testResults.add("Pf positive");
        testResults.add("Pv positive");

        List<RDTTestDetails> rdtTestDetails = new ArrayList<>();
        RDTTestDetails rdtTestDetail = new RDTTestDetails();
        rdtTestDetail.setRdtId("RDT ID 003");
        rdtTestDetail.setDate("2 days ago");
        rdtTestDetail.setRdtType("CareStart RDT");
        rdtTestDetail.setTestResult(testResults);
        rdtTestDetails.add(rdtTestDetail);

        rdtTestDetail = new RDTTestDetails();
        rdtTestDetail.setRdtId("RDT ID 002");
        rdtTestDetail.setDate("3 days ago");
        rdtTestDetail.setRdtType("CareStart RDT");
        rdtTestDetail.setTestResult(testResults);
        rdtTestDetails.add(rdtTestDetail);

        rdtTestDetail = new RDTTestDetails();
        rdtTestDetail.setRdtId("RDT ID 001");
        rdtTestDetail.setDate("1 year ago");
        rdtTestDetail.setRdtType("Open Guideline RDT");
        rdtTestDetail.setTestResult(testResults);
        rdtTestDetails.add(rdtTestDetail);

        RDTApplication.getInstance().getRdtTestsRepository().getRDTTestDetailsByBaseEntityId("14349b70-0dfa-4ca6-ac62-b1240fd055e6");

        return rdtTestDetails;
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
