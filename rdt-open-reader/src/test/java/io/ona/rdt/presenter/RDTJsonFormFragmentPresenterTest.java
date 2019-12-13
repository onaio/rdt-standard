package io.ona.rdt.presenter;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
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

import java.lang.ref.WeakReference;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.Step.BLOT_PAPER_TASK_PAGE;
import static io.ona.rdt.util.Constants.Step.EXPIRATION_DATE_READER_ADDRESS;
import static io.ona.rdt.util.Constants.Step.MANUAL_ENTRY_EXPIRATION_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.RDT_ID_LBL_ADDRESSES;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.util.Constants.Step.TAKE_IMAGE_OF_RDT_PAGE;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, RDTJsonFormFragmentPresenter.class, RDTApplication.class})
public class RDTJsonFormFragmentPresenterTest {

    private RDTJsonFormFragmentPresenter presenter;
    private RDTJsonFormFragmentContract.View rdtFormFragment;
    private RDTJsonFormInteractor interactor;
    private JsonFormFragment formFragment;
    private JsonFormFragmentView<JsonFormFragmentViewState> view;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Before
    public void setUp() throws JSONException {
        interactor = mock(RDTJsonFormInteractor.class);
        rdtFormFragment = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(rdtFormFragment, interactor);
        mockStaticMethods();
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
        presenter.attachView((JsonFormFragment) rdtFormFragment);
        presenter.performNextButtonAction("step6", null);
        verify(interactor).saveForm(any(JSONObject.class));
        verify(rdtFormFragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSkipImageViewsForCarestartRDT() {
        doReturn(Constants.RDTType.CARESTART_RDT).when(rdtFormFragment).getRDTType();
        mockStaticClasses();
        presenter.performNextButtonAction("step9", null);
        verify(rdtFormFragment).transactFragment(eq(formFragment));
    }

    @Test
    public void testPerformNextButtonActionShouldShowImageViewsForONARDT() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        doReturn(Constants.RDTType.ONA_RDT).when(rdtFormFragment).getRDTType();
        presenter.performNextButtonAction("step8", null);
        verify(rdtFormFragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", null);
        verify(rdtFormFragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() throws JSONException {
        mockStaticMethods();
        mockStaticClasses();
        presenter.performNextButtonAction("step1", true);
        verify(rdtFormFragment).saveForm();
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
    public void testSetupToolbar() throws JSONException {
        addViewAndMockStaticClasses();
        suppress(methods(JsonFormFragmentPresenter.class, "setUpToolBar"));
        presenter.setUpToolBar();
        verify(view).updateVisibilityOfNextAndSave(eq(false), eq(false));
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForOnaRDT() {
        doReturn(Constants.RDTType.ONA_RDT).when(rdtFormFragment).getRDTType();
        mockStaticClasses();
        presenter.performNextButtonAction("step9", null);
        verify(rdtFormFragment).moveToNextStep();
    }

    private void addViewAndMockStaticClasses() throws JSONException {
        addMockStepDetails();
        mockStaticClasses();
        WeakReference<JsonFormFragmentView<JsonFormFragmentViewState>> viewRef = mock(WeakReference.class);
        view = mock(JsonFormFragmentView.class);
        doReturn(view).when(viewRef).get();
        Whitebox.setInternalState(presenter, "viewRef", viewRef);
    }

    private void addMockStepDetails() throws JSONException {
        JSONObject mStepDetails = new JSONObject();
        mStepDetails.put("next", "step1");
        Whitebox.setInternalState(presenter, "mStepDetails", mStepDetails);
    }

    private void mockStaticClasses() {
        mockStatic(RDTJsonFormFragment.class);
        formFragment = mock(JsonFormFragment.class);
        when(RDTJsonFormFragment.getFormFragment(anyString())).thenReturn(formFragment);
    }

    private void mockStaticMethods() throws JSONException {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn("step1").when(jsonObject).optString(eq(RDT_EXPIRED_PAGE), anyString());
        doReturn("step6").when(jsonObject).optString(eq(RDT_EXPIRED_PAGE));
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
        doReturn("").when(jsonObject).optString(eq(MANUAL_ENTRY_EXPIRATION_PAGE));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }
}
