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
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormFragmentInteractor;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.util.StepStateConfig;

@PrepareForTest({RDTApplication.class})
public class CovidJsonFormFragmentPresenterTest extends PowerMockTest {

    private CovidJsonFormFragmentPresenter presenter;

    private RDTJsonFormFragment rdtFormFragmentView;

    @Before
    public void setUp() throws JSONException {
        mockStaticMethods();

        rdtFormFragmentView = Mockito.spy(new PatientRegisterFragmentStub());
        presenter = new CovidJsonFormFragmentPresenter(rdtFormFragmentView, Mockito.mock(RDTJsonFormInteractor.class));
        Whitebox.setInternalState(presenter, "rdtJsonFormFragmentInteractor", Mockito.mock(RDTJsonFormFragmentInteractor.class));

    }

    @Test
    public void testPerformNextButtonActionShouldHandleCommonTestClicks() {
        presenter.performNextButtonAction("", null);
        Mockito.verify(rdtFormFragmentView).navigateToNextStep();
    }

    private void mockStaticMethods() {
        PowerMockito.mockStatic(RDTApplication.class);

        RDTApplication rdtApplication = Mockito.mock(RDTApplication.class);
        org.smartregister.Context drishtiContext = Mockito.mock(org.smartregister.Context.class);
        StepStateConfig stepStateConfig = Mockito.mock(StepStateConfig.class);

        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);
        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }

}
