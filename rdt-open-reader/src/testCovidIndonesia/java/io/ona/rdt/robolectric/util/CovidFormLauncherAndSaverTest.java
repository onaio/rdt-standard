package io.ona.rdt.robolectric.util;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormLauncherAndSaver;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

public class CovidFormLauncherAndSaverTest extends RobolectricTest {

    private CovidFormLauncherAndSaver covidFormLauncherAndSaver;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidFormLauncherAndSaver = new CovidFormLauncherAndSaver();
    }

    @Test
    public void testCreateFormLauncher() throws Exception {
        FormLauncher formLauncher = Whitebox.invokeMethod(covidFormLauncherAndSaver, "createFormLauncher");
        Assert.assertEquals(CovidFormLauncher.class.getName(), formLauncher.getClass().getName());
    }

    @Test
    public void testCreateFormSaver() throws Exception {
        FormSaver formSaver = Whitebox.invokeMethod(covidFormLauncherAndSaver, "createFormSaver");
        Assert.assertEquals(CovidFormSaver.class.getName(), formSaver.getClass().getName());
    }
}
