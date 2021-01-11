package io.ona.rdt.robolectric.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientProfileFragmentInteractor;
import io.ona.rdt.presenter.CovidPatientProfileFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

public class CovidPatientProfileFragmentPresenterTest extends RobolectricTest {

    private CovidPatientProfileFragmentPresenter covidPatientProfileFragmentPresenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidPatientProfileFragmentPresenter = new CovidPatientProfileFragmentPresenter(null);
    }

    @Test
    public void testLaunchFormShouldVerifyCorrectFormLaunch() throws JSONException {
        Activity activity = Mockito.mock(Activity.class);
        String formName = "formName";
        Patient patient = Mockito.mock(Patient.class);

        CovidPatientProfileFragmentInteractor interactor = Mockito.mock(CovidPatientProfileFragmentInteractor.class);
        ReflectionHelpers.setField(covidPatientProfileFragmentPresenter, "interactor", interactor);

        covidPatientProfileFragmentPresenter.launchForm(activity, formName, patient);
        Mockito.verify(interactor).launchForm(activity, formName, patient);
    }
}
