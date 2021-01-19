package io.ona.rdt.presenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.interactor.RDTJsonFormFragmentInteractor;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.StepStateConfig;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@PrepareForTest({RDTApplication.class})
public class CovidJsonFormFragmentPresenterTest extends PowerMockTest {

    private CovidJsonFormFragmentPresenter presenter;

    private RDTJsonFormFragmentContract.View rdtFormFragmentView;

    @Before
    public void setUp() throws JSONException {
        mockStaticMethods();

        rdtFormFragmentView = spy(new PatientRegisterFragmentStub());
        presenter = new CovidJsonFormFragmentPresenter(rdtFormFragmentView, mock(RDTJsonFormInteractor.class));
        Whitebox.setInternalState(presenter, "rdtJsonFormFragmentInteractor", Mockito.mock(RDTJsonFormFragmentInteractor.class));

    }

    @Test
    public void testPerformNextButtonActionShouldHandleCommonTestClicks() {
        presenter.performNextButtonAction("", null);
        Mockito.verify(rdtFormFragmentView).navigateToNextStep();
    }

    private void mockStaticMethods() {
        mockStatic(RDTApplication.class);

        RDTApplication rdtApplication = Mockito.mock(RDTApplication.class);
        org.smartregister.Context drishtiContext = Mockito.mock(org.smartregister.Context.class);
        StepStateConfig stepStateConfig = Mockito.mock(StepStateConfig.class);

        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);
        JSONObject jsonObject = mock(JSONObject.class);
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }

}
