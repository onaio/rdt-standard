package io.ona.rdt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

@RunWith(PowerMockRunner.class)
@PrepareForTest({LayoutInflater.class})
public class CovidPatientHistoryAdapterTest {

    private static final String PATIENT_KEY = "patient_key";
    private static final String PATIENT_VAL = "patient_val";

    private CovidPatientHistoryAdapter covidPatientHistoryAdapter;
    private List<PatientHistoryEntry> patientHistoryEntries;

    @Before
    public void setUp() {
        patientHistoryEntries = new ArrayList<>();
        patientHistoryEntries.add(new PatientHistoryEntry(PATIENT_KEY, PATIENT_VAL));
        covidPatientHistoryAdapter = new CovidPatientHistoryAdapter(patientHistoryEntries);
    }

    @After
    public void tearDown() {

    }

    @Test
    public void testOnCreateViewHolderShouldReturnViewHolder() throws Exception {
        PowerMockito.mockStatic(LayoutInflater.class);
        LayoutInflater inflater = PowerMockito.mock(LayoutInflater.class);
        Context context = PowerMockito.mock(Context.class);
        ViewGroup viewGroup = PowerMockito.mock(ViewGroup.class);
        View view = PowerMockito.mock(View.class);

        PowerMockito.doReturn(context).when(viewGroup).getContext();
        PowerMockito.when(LayoutInflater.from(ArgumentMatchers.any(Context.class))).thenReturn(inflater);
        PowerMockito.when(inflater.inflate(ArgumentMatchers.anyInt(), ArgumentMatchers.any(ViewGroup.class), ArgumentMatchers.anyBoolean())).thenReturn(view);

        Assert.assertNotNull(covidPatientHistoryAdapter.onCreateViewHolder(viewGroup, 0));
    }

    @Test
    public void testOnBindViewHolderShouldVerify() {
        CovidPatientHistoryViewHolder viewHolder = PowerMockito.mock(CovidPatientHistoryViewHolder.class);
        TextView tvKey = PowerMockito.mock(TextView.class);
        TextView tvVal = PowerMockito.mock(TextView.class);

        PowerMockito.when(viewHolder.getTvHistoryKey()).thenReturn(tvKey);
        PowerMockito.when(viewHolder.getTvHistoryValue()).thenReturn(tvVal);

        covidPatientHistoryAdapter.onBindViewHolder(viewHolder, 0);

        Mockito.verify(tvKey).setText(PATIENT_KEY);
        Mockito.verify(tvVal).setText(PATIENT_VAL);
    }

    @Test
    public void testGetItemCountShouldReturnOne() {
        Assert.assertEquals(covidPatientHistoryAdapter.getItemCount(), 1);
    }
}
