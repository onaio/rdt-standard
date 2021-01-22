package io.ona.rdt.robolectric.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt.presenter.CovidPatientRegisterFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

public class CovidPatientRegisterFragmentPresenterTest extends RobolectricTest {

    private CovidPatientRegisterFragmentPresenter presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new CovidPatientRegisterFragmentPresenter(null);
    }

    @Test
    public void testCreateInteractorShouldReturnCovidPatientRegisterFragmentInteractor() throws Exception {
        PatientRegisterFragmentInteractor interactor = Whitebox.invokeMethod(presenter, "createInteractor");
        Assert.assertEquals(CovidPatientRegisterFragmentInteractor.class.getName(), interactor.getClass().getName());
    }
}
