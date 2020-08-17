package io.ona.rdt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

/**
 * Created by Vincent Karuri on 17/08/2020
 */
public class CovidPatientHistoryAdapter extends RecyclerView.Adapter<CovidPatientHistoryViewHolder> {

    private List<PatientHistoryEntry> patientHistoryEntries;

    public CovidPatientHistoryAdapter(List<PatientHistoryEntry> patientHistoryEntries) {
        this.patientHistoryEntries = patientHistoryEntries;
    }

    @NonNull
    @Override
    public CovidPatientHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.covid_patient_history_row, parent, false);
        return new CovidPatientHistoryViewHolder(rootLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidPatientHistoryViewHolder holder, int position) {
        PatientHistoryEntry patientHistoryEntry = patientHistoryEntries.get(position);
        holder.getTvHistoryKey().setText(patientHistoryEntry.getKey());
        holder.getTvHistoryValue().setText(patientHistoryEntry.getValue());
    }

    @Override
    public int getItemCount() {
        return patientHistoryEntries.size();
    }
}
