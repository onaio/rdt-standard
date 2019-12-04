package io.ona.rdt.viewholder;

import android.content.Context;
import android.view.View;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
public class PatientRegisterViewHolderTest {

    private PatientRegisterViewHolder patientRegisterViewHolder;

    @Mock
    private Context context;

    @Mock
    private View.OnClickListener onClickListener;

    @Before
    public void setUp() {
        patientRegisterViewHolder = new PatientRegisterViewHolder(context, onClickListener, onClickListener);
    }

    @Test
    public void testCreateNameAndAgeLabel() throws Exception {
        assertEquals("Doe, 12", Whitebox.invokeMethod(patientRegisterViewHolder, "createNameAndAgeLabel", "Doe", "", "12"));
        assertEquals("Doe, 12", Whitebox.invokeMethod(patientRegisterViewHolder, "createNameAndAgeLabel", "", "Doe", "12"));
    }

    @Test
    public void testAttachPatientOnclickListener() throws Exception {
        View view = mock(View.class);
        Whitebox.invokeMethod(patientRegisterViewHolder, "attachPatientOnclickListener", view);
        verify(view).setOnClickListener(eq(onClickListener));
    }
}
