package io.ona.rdt.robolectric.presenter;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientRegisterActivityInteractor;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

public class CovidPatientRegisterActivityPresenterTest extends RobolectricTest {

    private CovidPatientRegisterActivityPresenter presenter;
    private PatientRegisterActivity activity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = Mockito.mock(PatientRegisterActivity.class);
        presenter = new CovidPatientRegisterActivityPresenter(activity);
    }

    @Test
    public void testInitializeInteractorShouldReturnCovidPatientRegisterActivityInteractor() throws Exception {
        PatientRegisterActivityInteractor interactor = Whitebox.invokeMethod(presenter, "initializeInteractor");
        Assert.assertEquals(CovidPatientRegisterActivityInteractor.class.getName(), interactor.getClass().getName());
    }

    @Test
    public void testInitializeFormUtilsShouldReturnCovidRDTJsonFormUtils() throws Exception {
        RDTJsonFormUtils rdtJsonFormUtils = Whitebox.invokeMethod(presenter, "initializeFormUtils");
        Assert.assertEquals(CovidRDTJsonFormUtils.class.getName(), rdtJsonFormUtils.getClass().getName());
    }

    @Test
    public void testLaunchPostRegistrationViewShouldVerifyStartActivity() throws Exception {
        Whitebox.invokeMethod(presenter, "launchPostRegistrationView", new Patient("", "", ""));
        Mockito.verify(activity).startActivity(ArgumentMatchers.any(Intent.class), ArgumentMatchers.any());
    }
}
