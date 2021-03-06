package io.ona.rdt.viewholder;

import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.R;

public class CovidPatientVisitViewHolderTest extends PowerMockTest {

    private CovidPatientVisitViewHolder viewHolder;
    private final String txtVisitName = "first visit";
    private final String txtDateOfVisit = "02-05-2021";
    private View itemView;
    private TextView tvVisitName;
    private TextView tvDateOfVisit;

    @Before
    public void setUp() {

        itemView = Mockito.mock(View.class);
        tvVisitName = Mockito.mock(TextView.class);
        tvDateOfVisit = Mockito.mock(TextView.class);
        View btnVisitHistory = Mockito.mock(View.class);

        Mockito.when(itemView.findViewById(Mockito.eq(R.id.visit_name))).thenReturn(tvVisitName);
        Mockito.when(itemView.findViewById(Mockito.eq(R.id.date_of_visit))).thenReturn(tvDateOfVisit);
        Mockito.when(itemView.findViewById(Mockito.eq(R.id.btn_go_to_visit_history))).thenReturn(btnVisitHistory);
        Mockito.when(tvVisitName.getText()).thenReturn(txtVisitName);
        Mockito.when(tvDateOfVisit.getText()).thenReturn(txtDateOfVisit);

        viewHolder = new CovidPatientVisitViewHolder(itemView, Mockito.mock(View.OnClickListener.class));
    }

    @Test
    public void testGetTvVisitNameShouldReturnValidViewAndData() {
        viewHolder.setTvVisitName(tvVisitName);

        Assert.assertNotNull(viewHolder.getTvVisitName());
        Assert.assertEquals(txtVisitName, viewHolder.getTvVisitName().getText());
    }

    @Test
    public void testGetTvDateOfVisitShouldReturnValidViewAndData() {
        viewHolder.setTvDateOfVisit(tvDateOfVisit);

        Assert.assertNotNull(viewHolder.getTvDateOfVisit());
        Assert.assertEquals(txtDateOfVisit, viewHolder.getTvDateOfVisit().getText());
    }

    @Test
    public void testGetPatientVisitRowShouldReturnNonNullView() {
        viewHolder.setPatientVisitRow(itemView);

        Assert.assertNotNull(viewHolder.getPatientVisitRow());
    }
}
