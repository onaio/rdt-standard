package io.ona.rdt.viewholder;

import android.view.View;
import android.widget.TextView;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import io.ona.rdt.R;

@RunWith(MockitoJUnitRunner.class)
public class CovidPatientVisitViewHolderTest {

    private CovidPatientVisitViewHolder covidPatientVisitViewHolder;

    @Mock
    private TextView tvVisitName;

    @Mock
    private TextView tvDateOfVisit;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        View itemView = Mockito.mock(View.class);

        Mockito.doReturn(tvVisitName).when(itemView).findViewById(Mockito.eq(R.id.visit_name));
        Mockito.doReturn(tvDateOfVisit).when(itemView).findViewById(Mockito.eq(R.id.date_of_visit));

        covidPatientVisitViewHolder = new CovidPatientVisitViewHolder(itemView);
    }

    @After
    public void tearDown() {
        covidPatientVisitViewHolder = null;
    }

    @Test
    public void testGetTvVisitNameShouldReturnValidTextView() {
        Assert.assertEquals(tvVisitName, covidPatientVisitViewHolder.getTvVisitName());
    }

    @Test
    public void testSetTvVisitNameShouldVerify() {
        covidPatientVisitViewHolder.setTvVisitName(tvVisitName);
        Assert.assertEquals(tvVisitName, covidPatientVisitViewHolder.getTvVisitName());
    }

    @Test
    public void testGetTvDateOfVisitShouldReturnValidTextView() {
        Assert.assertEquals(tvDateOfVisit, covidPatientVisitViewHolder.getTvDateOfVisit());
    }

    @Test
    public void testSetTvDateOfVisitShouldVerify() {
        covidPatientVisitViewHolder.setTvDateOfVisit(tvDateOfVisit);
        Assert.assertEquals(tvDateOfVisit, covidPatientVisitViewHolder.getTvDateOfVisit());
    }
}
