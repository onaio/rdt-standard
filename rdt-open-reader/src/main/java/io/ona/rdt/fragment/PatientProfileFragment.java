package io.ona.rdt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.adapter.RDTTestListAdapter;
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
        setUpRDTTestList(rootLayout);

        return rootLayout;
    }

    private void addListeners(final View rootLayout) {
        rootLayout.findViewById(R.id.previous_step_text).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_back_to_patient_register).setOnClickListener(this);
        rootLayout.findViewById(R.id.btn_profile_record_rdt_test).setOnClickListener(this);
    }

    private void populatePatientDetails(final View rootLayout) {
        currPatient = getArguments().getParcelable(PATIENT);
        TextView tvPatientName = rootLayout.findViewById(R.id.profile_patient_name);
        TextView tvPatientSexAndId = rootLayout.findViewById(R.id.profile_sex_and_id);
        tvPatientName.setText(currPatient.getPatientName());
        tvPatientSexAndId.setText(getPatientSexAndId(currPatient));
    }

    private void setUpRDTTestList(final View rootLayout) {
        RecyclerView rdtTestList = rootLayout.findViewById(R.id.rdt_tests_list);
        rdtTestList.setHasFixedSize(true);
        rdtTestList.setLayoutManager(new LinearLayoutManager(getActivity()));
        populateRDTTestList(rdtTestList);
    }

    private void populateRDTTestList(RecyclerView rdtTestList) {

        class PopulateRDTListTask extends AsyncTask<Void, Void, List<RDTTestDetails>> {

            @Override
            protected List<RDTTestDetails> doInBackground(Void... voids) {
                return patientProfileFragmentPresenter.getRDTTestDetailsByBaseEntityId(currPatient.getBaseEntityId());
            }

            @Override
            protected void onPostExecute(List<RDTTestDetails> rdtTestDetails) {
                rdtTestList.setAdapter(new RDTTestListAdapter(getActivity(), rdtTestDetails));
            }
        }

        new PopulateRDTListTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back_to_patient_register:
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
