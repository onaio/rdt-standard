package io.ona.rdt.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.widget.LinearLayout;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnFieldsInvalid;
import com.vijay.jsonwizard.utils.ValidationStatus;
import com.vijay.jsonwizard.views.JsonFormFragmentView;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.powermock.reflect.Whitebox;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.RDTJsonFormUtilsTest.RDT_TEST_JSON_FORM;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 14/08/2019
 */

public class RDTJsonFormFragmentPresenterTest extends BaseRDTJsonFormFragmentPresenterTest {

    @Mock
    private JsonFormFragmentView<JsonFormFragmentViewState> view;

    private final String DUMMY_STR_VAL = "string";

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
        presenter.attachView((JsonFormFragment) rdtFormFragmentView);
        presenter.performNextButtonAction("step6", null);
        verify(interactor).saveForm(any(JSONObject.class), isNull());
        verify(rdtFormFragmentView).navigateToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", null);
        verify(rdtFormFragmentView).navigateToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", true);
        verify(rdtFormFragmentView).saveForm();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForCarestart() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        doReturn(Constants.RDTType.CARESTART_RDT).when(rdtFormFragmentView).getRDTType();
        presenter.performNextButtonAction(BLOT_PAPER_TASK_PAGE_NO, false);
        verify(rdtFormFragmentView).transactFragment(any(JsonFormFragment.class));
    }

    @Test
    public void testPerformNextButtonActionShouldSkipToRDTCaptureForOGRDT() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction(BLOT_PAPER_TASK_PAGE_NO, false);
        verify(rdtFormFragmentView).navigateToNextStep();
    }

    @Test
    public void testHasNextStep() throws JSONException {
        addMockStepDetails();
        assertTrue(presenter.hasNextStep());
    }

    @Test
    public void testMoveToNextStep() throws JSONException {
        addViewAndMockStaticClasses();
        presenter.moveToNextStep("step1");
        verify(view).hideKeyBoard();
        verify(view).transactThis(eq(formFragment));
    }

    @Test
    public void testMoveToNextStepWithStepArg() throws JSONException {
        addViewAndMockStaticClasses();
        presenter.moveToNextStep("step1");
        verify(view).hideKeyBoard();
        verify(view).transactThis(eq(formFragment));
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToCorrectStepFromExpirationPage() throws Exception {
        // valid rdt
        mockStaticClasses();
        mockStaticMethods();
        doReturn(new JSONObject(getExpirationDatePage(false))).when((JsonFormFragment) rdtFormFragmentView).getStep(anyString());
        invokePerformNextButtonActionFromExpirationPage();
        verify((JsonFormFragment) rdtFormFragmentView).next();
        // expired rdt
        doReturn(new JSONObject(getExpirationDatePage(true))).when((JsonFormFragment) rdtFormFragmentView).getStep(anyString());
        doNothing().when((JsonFormFragment) rdtFormFragmentView).transactThis(any(JsonFormFragment.class));
        invokePerformNextButtonActionFromExpirationPage();
        verify((JsonFormFragment) rdtFormFragmentView).transactThis(any(JsonFormFragment.class));
    }

    @Test
    public void testSetUpToolbarShouldCorrectlySetUpToolbar() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();
        Whitebox.setInternalState(presenter, "mStepName", "step345");
        presenter.setUpToolBar();
        verify(view).updateVisibilityOfNextAndSave(eq(false), eq(false));
    }

    @Test
    public void testOnNextClickShouldMoveToNextStep() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();
        JsonFormFragment jsonFormFragment = (JsonFormFragment) rdtFormFragmentView;
        jsonFormFragment.setOnFieldsInvalid(mock(OnFieldsInvalid.class));

        presenter.onNextClick(mock(LinearLayout.class));
        verify(view).hideKeyBoard();
        verify(view).transactThis(any(JsonFormFragment.class));
    }

    @Test
    public void testOnNextClickShouldShowErrorSnackBar() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();

        Map<String, ValidationStatus> invalidFields = new HashMap<>();
        invalidFields.put(DUMMY_STR_VAL, new ValidationStatus(false, "", view, mock(View.class)));
        Whitebox.setInternalState(presenter, "invalidFields", invalidFields);
        JsonFormFragment jsonFormFragment = (JsonFormFragment) rdtFormFragmentView;
        jsonFormFragment.setOnFieldsInvalid(mock(OnFieldsInvalid.class));

        presenter.onNextClick(mock(LinearLayout.class));
        verify(view).showSnackBar(eq(DUMMY_STR_VAL));
    }

    private void invokePerformNextButtonActionFromExpirationPage() {
        presenter.performNextButtonAction("step20", null);
    }

    private void addViewAndMockStaticClasses() throws JSONException {
        addMockStepDetails();
        mockStaticClasses();
        WeakReference<JsonFormFragmentView<JsonFormFragmentViewState>> viewRef = mock(WeakReference.class);
        doReturn(RDT_TEST_JSON_FORM).when(view).getCurrentJsonState();
        doReturn(view).when(viewRef).get();
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        doReturn(resources).when(context).getResources();
        doReturn(DUMMY_STR_VAL).when(resources).getString(ArgumentMatchers.anyInt());
        doReturn(context).when(view).getContext();
        Whitebox.setInternalState(presenter, "viewRef", viewRef);
    }

    private void addMockStepDetails() throws JSONException {
        JSONObject mStepDetails = new JSONObject();
        mStepDetails.put(JsonFormConstants.NEXT, "step1");
        mStepDetails.put(JsonFormConstants.STEP_TITLE, "title");
        Whitebox.setInternalState(presenter, "mStepDetails", mStepDetails);
    }

    private String getExpirationDatePage(boolean isExpired) {
        Date date = isExpired ? getDateWithOffset(-1) : getDateWithOffset(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY");
        String dateStr = simpleDateFormat.format(date);
        return  "{\n" +
                "    \"title\": \"Record RDT information\",\n" +
                "    \"display_back_button\": \"true\",\n" +
                "    \"next\": \"step6\",\n" +
                "    \"bottom_navigation\": \"true\",\n" +
                "    \"bottom_navigation_orientation\": \"vertical\",\n" +
                "    \"next_label\": \"CONTINUE\",\n" +
                "    \"fields\": [\n" +
                "      {\n" +
                "        \"key\": \"lbl_rdt_manufacturer\",\n" +
                "        \"type\": \"label\",\n" +
                "        \"text\": \"Enter manufacturer name\",\n" +
                "        \"text_color\": \"#000000\",\n" +
                "        \"text_size\": \"9sp\",\n" +
                "        \"top_margin\": \"15dp\",\n" +
                "        \"has_bg\": true,\n" +
                "        \"bg_color\": \"#ffffff\",\n" +
                "        \"bottom_margin\": \"50dp\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"rdt_manufacturer\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"false\",\n" +
                "          \"err\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"lbl_rdt_test_name\",\n" +
                "        \"type\": \"label\",\n" +
                "        \"text\": \"Enter RDT name\",\n" +
                "        \"text_color\": \"#000000\",\n" +
                "        \"text_size\": \"9sp\",\n" +
                "        \"top_margin\": \"15dp\",\n" +
                "        \"has_bg\": true,\n" +
                "        \"bg_color\": \"#ffffff\",\n" +
                "        \"bottom_margin\": \"50dp\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"rdt_test_name\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"false\",\n" +
                "          \"err\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"lbl_rdt_batch_id\",\n" +
                "        \"type\": \"label\",\n" +
                "        \"text\": \"Enter RDT batch ID\",\n" +
                "        \"text_color\": \"#000000\",\n" +
                "        \"text_size\": \"9sp\",\n" +
                "        \"top_margin\": \"15dp\",\n" +
                "        \"has_bg\": true,\n" +
                "        \"bg_color\": \"#ffffff\",\n" +
                "        \"bottom_margin\": \"50dp\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"rdt_batch_id\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"edit_text\",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": \"false\",\n" +
                "          \"err\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"lbl_record_expiration_date\",\n" +
                "        \"type\": \"label\",\n" +
                "        \"text\": \"Catat Tanggal Kedaluwarsa\",\n" +
                "        \"text_color\": \"#000000\",\n" +
                "        \"text_size\": \"9sp\",\n" +
                "        \"top_margin\": \"15dp\",\n" +
                "        \"has_bg\": true,\n" +
                "        \"bg_color\": \"#ffffff\",\n" +
                "        \"bottom_margin\": \"50dp\"\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"manual_expiration_date\",\n" +
                "        \"type\": \"date_picker\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"hint\": \"Tanggal kadaluarsa\",\n" +
                "        \"value\":" + dateStr + ",\n" +
                "        \"v_required\": {\n" +
                "          \"value\": true,\n" +
                "          \"err\": \"Please specify the expiration date\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"key\": \"rdt_id\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"hidden\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }";
    }
}
