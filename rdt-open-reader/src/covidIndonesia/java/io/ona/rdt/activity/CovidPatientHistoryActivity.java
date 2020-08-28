package io.ona.rdt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.adapter.CovidPatientHistoryAdapter;
import io.ona.rdt.contract.CovidPatientHistoryActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.presenter.CovidPatientHistoryActivityPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

import static io.ona.rdt.util.Utils.updateLocale;

public class CovidPatientHistoryActivity extends AppCompatActivity implements CovidPatientHistoryActivityContract.View {

    private Map<Integer, String> patientHistorySectionsMap = new HashMap<>();

    private Patient currPatient;
    private CovidPatientHistoryActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        updateLocale(this);

        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this,R.layout.activity_covid_patient_history);

        currPatient = getIntent().getParcelableExtra(Constants.FormFields.PATIENT);
        presenter = new CovidPatientHistoryActivityPresenter(this);

        populatePatientHistory();
    }

    private Map<Integer, String> getPatientHistorySectionsMap() {
        if (patientHistorySectionsMap.isEmpty()) {
            patientHistorySectionsMap.put(R.id.patient_rdt_history_section,
                    CovidConstants.Encounter.COVID_RDT_TEST);
            patientHistorySectionsMap.put(R.id.patient_samples_history_section,
                    CovidConstants.Encounter.SAMPLE_COLLECTION);
            patientHistorySectionsMap.put(R.id.patient_supporting_investigations_history_section,
                    CovidConstants.Encounter.SUPPORT_INVESTIGATION);
            patientHistorySectionsMap.put(R.id.patient_symptoms_history_section,
                    CovidConstants.Encounter.PATIENT_DIAGNOSTICS);
        }
        return patientHistorySectionsMap;
    }

    private void populatePatientHistory() {
        for (Map.Entry<Integer, String> sectionIdAndEvent : getPatientHistorySectionsMap().entrySet()) {
            fetchAndPopulateHistory(sectionIdAndEvent.getKey(), sectionIdAndEvent.getValue());
        }
    }

    private void fetchAndPopulateHistory(int layoutId, String eventType) {
        View patientHistorySection = findViewById(layoutId);
        RecyclerView patientHistoryEntryList = getPatientHistoryEntryList(patientHistorySection);
        patientHistoryEntryList.setHasFixedSize(true);
        patientHistoryEntryList.setLayoutManager(new LinearLayoutManager(this));
        fetchHistory(patientHistoryEntryList, eventType,
                getIntent().getStringExtra(Constants.FormFields.PATIENT_VISIT_DATE),
                patientHistorySection.findViewById(R.id.tv_no_entries_found));
    }

    private RecyclerView getPatientHistoryEntryList(View view) {
        return view.findViewById(R.id.patient_history_entries);
    }

    private void showNoEntriesText() {

    }

    private void fetchHistory(RecyclerView patientHistoryEntryList, String eventType, String date,
                              TextView tvNoDataAvailable) {
        class FetchPatientHistoryTask extends AsyncTask<Void, Void, List<PatientHistoryEntry>> {

            @Override
            protected List<PatientHistoryEntry> doInBackground(Void... voids) {
                return presenter.getPatientHistoryEntries(currPatient.getBaseEntityId(), eventType, date);
            }

            @Override
            protected void onPostExecute(List<PatientHistoryEntry> patientHistoryEntries) {
                patientHistoryEntryList.setAdapter(new CovidPatientHistoryAdapter(patientHistoryEntries));
                if (patientHistoryEntries.isEmpty()) {
                    tvNoDataAvailable.setVisibility(View.VISIBLE);
                }
            }
        }

        new FetchPatientHistoryTask().execute();
    }
}