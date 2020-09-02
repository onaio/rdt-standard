package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientHistoryActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.presenter.CovidPatientHistoryActivityPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

/**
 * Created by Vincent Karuri on 18/08/2020
 */
public class CovidPatientHistoryActivityTest extends ActivityRobolectricTest {

    private CovidPatientHistoryActivity covidPatientHistoryActivity;
    private ActivityController<CovidPatientHistoryActivity> activityController;
    private final String ENTITY_ID = "entity_id";
    private final String DATE = "date";

    @Before
    public void setUp() {
        Intent intent = new Intent();
        final int ten = 10;
        intent.putExtra(Constants.FormFields.PATIENT, new Patient("name", "sex", ENTITY_ID, "patient_id", ten));
        intent.putExtra(Constants.FormFields.PATIENT_VISIT_DATE, DATE);
        covidPatientHistoryActivity = Robolectric.buildActivity(CovidPatientHistoryActivity.class, intent)
                .create().resume().get();
    }

    @Test
    public void testOnCreateViewShouldCorrectlyPopulatePatientHistorySections() {
        CovidPatientHistoryActivityPresenter presenter = Mockito.mock(CovidPatientHistoryActivityPresenter.class);

        List<PatientHistoryEntry> patientHistoryEntries = new ArrayList<>();
        PatientHistoryEntry patientHistoryEntry = new PatientHistoryEntry("key1", "value1");
        patientHistoryEntries.add(patientHistoryEntry);
        patientHistoryEntry = new PatientHistoryEntry("key2", "value2");
        patientHistoryEntries.add(patientHistoryEntry);
        patientHistoryEntry = new PatientHistoryEntry("key3", "value3");
        patientHistoryEntries.add(patientHistoryEntry);
        Mockito.doReturn(patientHistoryEntries).when(presenter).getPatientHistoryEntries(ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        ReflectionHelpers.setField(covidPatientHistoryActivity, "presenter", presenter);
        ReflectionHelpers.callInstanceMethod(covidPatientHistoryActivity, "populatePatientHistory");

        final int firstItem = 1;
        final int secondItem = 2;
        final int thirdItem = 3;

        Mockito.verify(presenter).getPatientHistoryEntries(ArgumentMatchers.eq(ENTITY_ID),
                ArgumentMatchers.eq(CovidConstants.Encounter.COVID_RDT_TEST), ArgumentMatchers.eq(DATE));
        Mockito.verify(presenter).getPatientHistoryEntries(ArgumentMatchers.eq(ENTITY_ID),
                ArgumentMatchers.eq(CovidConstants.Encounter.SAMPLE_COLLECTION), ArgumentMatchers.eq(DATE));
        Mockito.verify(presenter).getPatientHistoryEntries(ArgumentMatchers.eq(ENTITY_ID),
                ArgumentMatchers.eq(CovidConstants.Encounter.SUPPORT_INVESTIGATION), ArgumentMatchers.eq(DATE));
        Mockito.verify(presenter).getPatientHistoryEntries(ArgumentMatchers.eq(ENTITY_ID),
                ArgumentMatchers.eq(CovidConstants.Encounter.PATIENT_DIAGNOSTICS), ArgumentMatchers.eq(DATE));

        // rdt history section
        assertListValuesAreCorrect(firstItem, R.id.patient_rdt_history_section);
        assertListValuesAreCorrect(secondItem, R.id.patient_rdt_history_section);
        assertListValuesAreCorrect(thirdItem, R.id.patient_rdt_history_section);

        // symptoms history section
        assertListValuesAreCorrect(firstItem, R.id.patient_symptoms_history_section);
        assertListValuesAreCorrect(secondItem, R.id.patient_symptoms_history_section);
        assertListValuesAreCorrect(thirdItem, R.id.patient_symptoms_history_section);

        // supporting investigations history section
        assertListValuesAreCorrect(firstItem, R.id.patient_supporting_investigations_history_section);
        assertListValuesAreCorrect(secondItem, R.id.patient_supporting_investigations_history_section);
        assertListValuesAreCorrect(thirdItem, R.id.patient_supporting_investigations_history_section);

        // samples history section
        assertListValuesAreCorrect(firstItem, R.id.patient_samples_history_section);
        assertListValuesAreCorrect(secondItem, R.id.patient_samples_history_section);
        assertListValuesAreCorrect(thirdItem, R.id.patient_samples_history_section);
    }

    private void assertListValuesAreCorrect(int position, int layoutId) {
        RecyclerView historyEntries = covidPatientHistoryActivity.findViewById(layoutId)
                .findViewById(R.id.patient_history_entries);

        final int zero = 0;
        final int hundred = 100;
        final int thousand = 1000;
        historyEntries.measure(zero, zero);
        historyEntries.layout(zero, zero, hundred, thousand);

        CovidPatientHistoryViewHolder viewHolder = (CovidPatientHistoryViewHolder) historyEntries
                .findViewHolderForAdapterPosition(position - 1);
        Assert.assertEquals(String.format("value%d", position), viewHolder.getTvHistoryValue().getText());
        Assert.assertEquals(String.format("key%d", position), viewHolder.getTvHistoryKey().getText());
    }

    @Override
    public Activity getActivity() {
        return covidPatientHistoryActivity;
    }
}
