package io.ona.rdt.robolectric.viewholder;

import android.view.View;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.viewholder.CovidPatientRegisterViewHolder;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

public class CovidPatientRegisterViewHolderTest extends RobolectricTest {

    private CovidPatientRegisterViewHolder viewHolder;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        viewHolder = new CovidPatientRegisterViewHolder(RDTApplication.getInstance(), Mockito.mock(View.OnClickListener.class), Mockito.mock(View.OnClickListener.class));
    }

    @Test
    public void testCreateViewHolderShouldReturnRegisterViewHolder() {
        PatientRegisterViewHolder.RegisterViewHolder registerViewHolder = viewHolder.createViewHolder(null);

        Assert.assertNotNull(registerViewHolder);
        Assert.assertNotNull(registerViewHolder.rowItem);
        Assert.assertNotNull(registerViewHolder.tvPatientNameAndAge);
        Assert.assertNotNull(registerViewHolder.tvPatientSex);
        Assert.assertNotNull(registerViewHolder.btnRecordRDTTest);
    }
}
