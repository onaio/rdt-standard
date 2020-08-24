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

import io.ona.rdt.R;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LayoutInflater.class)
public class CovidPatientHistoryAdapterTest {

    private static final String PATIENT_NAME_KEY = "patient_name";
    private static final String PATIENT_NAME_VAL = "Jane";

    private CovidPatientHistoryAdapter adapter;
    private List<PatientHistoryEntry> list = new ArrayList<>();

    @Before
    public void setUp() {
        list.add(new PatientHistoryEntry(PATIENT_NAME_KEY, PATIENT_NAME_VAL));
        adapter = new CovidPatientHistoryAdapter(list);
    }

    @After
    public void tearDown() {
        adapter = null;
        list = null;
    }

    @Test
    public void onCreateViewHolderShouldReturnNewInstance() {

        PowerMockito.mockStatic(LayoutInflater.class);

        ViewGroup viewGroup = PowerMockito.mock(ViewGroup.class);
        Context context = PowerMockito.mock(Context.class);
        View view = PowerMockito.mock(View.class);
        LayoutInflater inflater = PowerMockito.mock(LayoutInflater.class);

        PowerMockito.when(viewGroup.getContext()).thenReturn(context);
        PowerMockito.when(LayoutInflater.from(ArgumentMatchers.any(Context.class))).thenReturn(inflater);
        PowerMockito.when(inflater.inflate(ArgumentMatchers.anyInt(), ArgumentMatchers.any(ViewGroup.class), ArgumentMatchers.anyBoolean())).thenReturn(view);

        CovidPatientHistoryViewHolder viewHolder = adapter.onCreateViewHolder(viewGroup, R.layout.covid_patient_history_row);
        Assert.assertNotNull(viewHolder);
    }

    @Test
    public void onBindViewHolderShouldVerify() {
        CovidPatientHistoryViewHolder holder = PowerMockito.mock(CovidPatientHistoryViewHolder.class);
        TextView keyTextView = PowerMockito.mock(TextView.class);
        TextView valTextView = PowerMockito.mock(TextView.class);

        PowerMockito.when(holder.getTvHistoryKey()).thenReturn(keyTextView);
        PowerMockito.when(holder.getTvHistoryValue()).thenReturn(valTextView);

        adapter.onBindViewHolder(holder, 0);

        Mockito.verify(keyTextView, Mockito.times(1)).setText("patient_name");
        Mockito.verify(valTextView, Mockito.times(1)).setText("Jane");
    }

    @Test
    public void getItemCountShouldReturnOne() {
        Assert.assertEquals(1, list.size());
    }
}
