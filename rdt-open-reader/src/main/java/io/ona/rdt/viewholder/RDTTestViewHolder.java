package io.ona.rdt.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import io.ona.rdt.R;

/**
 * Created by Vincent Karuri on 16/01/2020
 */
public class RDTTestViewHolder extends RecyclerView.ViewHolder {

    public TextView rdtId;
    public TextView testDate;
    public TextView rdtType;
    public TextView rdtResult;

    public RDTTestViewHolder(@NonNull View rdtTestRow) {
        super(rdtTestRow);
        this.rdtId = rdtTestRow.findViewById(R.id.tests_profile_rdt_id);
        this.testDate = rdtTestRow.findViewById(R.id.test_date);
        this.rdtType = rdtTestRow.findViewById(R.id.rdt_type);
        this.rdtResult = rdtTestRow.findViewById(R.id.rdt_result);
    }
}
