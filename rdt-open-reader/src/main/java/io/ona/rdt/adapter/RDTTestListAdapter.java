package io.ona.rdt.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.viewholder.RDTTestViewHolder;

import static io.ona.rdt.util.Constants.RDTType.ONA_RDT;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static org.apache.commons.lang3.StringUtils.capitalize;

/**
 * Created by Vincent Karuri on 16/01/2020
 */
public class RDTTestListAdapter extends RecyclerView.Adapter<RDTTestViewHolder> {

    private List<RDTTestDetails> rdtTestDetails;
    private Context context;
    private Resources resources;

    public RDTTestListAdapter(Context context, List<RDTTestDetails> rdtTestDetails) {
        this.context = context;
        this.rdtTestDetails = rdtTestDetails;
        this.resources = context.getResources();
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
        rdtTestViewHolder.rdtId.setText(getFormattedRDTId(rdtTestDetails));
        rdtTestViewHolder.testDate.setText(getFormattedRDTTestDate(rdtTestDetails));
        rdtTestViewHolder.rdtType.setText(getFormattedRDTType(rdtTestDetails));
        rdtTestViewHolder.rdtResult.setText(getFormattedTestResults(rdtTestDetails));
    }

    private String getFormattedRDTType(RDTTestDetails rdtTestDetails) {
        return rdtTestDetails.getRdtType() == ONA_RDT ? resources.getString(R.string.open_guideline_rdt)
                : resources.getString(R.string.carestart_rdt);
    }

    private String getFormattedRDTId(RDTTestDetails rdtTestDetails) {
        return resources.getString(R.string.rdt_id_prefix) + " " + rdtTestDetails.getRdtId();
    }

    private String getFormattedRDTTestDate(RDTTestDetails rdtTestDetails) {
        return rdtTestDetails.getDate() == null ? "" : rdtTestDetails.getDate().split(" ")[0];
    }

    private String getFormattedTestResults(RDTTestDetails rdtTestDetails) {
        if (!POSITIVE.equals(rdtTestDetails.getTestResult())) {
            return rdtTestDetails.getTestResult();
        }

        String testResults = "";
        for (String testResult : rdtTestDetails.getParasiteTypes()) {
            testResults += capitalize(testResult) + " positive,";
        }
        return testResults.substring(0, testResults.length() - 1);
    }

    @Override
    public int getItemCount() {
        return rdtTestDetails.size();
    }
}
