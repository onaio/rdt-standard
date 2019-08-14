package io.ona.rdt_app.presenter;

import org.junit.Before;
import org.junit.Test;

import io.ona.rdt_app.contract.RDTJsonFormFragmentContract;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
public class RDTJsonFormFragmentPresenterTest {

    private RDTJsonFormFragmentPresenter presenter;
    private RDTJsonFormFragmentContract.View fragment;

    @Before
    public void setUp() {
        RDTJsonFormInteractor interactor = new RDTJsonFormInteractor();
        fragment = spy(new PatientRegisterFragmentStub());
        presenter = new RDTJsonFormFragmentPresenter(fragment, interactor);
    }

    @Test
    public void testPerformNextButtonActionShouldNavigateToNextStepAndSaveFormFromExpirationPage() {

    }

    @Test
    public void testPerformNextButtonActionShouldSkipImageViewsForCarestartRDT() {

    }

    @Test
    public void testPerformNextButtonActionShouldShowImageViewsForONARDT() {

    }

    @Test
    public void testPerformNextButtonActionShouldMoveToNextStepForDefaultNextButton() {
        presenter.performNextButtonAction("step1", true);
        verify(fragment).saveForm();
    }

    @Test
    public void testPerformNextButtonActionShouldSubmitFormForSubmitTypeNextButton() {
        presenter.performNextButtonAction("step1", null);
        verify(fragment).moveToNextStep();
    }
}
