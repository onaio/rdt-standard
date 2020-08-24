package io.ona.rdt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(LayoutInflater.class)
public class CovidPatientHistoryAdapterTest {

    private CovidPatientHistoryAdapter adapter;
    private List<PatientHistoryEntry> list = new ArrayList<>();

    @Before
    public void setUp() {
        list.add(new PatientHistoryEntry("patient_name", "Jane"));
        adapter = new CovidPatientHistoryAdapter(list);
    }

    @After
    public void tearDown() {
        adapter = null;
        list = null;
    }

    @Test
    public void onCreateViewHolderShouldReturnNewInstance() {

        mockStatic(LayoutInflater.class);

        ViewGroup viewGroup = mock(ViewGroup.class);
        Context context = mock(Context.class);
        View view = mock(View.class);
        LayoutInflater inflater = mock(LayoutInflater.class);

        when(viewGroup.getContext()).thenReturn(context);
        when(LayoutInflater.from(any(Context.class))).thenReturn(inflater);
        when(inflater.inflate(anyInt(), any(ViewGroup.class), anyBoolean())).thenReturn(view);

        CovidPatientHistoryViewHolder viewHolder = adapter.onCreateViewHolder(viewGroup, R.layout.covid_patient_history_row);
        assertNotNull(viewHolder);
    }

    @Test
    public void onBindViewHolderShouldVerify() {
        CovidPatientHistoryViewHolder holder = mock(CovidPatientHistoryViewHolder.class);
        TextView keyTextView = mock(TextView.class);
        TextView valTextView = mock(TextView.class);

        when(holder.getTvHistoryKey()).thenReturn(keyTextView);
        when(holder.getTvHistoryValue()).thenReturn(valTextView);

        adapter.onBindViewHolder(holder, 0);

        verify(keyTextView, Mockito.times(1)).setText("patient_name");
        verify(valTextView, Mockito.times(1)).setText("Jane");
    }

    @Test
    public void getItemCountShouldReturnOne() {
        assertEquals(1, list.size());
    }
}
