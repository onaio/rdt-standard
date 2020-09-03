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
public class CovidPatientHistoryViewHolderTest {

    private CovidPatientHistoryViewHolder covidPatientHistoryViewHolder;

    @Mock
    private TextView tvHistoryKey;

    @Mock
    private TextView tvHistoryVal;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        View itemView = Mockito.mock(View.class);

        Mockito.doReturn(tvHistoryKey).when(itemView).findViewById(Mockito.eq(R.id.tv_history_key));
        Mockito.doReturn(tvHistoryVal).when(itemView).findViewById(Mockito.eq(R.id.tv_history_value));

        covidPatientHistoryViewHolder = new CovidPatientHistoryViewHolder(itemView);
    }

    @After
    public void tearDown() {
        covidPatientHistoryViewHolder = null;
    }

    @Test
    public void testGetTvHistoryKeyShouldReturnValidTextView() {
        Assert.assertEquals(tvHistoryKey, covidPatientHistoryViewHolder.getTvHistoryKey());
    }

    @Test
    public void testGetTvHistoryValueShouldReturnValidTextView() {
        Assert.assertEquals(tvHistoryVal, covidPatientHistoryViewHolder.getTvHistoryValue());
    }
}
