package io.ona.rdt.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.fragment.TestsProfileFragment;
import io.ona.rdt.util.Utils;
import io.ona.rdt.viewholder.RDTTestViewHolder;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.RDTType.ONA_RDT;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static io.ona.rdt.util.Constants.Test.RDT_TEST_DETAILS;
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
        FormattedRDTTestDetails formattedRDTTestDetails = getFormattedRDTTestDetails(rdtTestDetails);
        rdtTestViewHolder.rdtId.setText(formattedRDTTestDetails.getFormattedRDTId());
        rdtTestViewHolder.testDate.setText(formattedRDTTestDetails.getFormattedRDTTestDate());
        rdtTestViewHolder.rdtType.setText(formattedRDTTestDetails.getFormattedRDTType());
        rdtTestViewHolder.rdtResult.setText(formattedRDTTestDetails.getFormattedTestResults());
        rdtTestViewHolder.btnGoToTestProfile.setTag(R.id.rdt_test_details, formattedRDTTestDetails);

        rdtTestViewHolder.btnGoToTestProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestsProfileFragment testsProfileFragment = new TestsProfileFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable(RDT_TEST_DETAILS, (FormattedRDTTestDetails) v.getTag(R.id.rdt_test_details));
                testsProfileFragment.setArguments(bundle);
                ((PatientProfileActivityContract.View) context).replaceFragment(testsProfileFragment, true);
            }
        });
    }

    private FormattedRDTTestDetails getFormattedRDTTestDetails(RDTTestDetails rdtTestDetails) {
        FormattedRDTTestDetails formattedRDTTestDetails = new FormattedRDTTestDetails();
        try {
            formattedRDTTestDetails.setFormattedRDTId(getFormattedRDTId(rdtTestDetails));
            formattedRDTTestDetails.setFormattedRDTTestDate(getFormattedRDTTestDate(rdtTestDetails));
            formattedRDTTestDetails.setFormattedRDTType(getFormattedRDTType(rdtTestDetails));
            formattedRDTTestDetails.setFormattedTestResults(getFormattedTestResults(rdtTestDetails));
            formattedRDTTestDetails.setTestResult(rdtTestDetails.getTestResult());
        } catch (ParseException e) {
            Timber.e(e);
        }
        return formattedRDTTestDetails;
    }

    private String getFormattedRDTType(RDTTestDetails rdtTestDetails) {
        return rdtTestDetails.getRdtType() == ONA_RDT ? resources.getString(R.string.open_guideline_rdt)
                : resources.getString(R.string.carestart_rdt);
    }

    private String getFormattedRDTId(RDTTestDetails rdtTestDetails) {
        return resources.getString(R.string.rdt_id_prefix) + " " + rdtTestDetails.getRdtId();
    }

    private String getFormattedRDTTestDate(RDTTestDetails rdtTestDetails) throws ParseException {
        return rdtTestDetails.getDate() == null ? ""
                : Utils.convertDate(rdtTestDetails.getDate().split(" ")[0], "yyyy-MM-dd", "dd MMM yyyy").toString();
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
