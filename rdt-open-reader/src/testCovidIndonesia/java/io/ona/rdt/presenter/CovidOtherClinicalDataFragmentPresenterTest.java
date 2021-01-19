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

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidOtherClinicalDataFragmentInteractor;
import timber.log.Timber;

@PrepareForTest(Timber.class)
public class CovidOtherClinicalDataFragmentPresenterTest extends PowerMockTest {

    private CovidOtherClinicalDataFragmentPresenter covidOtherClinicalDataFragmentPresenter;
    private CovidOtherClinicalDataFragmentInteractor interactor;

    @Before
    public void setUp() {
        covidOtherClinicalDataFragmentPresenter = new CovidOtherClinicalDataFragmentPresenter(null);
        interactor = Mockito.mock(CovidOtherClinicalDataFragmentInteractor.class);
        ReflectionHelpers.setField(covidOtherClinicalDataFragmentPresenter, "interactor", interactor);
    }

    @Test
    public void testLaunchFormShouldVerifyCorrectFormLaunch() throws JSONException {
        Mockito.doNothing().when(interactor).launchForm(ArgumentMatchers.any(Activity.class), ArgumentMatchers.anyString(), ArgumentMatchers.any(Patient.class));

        covidOtherClinicalDataFragmentPresenter.launchForm(null, null, null);
        Mockito.verify(interactor).launchForm(null, null, null);
    }

    @Test
    public void testCatchExceptionShouldReturnJSONException() throws JSONException {
        PowerMockito.mockStatic(Timber.class);
        Mockito.doThrow(JSONException.class).when(interactor).launchForm(null, null, null);

        covidOtherClinicalDataFragmentPresenter.launchForm(null, null, null);
        PowerMockito.verifyStatic(Timber.class);
        Timber.e(ArgumentMatchers.any(JSONException.class));
    }
}
