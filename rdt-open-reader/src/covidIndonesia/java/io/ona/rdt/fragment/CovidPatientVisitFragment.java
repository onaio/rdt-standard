package io.ona.rdt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.adapter.CovidPatientVisitAdapter;
import io.ona.rdt.domain.Visit;

public class CovidPatientVisitFragment extends Fragment {

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
        fetchAndPopulateVisits(patientVisitList,""); // todo: replace with appropriate params
    }

    private void fetchAndPopulateVisits(RecyclerView patientVisitList, String baseEntityId) {
        class FetchPatientVisitsTask extends AsyncTask<Void, Void, List<Visit>> {

            @Override
            protected List<Visit> doInBackground(Void... voids) {
                return getVisits();
            }

            @Override
            protected void onPostExecute(List<Visit> visits) {
                patientVisitList.setAdapter(new CovidPatientVisitAdapter(visits));
            }
        }

        new FetchPatientVisitsTask().execute();
    }

    // todo: replace with db call
    private List<Visit> getVisits() {
        List<Visit> visits = new ArrayList<>();
        visits.add(new Visit("visit1", "date1"));
        visits.add(new Visit("visit2", "date2"));
        visits.add(new Visit("visit3", "date3"));
        visits.add(new Visit("visit4", "date4"));
        visits.add(new Visit("visit5", "date5"));
        visits.add(new Visit("visit6", "date6"));
        return visits;
    }
}