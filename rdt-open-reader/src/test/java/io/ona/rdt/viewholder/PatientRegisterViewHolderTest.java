package io.ona.rdt.viewholder;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.util.Utils;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.Constants;

import static org.hamcrest.Matchers.any;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
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

    @Test
    public void testGetViewShouldCorrectlySetUpView() {
        PatientRegisterViewHolder.RegisterViewHolder viewHolder = mock(PatientRegisterViewHolder.RegisterViewHolder.class);
        viewHolder.tvPatientNameAndAge = mock(TextView.class);
        viewHolder.tvPatientSex = mock(TextView.class);
        viewHolder.rowItem = mock(View.class);
        viewHolder.btnRecordRDTTest = mock(TextView.class);

        Map<String, String> columnMap = new HashMap<>();
        columnMap.put(Constants.DBConstants.FIRST_NAME, "John");
        columnMap.put(Constants.DBConstants.LAST_NAME, "Doe");
        columnMap.put(Constants.DBConstants.AGE, "12");
        columnMap.put(Constants.DBConstants.SEX, "female");
        columnMap.put(Constants.DBConstants.PATIENT_ID, "patient_id");

        CommonPersonObjectClient client = new CommonPersonObjectClient("case_id", null, "name");
        client.setColumnmaps(columnMap);
        patientRegisterViewHolder.getView(mock(Cursor.class), client, viewHolder);

        verify(viewHolder.tvPatientNameAndAge).setText(eq("John Doe, 12"));
        verify(viewHolder.tvPatientSex).setText(eq("Female"));

        columnMap.put(Constants.DBConstants.FIRST_NAME, "John");
        columnMap.put(Constants.DBConstants.LAST_NAME, "");
        patientRegisterViewHolder.getView(mock(Cursor.class), client, viewHolder);
        verify(viewHolder.tvPatientNameAndAge).setText(eq("John, 12"));

        columnMap.put(Constants.DBConstants.FIRST_NAME, "");
        columnMap.put(Constants.DBConstants.LAST_NAME, "Doe");

        patientRegisterViewHolder.getView(mock(Cursor.class), client, viewHolder);
        verify(viewHolder.tvPatientNameAndAge).setText(eq("Doe, 12"));
    }
}
