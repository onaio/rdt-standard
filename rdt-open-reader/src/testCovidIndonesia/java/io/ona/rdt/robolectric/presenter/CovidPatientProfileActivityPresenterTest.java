package io.ona.rdt.robolectric.presenter;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.interactor.CovidPatientProfileActivityInteractor;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormSaver;

public class CovidPatientProfileActivityPresenterTest extends RobolectricTest {

    private CovidPatientProfileActivityPresenter presenter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        presenter = new CovidPatientProfileActivityPresenter(null);
    }

    @Test
    public void testCreateInteractor() throws Exception {
        FormSaver formSaver = Whitebox.invokeMethod(presenter, "createInteractor");
        Assert.assertEquals(CovidPatientProfileActivityInteractor.class.getName(), formSaver.getClass().getName());
    }
}
