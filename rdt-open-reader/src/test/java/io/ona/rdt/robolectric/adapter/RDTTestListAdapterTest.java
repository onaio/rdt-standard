package io.ona.rdt.robolectric.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.adapter.RDTTestListAdapter;
import io.ona.rdt.domain.FormattedRDTTestDetails;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.RDTTestDetails;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.viewholder.RDTTestViewHolder;

/**
 * Created by Vincent Karuri on 07/08/2020
 */
public class RDTTestListAdapterTest extends RobolectricTest {

    private RDTTestListAdapter listAdapter;
    private List<RDTTestDetails> rdtTestDetailsList;
    private PatientProfileActivity patientProfileActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        init();
    }

    @After
    public void tearDown() {
        patientProfileActivity.finish();
    }

    @Test
    public void testAdapterConstructionShouldCorrectlyBootstrapAllComponents() {
        Context context = ReflectionHelpers.getField(listAdapter, "context");
        Assert.assertEquals(patientProfileActivity, context);
        Assert.assertEquals(patientProfileActivity.getResources(), ReflectionHelpers.getField(listAdapter, "resources"));
        Assert.assertEquals(rdtTestDetailsList, ReflectionHelpers.getField(listAdapter, "rdtTestDetails"));
    }

    @Test
    public void testClickTestProfileButtonShouldLaunchTestsProfileFragment() {
        View rootLayout = LayoutInflater.from(RuntimeEnvironment.application)
                .inflate(R.layout.rdt_test_row, null);
        RDTTestViewHolder viewHolder = new RDTTestViewHolder(rootLayout);
        listAdapter.onBindViewHolder(viewHolder, 0);
        viewHolder.btnGoToTestProfile.performClick();
        Mockito.verify(patientProfileActivity).replaceFragment(ArgumentMatchers.any(Fragment.class), ArgumentMatchers.anyBoolean());
    }

    @Test
    public void testOnBindViewHolderShouldCorrectlyPopulateFields() {
        View rootLayout = LayoutInflater.from(RuntimeEnvironment.application)
                .inflate(R.layout.rdt_test_row, null);
        RDTTestViewHolder viewHolder = new RDTTestViewHolder(rootLayout);
        listAdapter.onBindViewHolder(viewHolder, 0);

        RDTTestDetails rdtTestDetails = rdtTestDetailsList.get(0);
        FormattedRDTTestDetails expectedFormattedRDTTestDetails = ReflectionHelpers.callInstanceMethod(listAdapter,
                "getFormattedRDTTestDetails",
                ReflectionHelpers.ClassParameter.from(RDTTestDetails.class, rdtTestDetails));
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), viewHolder.rdtId.getText());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTTestDate(), viewHolder.testDate.getText());
        Assert.assertEquals(String.format("%s: ", expectedFormattedRDTTestDetails.getFormattedRDTType()), viewHolder.rdtType.getText());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedTestResults(), viewHolder.rdtResult.getText());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), viewHolder.rdtId.getText());

        FormattedRDTTestDetails actualFormattedRDTTestDetails = (FormattedRDTTestDetails) viewHolder.btnGoToTestProfile.getTag(R.id.rdt_test_details);
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTTestDate(), actualFormattedRDTTestDetails.getFormattedRDTTestDate());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTType(), actualFormattedRDTTestDetails.getFormattedRDTType());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), actualFormattedRDTTestDetails.getFormattedRDTId());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getFormattedTestResults(), actualFormattedRDTTestDetails.getFormattedTestResults());
        Assert.assertEquals(expectedFormattedRDTTestDetails.getTestResult(), actualFormattedRDTTestDetails.getTestResult());
    }

    private void init() {
        final String TEST_DATE = "2020-04-30";
        rdtTestDetailsList = new ArrayList<>();
        RDTTestDetails rdtTestDetails = new RDTTestDetails();
        rdtTestDetails.setTestResult(Constants.Test.POSITIVE);

        rdtTestDetails.setDate(TEST_DATE);
        rdtTestDetails.setRdtId("rdt_id");
        rdtTestDetails.setRdtType("rdt_type");

        List<String> parasiteTypes = new ArrayList<>();
        parasiteTypes.add("ovale");
        parasiteTypes.add("gameto");
        rdtTestDetails.setParasiteTypes(parasiteTypes);
        rdtTestDetailsList.add(rdtTestDetails);

        rdtTestDetails = new RDTTestDetails();
        rdtTestDetails.setTestResult(Constants.Test.POSITIVE);
        rdtTestDetails.setDate(TEST_DATE);
        rdtTestDetails.setRdtId("rdt_id2");
        rdtTestDetails.setRdtType("rdt_type2");

        parasiteTypes = new ArrayList<>();
        parasiteTypes.add("ovale2");
        parasiteTypes.add("gameto2");
        rdtTestDetails.setParasiteTypes(parasiteTypes);
        rdtTestDetailsList.add(rdtTestDetails);

        Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, patient);

        patientProfileActivity =
                Mockito.spy(Robolectric.buildActivity(PatientProfileActivity.class, intent)
                        .create()
                        .resume()
                        .get());
        Mockito.doNothing().when(patientProfileActivity).replaceFragment(ArgumentMatchers.any(Fragment.class), ArgumentMatchers.anyBoolean());

        listAdapter = new RDTTestListAdapter(patientProfileActivity, rdtTestDetailsList);
    }
}
