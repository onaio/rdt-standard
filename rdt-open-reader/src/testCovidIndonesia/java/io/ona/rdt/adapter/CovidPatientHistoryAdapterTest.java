package io.ona.rdt.adapter;

import android.content.Context;
import android.view.ViewGroup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.viewholder.CovidPatientHistoryViewHolder;

public class CovidPatientHistoryAdapterTest extends RobolectricTest {

    private final String patientKey = "key";
    private final String patientVal = "val";

    private CovidPatientHistoryAdapter adapter;
    private List<PatientHistoryEntry> list;
    private ViewGroup parent;

    @Before
    public void setUp() {

        list = new ArrayList<>();
        list.add(new PatientHistoryEntry(patientKey, patientVal));

        Context context = RDTApplication.getInstance();
        parent = Mockito.spy(ViewGroup.class);

        Mockito.when(parent.getContext()).thenReturn(context);

        adapter = new CovidPatientHistoryAdapter(list);
    }

    @Test
    public void testOnCreateViewHolder() {
        CovidPatientHistoryViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        Assert.assertNotNull(viewHolder.getTvHistoryKey());
        Assert.assertNotNull(viewHolder.getTvHistoryValue());
    }

    @Test
    public void testOnBindViewHolder() {
        CovidPatientHistoryViewHolder viewHolder = adapter.onCreateViewHolder(parent, 0);

        adapter.onBindViewHolder(viewHolder, 0);

        Assert.assertEquals(patientKey, viewHolder.getTvHistoryKey().getText());
        Assert.assertEquals(patientVal, viewHolder.getTvHistoryValue().getText());
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(list.size(), adapter.getItemCount());
    }
}
