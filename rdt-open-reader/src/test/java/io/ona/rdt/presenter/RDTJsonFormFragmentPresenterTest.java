package io.ona.rdt.presenter;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.mvp.MvpView;
import com.vijay.jsonwizard.views.JsonFormFragmentView;
import com.vijay.jsonwizard.viewstates.JsonFormFragmentViewState;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.lang.ref.WeakReference;

import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.Constants;

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

/**
 * Created by Vincent Karuri on 14/08/2019
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class})
public class RDTJsonFormFragmentPresenterTest {

    private RDTJsonFormFragmentPresenter presenter;
    private RDTJsonFormFragmentContract.View fragment;
    private RDTJsonFormInteractor interactor;
    private JsonFormFragment formFragment;

    @Before
    public void setUp() {
        interactor = mock(RDTJsonFormInteractor.class);
        fragment = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(fragment, interactor);
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
        presenter.attachView((JsonFormFragment) fragment);
        presenter.performNextButtonAction("step6", null);
        verify(interactor).saveForm(any(JSONObject.class));
        verify(fragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSkipImageViewsForCarestartRDT() {
        doReturn(Constants.CARESTART_RDT).when(fragment).getRDTType();
        mockStaticClasses();
        presenter.performNextButtonAction("step9", null);
        verify(fragment).transactFragment(eq(formFragment));
    }

    @Test
    public void testPerformNextButtonActionShouldShowImageViewsForONARDT() {
        doReturn(Constants.ONA_RDT).when(fragment).getRDTType();
        presenter.performNextButtonAction("step8", null);
        verify(fragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() {
        presenter.performNextButtonAction("step1", null);
        verify(fragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() {
        presenter.performNextButtonAction("step1", true);
        verify(fragment).saveForm();
    }

    @Test
    public void testHasNextStep() throws JSONException {
        addMockStepDetails();
        assertTrue(presenter.hasNextStep());
    }

    @Test
    public void testMoveToNextStep() throws JSONException {
        addMockStepDetails();
        mockStaticClasses();
        WeakReference<JsonFormFragmentView<JsonFormFragmentViewState>> viewRef = mock(WeakReference.class);

        JsonFormFragmentView<JsonFormFragmentViewState> view = mock(JsonFormFragmentView.class);
        doReturn(view).when(viewRef).get();
        Whitebox.setInternalState(presenter, "viewRef", viewRef);

        presenter.moveToNextStep("step1");
        verify(view).hideKeyBoard();
        verify(view).transactThis(eq(formFragment));
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
}
