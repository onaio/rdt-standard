package io.ona.rdt.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

public class CovidPatientHistoryAdapterTest extends PowerMockTest {

    private final String patientKey = "key";
    private final String patientVal = "val";

    private CovidPatientHistoryAdapter adapter;
    private List<PatientHistoryEntry> list;

    @Before
    public void setUp() {

        list = new ArrayList<>();
        list.add(new PatientHistoryEntry(patientKey, patientVal));

        adapter = new CovidPatientHistoryAdapter(list);
    }

    @Test
    @PrepareForTest(LayoutInflater.class)
    public void testOnCreateViewHolder() {

        PowerMockito.mockStatic(LayoutInflater.class);

        Context context = Mockito.mock(Context.class);
        View view = Mockito.mock(View.class);
        LayoutInflater layoutInflater = Mockito.mock(LayoutInflater.class);
        ViewGroup parent = Mockito.mock(ViewGroup.class);
        TextView textView = Mockito.mock(TextView.class);

        Mockito.when(parent.getContext()).thenReturn(context);
        Mockito.when(LayoutInflater.from(ArgumentMatchers.any(Context.class))).thenReturn(layoutInflater);
        Mockito.when(layoutInflater.inflate(ArgumentMatchers.anyInt(), ArgumentMatchers.any(ViewGroup.class), ArgumentMatchers.anyBoolean())).thenReturn(view);
        Mockito.when(view.findViewById(ArgumentMatchers.anyInt())).thenReturn(textView);

        CovidPatientHistoryViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        Assert.assertNotNull(viewHolder.getTvHistoryKey());
    }

    @Test
    public void testOnBindViewHolder() {

        CovidPatientHistoryViewHolder viewHolder = Mockito.mock(CovidPatientHistoryViewHolder.class);
        TextView tvKey = Mockito.mock(TextView.class);
        TextView tvVal = Mockito.mock(TextView.class);

        Mockito.when(viewHolder.getTvHistoryKey()).thenReturn(tvKey);
        Mockito.when(viewHolder.getTvHistoryValue()).thenReturn(tvVal);

        adapter.onBindViewHolder(viewHolder, 0);

        Mockito.verify(tvKey).setText(patientKey);
        Mockito.verify(tvVal).setText(patientVal);
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(list.size(), adapter.getItemCount());
    }
}
