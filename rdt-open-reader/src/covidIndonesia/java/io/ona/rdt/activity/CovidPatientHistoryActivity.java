package io.ona.rdt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vijay.jsonwizard.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.adapter.CovidPatientHistoryAdapter;
import io.ona.rdt.contract.CovidPatientHistoryActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.presenter.CovidPatientHistoryActivityPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidPatientHistoryActivity extends AppCompatActivity implements CovidPatientHistoryActivityContract.View {

    private Map<Integer, String> patientHistorySectionsMap = new HashMap<>();

    private Patient currPatient;
    private CovidPatientHistoryActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        io.ona.rdt.util.Utils.updateLocale(this);

        super.onCreate(savedInstanceState);

        DataBindingUtil.setContentView(this, R.layout.activity_covid_patient_history);

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
            patientHistorySectionsMap.put(R.id.patient_symptoms_history_section,
                    CovidConstants.Encounter.PATIENT_DIAGNOSTICS);
            patientHistorySectionsMap.put(R.id.patient_x_ray_history_section,
                    CovidConstants.Encounter.COVID_XRAY);
            patientHistorySectionsMap.put(R.id.patient_white_blood_cell_count_history_section,
                    CovidConstants.Encounter.COVID_WBC);
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

    private void fetchHistory(RecyclerView patientHistoryEntryList, String eventType, String date,
                              TextView tvNoDataAvailable) {
        class FetchPatientHistoryTask extends AsyncTask<Void, Void, List<PatientHistoryEntry>> {

            @Override
            protected void onPreExecute() {
                Utils.showProgressDialog(R.string.please_wait_title, R.string.please_wait_message, CovidPatientHistoryActivity.this);
            }

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
                Utils.hideProgressDialog();
            }
        }

        new FetchPatientHistoryTask().execute();
    }
}
