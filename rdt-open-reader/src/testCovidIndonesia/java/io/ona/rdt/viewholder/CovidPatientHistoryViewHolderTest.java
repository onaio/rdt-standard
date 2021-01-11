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
    private final String txtKey = "key";
    private final String txtVal = "val";

    @Before
    public void setUp() {
        View itemView = Mockito.mock(View.class);
        TextView tvHistoryKey = Mockito.mock(TextView.class);
        TextView tvHistoryValue = Mockito.mock(TextView.class);

        Mockito.when(itemView.findViewById(Mockito.eq(R.id.tv_history_key))).thenReturn(tvHistoryKey);
        Mockito.when(itemView.findViewById(Mockito.eq(R.id.tv_history_value))).thenReturn(tvHistoryValue);
        Mockito.when(tvHistoryKey.getText()).thenReturn(txtKey);
        Mockito.when(tvHistoryValue.getText()).thenReturn(txtVal);

        covidPatientHistoryViewHolder = new CovidPatientHistoryViewHolder(itemView);
    }

    @Test
    public void testGetTvHistoryKeyShouldValidViewAndData() {
        Assert.assertNotNull(covidPatientHistoryViewHolder.getTvHistoryKey());
        Assert.assertEquals(txtKey, covidPatientHistoryViewHolder.getTvHistoryKey().getText());
    }

    @Test
    public void testGetTvHistoryValueShouldReturnValidViewAndData() {
        Assert.assertNotNull(covidPatientHistoryViewHolder.getTvHistoryValue());
        Assert.assertEquals(txtVal, covidPatientHistoryViewHolder.getTvHistoryValue().getText());
    }
}
