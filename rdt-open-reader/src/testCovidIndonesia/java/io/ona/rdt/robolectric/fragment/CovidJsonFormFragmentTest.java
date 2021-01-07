package io.ona.rdt.robolectric.fragment;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidJsonFormFragmentTest extends RobolectricTest {

    private CovidJsonFormFragment covidJsonFormFragment;
    private Set<String> formsWithSpecialNavigationRules = new HashSet<>(Arrays.asList(Constants.Encounter.RDT_TEST, CovidConstants.Encounter.COVID_RDT_TEST));

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidJsonFormFragment = (CovidJsonFormFragment) CovidJsonFormFragment.getFormFragment("step1");
    }

    @Test
    public void testFormHasSpecialNavigationRules() throws Exception {
        for (String formName : formsWithSpecialNavigationRules) {
            Assert.assertTrue(Whitebox.invokeMethod(covidJsonFormFragment, "formHasSpecialNavigationRules", formName));
        }
    }

    @Test
    public void testIs20minTimerPage() throws Exception {
        Assert.assertFalse(Whitebox.invokeMethod(covidJsonFormFragment, "is20minTimerPage", "none"));
    }

    @Test
    public void testCreateRDTJsonFormFragmentPresenter() throws Exception {
        RDTJsonFormFragmentPresenter presenter = Whitebox.invokeMethod(covidJsonFormFragment, "createRDTJsonFormFragmentPresenter");
        Assert.assertEquals(CovidJsonFormFragmentPresenter.class.getName(), presenter.getClass().getName());
    }
}
