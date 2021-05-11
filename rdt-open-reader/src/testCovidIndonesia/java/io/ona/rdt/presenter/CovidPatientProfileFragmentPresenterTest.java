package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientProfileFragmentInteractor;
import timber.log.Timber;

@PrepareForTest({RDTApplication.class, Timber.class})
public class CovidPatientProfileFragmentPresenterTest extends PowerMockTest {

    private CovidPatientProfileFragmentPresenter covidPatientProfileFragmentPresenter;
    private CovidPatientProfileFragmentInteractor interactor;

    @Before
    public void setUp() throws Exception {
        mockStaticMethods();
        covidPatientProfileFragmentPresenter = new CovidPatientProfileFragmentPresenter(null);
        interactor = Mockito.mock(CovidPatientProfileFragmentInteractor.class);
        ReflectionHelpers.setField(covidPatientProfileFragmentPresenter, "interactor", interactor);
    }

    @Test
    public void testLaunchFormShouldVerifyCorrectFormLaunch() throws JSONException {

        Mockito.doNothing().when(interactor).launchForm(ArgumentMatchers.any(Activity.class), ArgumentMatchers.anyString(), ArgumentMatchers.any(Patient.class));

        Activity activity = Mockito.mock(Activity.class);
        String formName = "dummyForm";
        Patient patient = Mockito.mock(Patient.class);

        covidPatientProfileFragmentPresenter.launchForm(activity, formName, patient);
        Mockito.verify(interactor).launchForm(activity, formName, patient);
    }

    @Test
    public void testCatchExceptionShouldReturnJSONException() throws JSONException {
        PowerMockito.mockStatic(Timber.class);
        Mockito.doThrow(JSONException.class).when(interactor).launchForm(null, null, null);

        covidPatientProfileFragmentPresenter.launchForm(null, null, null);
        PowerMockito.verifyStatic(Timber.class);
        Timber.e(ArgumentMatchers.any(JSONException.class));
    }

    private void mockStaticMethods() {
        PowerMockito.mockStatic(RDTApplication.class);

        RDTApplication rdtApplication = Mockito.mock(RDTApplication.class);
        Context context = Mockito.mock(Context.class);
        EventClientRepository eventClientRepository = Mockito.mock(EventClientRepository.class);

        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(context);
        PowerMockito.when(context.getEventClientRepository()).thenReturn(eventClientRepository);
    }
}
