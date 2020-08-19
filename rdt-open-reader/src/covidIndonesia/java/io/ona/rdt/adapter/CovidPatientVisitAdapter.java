package io.ona.rdt.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

/**
 * Created by Vincent Karuri on 19/08/2020
 */
public class CovidPatientVisitAdapter extends RecyclerView.Adapter<CovidPatientVisitViewHolder> {

    private List<Visit> patientVisits;

    public CovidPatientVisitAdapter(List<Visit> patientVisits) {
        this.patientVisits = patientVisits;
    }

    @NonNull
    @Override
    public CovidPatientVisitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootLayout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.covid_patient_visit_row, parent, false);
        return new CovidPatientVisitViewHolder(rootLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidPatientVisitViewHolder holder, int position) {
        Visit visit = patientVisits.get(position);
        holder.getTvVisitName().setText(visit.getVisitName());
        holder.getTvDateOfVisit().setText(visit.getDateOfVisit());
    }

    @Override
    public int getItemCount() {
        return patientVisits.size();
    }
}
