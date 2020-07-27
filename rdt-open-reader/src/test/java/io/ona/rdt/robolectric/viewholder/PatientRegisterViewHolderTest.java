package io.ona.rdt.robolectric.viewholder;

import android.database.Cursor;
import android.view.View;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.commonregistry.CommonPersonObjectClient;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.R;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.viewholder.FooterViewHolder;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
public class PatientRegisterViewHolderTest extends RobolectricTest {

    private PatientRegisterViewHolder patientRegisterViewHolder;

    @Mock
    private View.OnClickListener onClickListener;

    @Before
    public void setUp() {
        patientRegisterViewHolder = new PatientRegisterViewHolder(RuntimeEnvironment.application, onClickListener, onClickListener);
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

    @Test
    public void testIsFooterViewHolderShouldReturnCorrectValue() {
        assertFalse(patientRegisterViewHolder.isFooterViewHolder(mock(PatientRegisterViewHolder.RegisterViewHolder.class)));
        assertTrue(patientRegisterViewHolder.isFooterViewHolder(mock(FooterViewHolder.class)));
    }

    @Test
    public void testCreateFooterHolderShouldReturnNonNullFooterHolder() {
        assertNotNull(patientRegisterViewHolder.createFooterHolder(null));
    }

    @Test
    public void testCreateViewHolderShouldReturnNonNullViewHolder() {
        View view = patientRegisterViewHolder.inflater().inflate(R.layout.register_row_item, null, false);
        PatientRegisterViewHolder.RegisterViewHolder viewHolder = patientRegisterViewHolder.createViewHolder(null);
        assertNotNull(viewHolder);
        assertEquals(view.getRootView().getId(), viewHolder.rowItem.getId());
        assertEquals(R.id.tv_patient_name_and_age, viewHolder.tvPatientNameAndAge.getId());
        assertEquals(R.id.tv_sex, viewHolder.tvPatientSex.getId());
        assertEquals(R.id.btn_record_rdt_test, viewHolder.btnRecordRDTTest.getId());
    }

    @Test
    public void testGetFooterViewShouldCorrectlySetUpFooterView() {
        View.OnClickListener paginationClickListener = mock(View.OnClickListener.class);
        ReflectionHelpers.setField(patientRegisterViewHolder, "paginationClickListener", paginationClickListener);
        FooterViewHolder footerViewHolder = (FooterViewHolder) patientRegisterViewHolder.createFooterHolder(null);
        patientRegisterViewHolder.getFooterView(footerViewHolder, 2, 3, true, true);

        assertEquals(View.VISIBLE, footerViewHolder.nextPageView.getVisibility());
        assertEquals(View.VISIBLE, footerViewHolder.previousPageView.getVisibility());
        assertEquals( MessageFormat.format(RuntimeEnvironment.application.getString(R.string.str_page_info), 2, 3),
                footerViewHolder.pageInfoView.getText());
        footerViewHolder.nextPageView.performClick();
        verify(paginationClickListener).onClick(eq(footerViewHolder.nextPageView));
        footerViewHolder.previousPageView.performClick();
        verify(paginationClickListener).onClick(eq(footerViewHolder.previousPageView));
    }
}
