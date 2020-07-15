package io.ona.rdt.presenter;

import android.content.Context;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.views.JsonFormFragmentView;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormFragmentInteractor;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.Constants.Step.BLOT_PAPER_TASK_PAGE;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.util.Constants.Step.TAKE_IMAGE_OF_RDT_PAGE;
import static io.ona.rdt.util.RDTJsonFormUtilsTest.RDT_TEST_JSON_FORM;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, RDTApplication.class})
public class RDTJsonFormFragmentPresenterTest {

    protected RDTJsonFormFragmentPresenter presenter;
    protected RDTJsonFormFragmentContract.View rdtFormFragmentView;
    protected RDTJsonFormFragment formFragment;
    private JsonFormFragmentView<JsonFormFragmentViewState> view;

    @Mock
    private RDTJsonFormFragmentInteractor interactor;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    protected EventClientRepository eventClientRepository;

    @Before
    public void setUp() throws JSONException {
        mockStaticMethods();
        rdtFormFragmentView = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(rdtFormFragmentView, mock(RDTJsonFormInteractor.class));
        Whitebox.setInternalState(presenter, "rdtJsonFormFragmentInteractor", interactor);
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
        presenter.attachView((JsonFormFragment) rdtFormFragmentView);
        presenter.performNextButtonAction("step6", null);
        verify(interactor).saveForm(any(JSONObject.class), isNull());
        verify(rdtFormFragmentView).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", null);
        verify(rdtFormFragmentView).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", true);
        verify(rdtFormFragmentView).saveForm();
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

    private void invokePerformNextButtonActionFromExpirationPage() {
        presenter.performNextButtonAction("step20", null);
    }

    private void addViewAndMockStaticClasses() throws JSONException {
        addMockStepDetails();
        mockStaticClasses();
        WeakReference<JsonFormFragmentView<JsonFormFragmentViewState>> viewRef = mock(WeakReference.class);
        view = mock(JsonFormFragmentView.class);
        doReturn(RDT_TEST_JSON_FORM).when(view).getCurrentJsonState();
        doReturn(view).when(viewRef).get();
        Whitebox.setInternalState(presenter, "viewRef", viewRef);
    }

    private void addMockStepDetails() throws JSONException {
        JSONObject mStepDetails = new JSONObject();
        mStepDetails.put("next", "step1");
        Whitebox.setInternalState(presenter, "mStepDetails", mStepDetails);
    }

    protected void mockStaticClasses() throws JSONException {
        mockStatic(RDTJsonFormFragment.class);
        formFragment = mock(RDTJsonFormFragment.class);
        when(RDTJsonFormFragment.getFormFragment(anyString())).thenReturn(formFragment);
    }

    protected void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.getEventClientRepository()).thenReturn(eventClientRepository);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn("step1").when(jsonObject).optString(eq(PRODUCT_EXPIRED_PAGE), anyString());
        doReturn("step6").when(jsonObject).optString(eq(PRODUCT_EXPIRED_PAGE));
        doReturn("step9").when(jsonObject).optString(eq(BLOT_PAPER_TASK_PAGE));
        doReturn("step1:expiration_date_reader").when(jsonObject).optString(eq(EXPIRATION_DATE_READER_ADDRESS), anyString());
        doReturn("step1").when(jsonObject).optString(eq(TAKE_IMAGE_OF_RDT_PAGE));
        doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
        doReturn(new JSONArray("[\n" +
                "    \"step7:lbl_rdt_id\",\n" +
                "    \"step8:lbl_rdt_id\",\n" +
                "    \"step9:lbl_rdt_id\",\n" +
                "    \"step18:lbl_rdt_id\",\n" +
                "    \"step19:lbl_rdt_id\"\n" +
                "  ]")).when(jsonObject).optJSONArray(eq(RDT_ID_LBL_ADDRESSES));
        doReturn("step20").when(jsonObject).optString(eq(MANUAL_EXPIRATION_DATE_ENTRY_PAGE));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
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
