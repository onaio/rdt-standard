package io.ona.rdt.robolectric.interactor;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.interactor.CovidPatientRegisterFragmentInteractor;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

public class CovidPatientRegisterFragmentInteractorTest extends RobolectricTest {

    private CovidPatientRegisterFragmentInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new CovidPatientRegisterFragmentInteractor();
    }

    @Test
    public void testCreateFormLauncherShouldReturnCovidFormLauncher() throws Exception {
        FormLauncher formLauncher = Whitebox.invokeMethod(interactor, "createFormLauncher");
        Assert.assertEquals(CovidFormLauncher.class.getName(), formLauncher.getClass().getName());
    }

    @Test
    public void testCreateFormSaverShouldReturnCovidFormSaver() throws Exception {
        FormSaver formSaver = Whitebox.invokeMethod(interactor, "createFormSaver");
        Assert.assertEquals(CovidFormSaver.class.getName(), formSaver.getClass().getName());
    }
}
