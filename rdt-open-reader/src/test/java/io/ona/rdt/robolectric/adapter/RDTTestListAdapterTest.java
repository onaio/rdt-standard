package io.ona.rdt.robolectric.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
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
import io.ona.rdt.viewholder.RDTTestViewHolder;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.Test.POSITIVE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 07/08/2020
 */
public class RDTTestListAdapterTest extends RobolectricTest {

    private RDTTestListAdapter listAdapter;
    private List<RDTTestDetails> rdtTestDetailsList;
    private PatientProfileActivity patientProfileActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        init();
    }

    @Test
    public void testAdapterConstructionShouldCorrectlyBootstrapAllComponents() {
       Context context = ReflectionHelpers.getField(listAdapter, "context");
       assertEquals(patientProfileActivity, context);
       assertEquals(patientProfileActivity.getResources(), ReflectionHelpers.getField(listAdapter, "resources"));
       assertEquals(rdtTestDetailsList, ReflectionHelpers.getField(listAdapter, "rdtTestDetails"));
    }

    @Test
    public void testClickTestProfileButtonShouldLaunchTestsProfileFragment() {
        View rootLayout = LayoutInflater.from(RuntimeEnvironment.application)
                .inflate(R.layout.rdt_test_row, null);
        RDTTestViewHolder viewHolder = new RDTTestViewHolder(rootLayout);
        listAdapter.onBindViewHolder(viewHolder, 0);
        viewHolder.btnGoToTestProfile.performClick();
        verify(patientProfileActivity).replaceFragment(any(Fragment.class), anyBoolean());
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
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), viewHolder.rdtId.getText());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTTestDate(), viewHolder.testDate.getText());
        assertEquals(String.format("%s: ", expectedFormattedRDTTestDetails.getFormattedRDTType()), viewHolder.rdtType.getText());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedTestResults(), viewHolder.rdtResult.getText());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), viewHolder.rdtId.getText());

        FormattedRDTTestDetails actualFormattedRDTTestDetails = (FormattedRDTTestDetails) viewHolder.btnGoToTestProfile.getTag(R.id.rdt_test_details);
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTTestDate(), actualFormattedRDTTestDetails.getFormattedRDTTestDate());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTType(), actualFormattedRDTTestDetails.getFormattedRDTType());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedRDTId(), actualFormattedRDTTestDetails.getFormattedRDTId());
        assertEquals(expectedFormattedRDTTestDetails.getFormattedTestResults(), actualFormattedRDTTestDetails.getFormattedTestResults());
        assertEquals(expectedFormattedRDTTestDetails.getTestResult(), actualFormattedRDTTestDetails.getTestResult());
    }

    private void init() {
        rdtTestDetailsList = new ArrayList<>();
        RDTTestDetails rdtTestDetails = new RDTTestDetails();
        rdtTestDetails.setTestResult(POSITIVE);

        rdtTestDetails.setDate("2020-04-30");
        rdtTestDetails.setRdtId("rdt_id");
        rdtTestDetails.setRdtType("rdt_type");

        List<String> parasiteTypes = new ArrayList<>();
        parasiteTypes.add("ovale");
        parasiteTypes.add("gameto");
        rdtTestDetails.setParasiteTypes(parasiteTypes);
        rdtTestDetailsList.add(rdtTestDetails);

        rdtTestDetails = new RDTTestDetails();
        rdtTestDetails.setTestResult(POSITIVE);
        rdtTestDetails.setDate("2020-04-30");
        rdtTestDetails.setRdtId("rdt_id2");
        rdtTestDetails.setRdtType("rdt_type2");

        parasiteTypes = new ArrayList<>();
        parasiteTypes.add("ovale2");
        parasiteTypes.add("gameto2");
        rdtTestDetails.setParasiteTypes(parasiteTypes);
        rdtTestDetailsList.add(rdtTestDetails);

        Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
        Intent intent = new Intent();
        intent.putExtra(PATIENT, patient);

        patientProfileActivity =
                spy(Robolectric.buildActivity(PatientProfileActivity.class, intent)
                        .create()
                        .resume()
                        .get());
        doNothing().when(patientProfileActivity).replaceFragment(any(Fragment.class), anyBoolean());

        listAdapter = new RDTTestListAdapter(patientProfileActivity, rdtTestDetailsList);
    }
}
