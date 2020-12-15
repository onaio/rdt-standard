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
import io.ona.rdt.R;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

public class CovidPatientVisitAdapterTest extends PowerMockTest {

    private final String visitName = "First Visit";
    private final String visitDate = "12-15-2020";

    private CovidPatientVisitAdapter adapter;
    private List<Visit> list;

    @Before
    public void setUp() {

        list = new ArrayList<>();
        list.add(new Visit(visitName, visitDate));

        adapter = new CovidPatientVisitAdapter(list, Mockito.mock(View.OnClickListener.class));
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

        CovidPatientVisitViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        Assert.assertNotNull(viewHolder.getTvVisitName());
    }

    @Test
    public void testOnBindViewHolder() {

        CovidPatientVisitViewHolder viewHolder = Mockito.mock(CovidPatientVisitViewHolder.class);
        View parentVisitRow = Mockito.mock(View.class);
        View visitHistory = Mockito.mock(View.class);
        TextView tvVisitName = Mockito.mock(TextView.class);
        TextView tvVisitDate = Mockito.mock(TextView.class);

        Mockito.when(viewHolder.getTvVisitName()).thenReturn(tvVisitName);
        Mockito.when(viewHolder.getTvDateOfVisit()).thenReturn(tvVisitDate);
        Mockito.when(viewHolder.getPatientVisitRow()).thenReturn(parentVisitRow);
        Mockito.when(parentVisitRow.findViewById(Mockito.eq(R.id.btn_go_to_visit_history))).thenReturn(visitHistory);

        adapter.onBindViewHolder(viewHolder, 0);

        Mockito.verify(tvVisitName).setText(visitName);
        Mockito.verify(tvVisitDate).setText(visitDate);
        Mockito.verify(parentVisitRow).setTag(R.id.patient_visit_date, visitDate);
        Mockito.verify(visitHistory).setTag(R.id.patient_visit_date, visitDate);
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(list.size(), adapter.getItemCount());
    }
}
