package io.ona.rdt.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.adapter.CovidPatientHistoryAdapter;
import io.ona.rdt.domain.PatientHistoryEntry;


public class CovidPatientHistoryFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLayout = DataBindingUtil.inflate(inflater, R.layout.fragment_covid_patient_history,
                container, false).getRoot();
        populatePatientHistory(rootLayout);
        return rootLayout;
    }

    private void populatePatientHistory(View rootLayout) {
        fetchAndPopulateHistory(rootLayout, R.id.patient_rdt_history_section);
        fetchAndPopulateHistory(rootLayout, R.id.patient_samples_history_section);
        fetchAndPopulateHistory(rootLayout, R.id.patient_supporting_investigations_history_section);
        fetchAndPopulateHistory(rootLayout, R.id.patient_symptoms_history_section);
    }

    private void fetchAndPopulateHistory(View rootLayout, int layoutId) {
        RecyclerView patientHistoryEntryList = getPatientHistoryEntryList(rootLayout.findViewById(layoutId));
        patientHistoryEntryList.setHasFixedSize(true);
        patientHistoryEntryList.setLayoutManager(new LinearLayoutManager(getContext()));
        fetchHistory(patientHistoryEntryList, "", "", ""); // todo populate the params here
    }

    private RecyclerView getPatientHistoryEntryList(View view) {
        return view.findViewById(R.id.patient_history_entries);
    }

    private void fetchHistory(RecyclerView patientHistoryEntryList, String baseEntityId, String date, String eventType) {
        class FetchPatientHistoryTask extends AsyncTask<Void, Void, List<PatientHistoryEntry>> {

            @Override
            protected List<PatientHistoryEntry> doInBackground(Void... voids) {
                // todo: remove this and replace with db call
                List<PatientHistoryEntry> patientHistoryEntries = getPatientHistoryEntries();
                return patientHistoryEntries;
            }

            @Override
            protected void onPostExecute(List<PatientHistoryEntry> patientHistoryEntries) {
                patientHistoryEntryList.setAdapter(new CovidPatientHistoryAdapter(patientHistoryEntries));
            }
        }
        new FetchPatientHistoryTask().execute();
    }

    // todo: remove this and replace with db call
    private List<PatientHistoryEntry> getPatientHistoryEntries() {
        List<PatientHistoryEntry> patientHistoryEntries = new ArrayList<>();
        patientHistoryEntries.add(new PatientHistoryEntry("key1", "value1"));
        patientHistoryEntries.add(new PatientHistoryEntry("key2", "value2"));
        patientHistoryEntries.add(new PatientHistoryEntry("key3", "value3"));
        patientHistoryEntries.add(new PatientHistoryEntry("key4", "value4"));
        patientHistoryEntries.add(new PatientHistoryEntry("key5", "value5"));
        patientHistoryEntries.add(new PatientHistoryEntry("key6", "value6"));
        patientHistoryEntries.add(new PatientHistoryEntry("key7", "value7"));
        patientHistoryEntries.add(new PatientHistoryEntry("key8", "value8"));
        patientHistoryEntries.add(new PatientHistoryEntry("key9", "value9"));
        patientHistoryEntries.add(new PatientHistoryEntry("key10", "value10"));
        return patientHistoryEntries;
    }
}