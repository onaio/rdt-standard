package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.PowerMockTest;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidOtherClinicalDataFragmentInteractor;

public class CovidOtherClinicalDataFragmentPresenterTest extends PowerMockTest {

    private CovidOtherClinicalDataFragmentPresenter covidOtherClinicalDataFragmentPresenter;

    @Before
    public void setUp() {
        covidOtherClinicalDataFragmentPresenter = new CovidOtherClinicalDataFragmentPresenter(null);
    }

    @Test
    public void testLaunchForm() throws JSONException {
        Activity activity = Mockito.mock(Activity.class);
        String formName = "formName";
        Patient patient = Mockito.mock(Patient.class);

        CovidOtherClinicalDataFragmentInteractor interactor = Mockito.mock(CovidOtherClinicalDataFragmentInteractor.class);
        ReflectionHelpers.setField(covidOtherClinicalDataFragmentPresenter, "interactor", interactor);

        covidOtherClinicalDataFragmentPresenter.launchForm(activity, formName, patient);
        Mockito.verify(interactor).launchForm(activity, formName, patient);
    }
}
