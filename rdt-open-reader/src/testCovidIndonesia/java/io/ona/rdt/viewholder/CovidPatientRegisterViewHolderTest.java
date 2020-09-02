package io.ona.rdt.viewholder;

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
import org.mockito.junit.MockitoJUnitRunner;

import io.ona.rdt.R;

@RunWith(MockitoJUnitRunner.class)
public class CovidPatientRegisterViewHolderTest {

    private CovidPatientRegisterViewHolder covidPatientRegisterViewHolder;

    @Before
    public void setUp() {

        covidPatientRegisterViewHolder = Mockito.spy(
                new CovidPatientRegisterViewHolder(
                        Mockito.mock(Context.class) ,
                        Mockito.mock(View.OnClickListener.class) ,
                        Mockito.mock(View.OnClickListener.class)
                )
        );
    }

    @After
    public void tearDown() {
        covidPatientRegisterViewHolder = null;
    }

    @Test
    public void testCreateViewHolderShouldReturnRegisterViewHolder() {
        View view = Mockito.mock(View.class);
        TextView tvDummy = Mockito.mock(TextView.class);
        ViewGroup parent = Mockito.mock(ViewGroup.class);
        LayoutInflater inflater = Mockito.mock(LayoutInflater.class);

        Mockito.doReturn(inflater).when(covidPatientRegisterViewHolder).inflater();
        Mockito.doReturn(view).when(inflater).inflate(ArgumentMatchers.anyInt(), ArgumentMatchers.any(ViewGroup.class), ArgumentMatchers.anyBoolean());
        Mockito.doReturn(tvDummy).when(view).findViewById(Mockito.eq(R.id.tv_patient_name_and_age));
        Mockito.doReturn(tvDummy).when(view).findViewById(Mockito.eq(R.id.tv_sex));
        Mockito.doReturn(tvDummy).when(view).findViewById(Mockito.eq(R.id.btn_record_rdt_test));

        PatientRegisterViewHolder.RegisterViewHolder registerViewHolder = covidPatientRegisterViewHolder.createViewHolder(parent);

        Assert.assertNotNull(registerViewHolder);
    }
}
