package io.ona.rdt.viewholder;

import android.view.View;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.R;

public class CovidPatientHistoryViewHolderTest extends PowerMockTest {

    private CovidPatientHistoryViewHolder covidPatientHistoryViewHolder;

    @Before
    public void setUp() {
        View itemView = Mockito.mock(View.class);
        TextView tvHistoryKey = Mockito.mock(TextView.class);
        TextView tvHistoryValue = Mockito.mock(TextView.class);

        Mockito.when(itemView.findViewById(Mockito.eq(R.id.tv_history_key))).thenReturn(tvHistoryKey);
        Mockito.when(itemView.findViewById(Mockito.eq(R.id.tv_history_value))).thenReturn(tvHistoryValue);

        covidPatientHistoryViewHolder = new CovidPatientHistoryViewHolder(itemView);
    }

    @Test
    public void testGetTvHistoryKey() {
        Assert.assertNotNull(covidPatientHistoryViewHolder.getTvHistoryKey());
    }

    @Test
    public void testGetTvHistoryValue() {
        Assert.assertNotNull(covidPatientHistoryViewHolder.getTvHistoryValue());
    }
}
