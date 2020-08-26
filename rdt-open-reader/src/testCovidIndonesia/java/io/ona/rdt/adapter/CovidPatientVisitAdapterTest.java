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
import io.ona.rdt.domain.Visit;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

@PrepareForTest(LayoutInflater.class)
@RunWith(PowerMockRunner.class)
public class CovidPatientVisitAdapterTest {

    private static final String VISIT_NAME = "name_of_visit";
    private static final String VISIT_DATE = "26-08-2020";

    private CovidPatientVisitAdapter adapter;
    private List<Visit> list = new ArrayList<>();

    @Before
    public void setUp() {
        list.add(new Visit(VISIT_NAME, VISIT_DATE));
        adapter = new CovidPatientVisitAdapter(list);
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

        CovidPatientVisitViewHolder viewHolder = adapter.onCreateViewHolder(viewGroup, R.layout.covid_patient_visit_row);
        Assert.assertNotNull(viewHolder);
    }

    @Test
    public void onBindViewHolderShouldVerify() {
        CovidPatientVisitViewHolder holder = PowerMockito.mock(CovidPatientVisitViewHolder.class);
        TextView keyTextView = PowerMockito.mock(TextView.class);
        TextView valTextView = PowerMockito.mock(TextView.class);

        PowerMockito.when(holder.getTvVisitName()).thenReturn(keyTextView);
        PowerMockito.when(holder.getTvDateOfVisit()).thenReturn(valTextView);

        adapter.onBindViewHolder(holder, 0);

        Mockito.verify(keyTextView, Mockito.times(1)).setText(VISIT_NAME);
        Mockito.verify(valTextView, Mockito.times(1)).setText(VISIT_DATE);
    }

    @Test
    public void getItemCountShouldReturnOne() {
        Assert.assertEquals(1, adapter.getItemCount());
    }
}
