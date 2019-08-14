package io.ona.rdt_app.presenter;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import edu.washington.cs.ubicomplab.rdt_reader.RDT;
import io.ona.rdt_app.contract.RDTJsonFormFragmentContract;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.util.Constants;

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

    @Before
    public void setUp() {
        interactor = mock(RDTJsonFormInteractor.class);
        fragment = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(fragment, interactor);
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {
//        presenter.performNextButtonAction("5", null);
//        verify(interactor).saveForm(any(JSONObject.class));
//        verify(fragment).moveToNextStep();
    }

    @Test
    public void testPerformNextButtonActionShouldSkipImageViewsForCarestartRDT() {
        doReturn(Constants.CARESTART_RDT).when(fragment).getRDTType();
        mockStatic(RDTJsonFormFragment.class);

        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        when(RDTJsonFormFragment.getFormFragment(anyString())).thenReturn(formFragment);
        presenter.performNextButtonAction("8", null);
        verify(fragment).transactFragment(eq(formFragment));
    }

    @Test
    public void testPerformNextButtonActionShouldShowImageViewsForONARDT() {
        doReturn(Constants.ONA_RDT).when(fragment).getRDTType();
        presenter.performNextButtonAction("8", null);
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
}
