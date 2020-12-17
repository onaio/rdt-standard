package io.ona.rdt.robolectric.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.interactor.CovidPatientRegisterActivityInteractor;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

public class CovidPatientRegisterActivityPresenterTest extends RobolectricTest {

    private CovidPatientRegisterActivityPresenter presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new CovidPatientRegisterActivityPresenter(null);
    }

    @Test
    public void testInitializeInteractor() throws Exception {
        PatientRegisterActivityInteractor interactor = Whitebox.invokeMethod(presenter, "initializeInteractor");
        Assert.assertEquals(CovidPatientRegisterActivityInteractor.class.getName(), interactor.getClass().getName());
    }

    @Test
    public void testInitializeFormUtils() throws Exception {
        RDTJsonFormUtils rdtJsonFormUtils = Whitebox.invokeMethod(presenter, "initializeFormUtils");
        Assert.assertEquals(CovidRDTJsonFormUtils.class.getName(), rdtJsonFormUtils.getClass().getName());
    }
}
