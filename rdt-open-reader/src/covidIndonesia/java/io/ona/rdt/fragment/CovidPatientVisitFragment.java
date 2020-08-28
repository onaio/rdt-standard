package io.ona.rdt.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vijay.jsonwizard.utils.Utils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientHistoryActivity;
import io.ona.rdt.adapter.CovidPatientVisitAdapter;
import io.ona.rdt.contract.CovidPatientVisitFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.presenter.CovidPatientVisitFragmentPresenter;
import io.ona.rdt.util.Constants;

public class CovidPatientVisitFragment extends Fragment implements CovidPatientVisitFragmentContract.View,
        View.OnClickListener {

    private Patient currPatient;
    private CovidPatientVisitFragmentPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currPatient = getActivity().getIntent().getParcelableExtra(Constants.FormFields.PATIENT);
        presenter = new CovidPatientVisitFragmentPresenter(this);
    }

    public static CovidPatientVisitFragment getInstance (Bundle args) {
        CovidPatientVisitFragment covidPatientVisitFragment = new CovidPatientVisitFragment();
        covidPatientVisitFragment.setArguments(args);
        return covidPatientVisitFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_covid_patient_visit, container, false);
        populateVisits(rootLayout);
        return rootLayout;
    }

    private void populateVisits(View rootLayout) {
        RecyclerView patientVisitList = rootLayout.findViewById(R.id.covid_patient_visit_list);
        patientVisitList.setHasFixedSize(true);
        patientVisitList.setLayoutManager(new LinearLayoutManager(getContext()));
        patientVisitList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        fetchAndPopulateVisits(patientVisitList);
    }

    private void fetchAndPopulateVisits(RecyclerView patientVisitList) {
        class FetchPatientVisitsTask extends AsyncTask<Void, Void, List<Visit>> {

            @Override
            protected void onPreExecute() {
                Utils.showProgressDialog(R.string.please_wait_title, R.string.please_wait_message, getActivity());
            }

            @Override
            protected List<Visit> doInBackground(Void... voids) {
                return presenter.getPatientVisits(currPatient.getBaseEntityId());
            }

            @Override
            protected void onPostExecute(List<Visit> visits) {
                Utils.hideProgressDialog();
                patientVisitList.setAdapter(new CovidPatientVisitAdapter(visits, CovidPatientVisitFragment.this));
            }
        }

        new FetchPatientVisitsTask().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                launchPatientHistory();
        }
    }

    private void launchPatientHistory() {
        Intent intent = new Intent(getActivity(), CovidPatientHistoryActivity.class);
        intent.putExtra(Constants.FormFields.PATIENT, currPatient);
        getActivity().startActivity(intent);
    }
}
