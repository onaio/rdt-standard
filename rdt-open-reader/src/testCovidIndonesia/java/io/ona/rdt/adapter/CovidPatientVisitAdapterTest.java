package io.ona.rdt.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

public class CovidPatientVisitAdapterTest extends RobolectricTest {

    private final String visitName = "First Visit";
    private final String visitDate = "12-15-2020";

    private CovidPatientVisitAdapter adapter;
    private List<Visit> list;
    private ViewGroup parent;

    @Before
    public void setUp() {

        list = new ArrayList<>();
        list.add(new Visit(visitName, visitDate));

        Context context = RDTApplication.getInstance();
        parent = Mockito.spy(ViewGroup.class);

        Mockito.when(parent.getContext()).thenReturn(context);

        adapter = new CovidPatientVisitAdapter(list, Mockito.mock(View.OnClickListener.class));
    }

    @Test
    public void testOnCreateViewHolder() {
        CovidPatientVisitViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        Assert.assertNotNull(viewHolder.getTvVisitName());
        Assert.assertNotNull(viewHolder.getTvDateOfVisit());
        Assert.assertNotNull(viewHolder.getPatientVisitRow());
    }

    @Test
    public void testOnBindViewHolder() {
        CovidPatientVisitViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        Assert.assertEquals(visitName, viewHolder.getTvVisitName().getText());
        Assert.assertEquals(visitDate, viewHolder.getTvDateOfVisit().getText());
        Assert.assertEquals(visitDate, viewHolder.getPatientVisitRow().getTag(R.id.patient_visit_date).toString());
        Assert.assertEquals(visitDate, viewHolder.getPatientVisitRow().findViewById(R.id.btn_go_to_visit_history).getTag(R.id.patient_visit_date).toString());
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(list.size(), adapter.getItemCount());
    }
}
