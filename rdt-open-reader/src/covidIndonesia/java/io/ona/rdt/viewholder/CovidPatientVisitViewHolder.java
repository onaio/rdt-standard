package io.ona.rdt.viewholder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;

/**
 * Created by Vincent Karuri on 19/08/2020
 */
public class CovidPatientVisitViewHolder extends RecyclerView.ViewHolder {

    private TextView tvVisitName;
    private TextView tvDateOfVisit;

    public CovidPatientVisitViewHolder(@NonNull View itemView, View.OnClickListener onClickListener) {
        super(itemView);
        tvVisitName = itemView.findViewById(R.id.visit_name);
        tvDateOfVisit = itemView.findViewById(R.id.date_of_visit);
        itemView.findViewById(R.id.btn_go_to_visit_history).setOnClickListener(onClickListener);
    }

    public TextView getTvVisitName() {
        return tvVisitName;
    }

    public void setTvVisitName(TextView tvVisitName) {
        this.tvVisitName = tvVisitName;
    }

    public TextView getTvDateOfVisit() {
        return tvDateOfVisit;
    }

    public void setTvDateOfVisit(TextView tvDateOfVisit) {
        this.tvDateOfVisit = tvDateOfVisit;
    }
}
