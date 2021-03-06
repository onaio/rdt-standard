package io.ona.rdt.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.widget.LinearLayout;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnFieldsInvalid;
import com.vijay.jsonwizard.views.JsonFormFragmentView;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.RDTJsonFormUtilsTest.RDT_TEST_JSON_FORM;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 14/08/2019
 */

public class RDTJsonFormFragmentPresenterTest extends BaseRDTJsonFormFragmentPresenterTest {

    @Mock
    private JsonFormFragmentView<JsonFormFragmentViewState> view;

    private final String DUMMY_STR_VAL = "string";

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
        presenter.attachView((JsonFormFragment) rdtFormFragment);
        presenter.performNextButtonAction("step6", null);
        Mockito.verify(interactor).saveForm(ArgumentMatchers.any(JSONObject.class), isNull());
        Mockito.verify(rdtFormFragment).navigateToNextStep();
    }

    @Test
    public void testGetNextJsonFormFragmentShouldGetJsonFormFragment() {
        Assert.assertTrue(presenter.getNextJsonFormFragment(JsonFormConstants.STEP1) instanceof RDTJsonFormFragment);
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", null);
        Mockito.verify(rdtFormFragment).navigateToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", true);
        Mockito.verify(rdtFormFragment).saveForm();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForCarestart() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        Mockito.doReturn(Constants.RDTType.CARESTART_RDT).when(rdtFormFragment).getRDTType();
        presenter.performNextButtonAction(BLOT_PAPER_TASK_PAGE_NO, false);
        Mockito.verify(rdtFormFragment).transactFragment(ArgumentMatchers.any(JsonFormFragment.class));
    }

    @Test
    public void testPerformNextButtonActionShouldSkipToRDTCaptureForOGRDT() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction(BLOT_PAPER_TASK_PAGE_NO, false);
        Mockito.verify(rdtFormFragment).navigateToNextStep();
    }

    @Test
    public void testMoveToNextStepForNonBlankStep() throws JSONException {
        // should move to next step for non-blank step
        addViewAndMockStaticClasses();
        presenter.moveToNextStep("step1");
        Mockito.verify(view).hideKeyBoard();
        Mockito.verify(view).transactThis(eq(formFragment));

        // don't move to next step for blank step
        Mockito.clearInvocations(view);
        Assert.assertFalse(presenter.moveToNextStep(""));
        Mockito.verifyZeroInteractions(view);
    }

    @Test
    public void testMoveToNextStepWithStepArg() throws JSONException {
        addViewAndMockStaticClasses();
        presenter.moveToNextStep("step1");
        Mockito.verify(view).hideKeyBoard();
        Mockito.verify(view).transactThis(eq(formFragment));
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToCorrectStepFromExpirationPage() throws Exception {

        // if no date exists
        mockStaticClasses();
        mockStaticMethods();
        Mockito.doReturn(new JSONObject()).when((JsonFormFragment) rdtFormFragment).getStep(ArgumentMatchers.anyString());
        invokePerformNextButtonActionFromExpirationPage();
        Mockito.verify((JsonFormFragment) rdtFormFragment, Mockito.never()).next();
        Mockito.verify((JsonFormFragment) rdtFormFragment, Mockito.never()).transactThis(ArgumentMatchers.any(JsonFormFragment.class));

        // valid rdt
        Mockito.doReturn(new JSONObject(getExpirationDatePage(false))).when((JsonFormFragment) rdtFormFragment).getStep(ArgumentMatchers.anyString());
        invokePerformNextButtonActionFromExpirationPage();
        Mockito.verify((JsonFormFragment) rdtFormFragment).next();

        // expired rdt
        Mockito.doReturn(new JSONObject(getExpirationDatePage(true))).when((JsonFormFragment) rdtFormFragment).getStep(ArgumentMatchers.anyString());
        doNothing().when((JsonFormFragment) rdtFormFragment).transactThis(ArgumentMatchers.any(JsonFormFragment.class));
        invokePerformNextButtonActionFromExpirationPage();
        Mockito.verify((JsonFormFragment) rdtFormFragment).transactThis(ArgumentMatchers.any(JsonFormFragment.class));
    }

    @Test
    public void testSetUpToolbarShouldCorrectlySetUpToolbar() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();
        Whitebox.setInternalState(presenter, "mStepName", "step345");
        presenter.setUpToolBar();
        Mockito.verify(view).updateVisibilityOfNextAndSave(eq(false), eq(false));
    }

    @Test
    public void testOnNextClickShouldMoveToNextStep() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();
        JsonFormFragment jsonFormFragment = (JsonFormFragment) rdtFormFragment;
        jsonFormFragment.setOnFieldsInvalid(mock(OnFieldsInvalid.class));
        JsonApi jsonApi = jsonFormFragment.getJsonApi();
        Mockito.doReturn("step1").when(jsonApi).nextStep();
        Mockito.doReturn(jsonApi).when(jsonFormFragment).getJsonApi();

        presenter = Mockito.spy(presenter);
        Mockito.doReturn(false).when(presenter).executeRefreshLogicForNextStep();
        Mockito.doReturn(mock(RDTJsonFormFragment.class)).when(presenter).getNextJsonFormFragment(ArgumentMatchers.anyString());

        presenter.onNextClick(mock(LinearLayout.class));

        Mockito.verify(view).hideKeyBoard();
        Mockito.verify(view).transactThis(ArgumentMatchers.any(JsonFormFragment.class));
    }

    @Test
    public void testOnNextClickShouldShowErrorSnackBar() throws JSONException {
        addViewAndMockStaticClasses();
        mockStaticMethods();
        addMockStepDetails();

        presenter = Mockito.spy(presenter);
        Mockito.doReturn(false).when(presenter).isFormValid();

        JsonFormFragment jsonFormFragment = (JsonFormFragment) rdtFormFragment;
        jsonFormFragment.setOnFieldsInvalid(mock(OnFieldsInvalid.class));

        ReflectionHelpers.setField(presenter, "mStepName", JsonFormConstants.STEP1);

        presenter.onNextClick(mock(LinearLayout.class));
        Mockito.verify(view).showSnackBar(DUMMY_STR_VAL);
    }

    private void invokePerformNextButtonActionFromExpirationPage() {
        presenter.performNextButtonAction("step20", null);
    }

    private void addViewAndMockStaticClasses() throws JSONException {
        addMockStepDetails();
        mockStaticClasses();
        WeakReference<JsonFormFragmentView<JsonFormFragmentViewState>> viewRef = mock(WeakReference.class);
        Mockito.doReturn(RDT_TEST_JSON_FORM).when(view).getCurrentJsonState();
        Mockito.doReturn(view).when(viewRef).get();
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        Mockito.doReturn(resources).when(context).getResources();
        Mockito.doReturn(DUMMY_STR_VAL).when(resources).getString(ArgumentMatchers.anyInt());
        Mockito.doReturn(context).when(view).getContext();
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
