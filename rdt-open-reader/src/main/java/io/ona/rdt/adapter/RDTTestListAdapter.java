package io.ona.rdt.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.viewholder.RDTTestViewHolder;

/**
 * Created by Vincent Karuri on 16/01/2020
 */
public class RDTTestListAdapter extends RecyclerView.Adapter<RDTTestViewHolder> {

    private List<RDTTestDetails> rdtTestDetails;

    public RDTTestListAdapter(List<RDTTestDetails> rdtTestDetails) {
        this.rdtTestDetails = rdtTestDetails;
    }

    @NonNull
    @Override
    public RDTTestViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootLayout = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.rdt_test_row, viewGroup, false);

        return new RDTTestViewHolder(rootLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RDTTestViewHolder rdtTestViewHolder, int i) {
        RDTTestDetails rdtTestDetails = this.rdtTestDetails.get(i);
        String testResults = StringUtils.join(rdtTestDetails.getTestResult(), ",");
        rdtTestViewHolder.rdtId.setText(rdtTestDetails.getRdtId());
        rdtTestViewHolder.testDate.setText(rdtTestDetails.getDate());
        rdtTestViewHolder.rdtType.setText(rdtTestDetails.getRdtType());
        rdtTestViewHolder.rdtResult.setText(testResults);
    }

    @Override
    public int getItemCount() {
        return rdtTestDetails.size();
    }
}
