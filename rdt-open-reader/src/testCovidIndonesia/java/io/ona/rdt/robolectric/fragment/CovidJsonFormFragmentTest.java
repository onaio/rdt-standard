package io.ona.rdt.robolectric.fragment;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

public class CovidJsonFormFragmentTest extends RobolectricTest {

    private CovidJsonFormFragment covidJsonFormFragment;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidJsonFormFragment = (CovidJsonFormFragment) CovidJsonFormFragment.getFormFragment("step1");
    }

    @Test
    public void testFormHasSpecialNavigationRulesShouldContainsRelevantForms() throws Exception {
        for (String formName : covidJsonFormFragment.formsWithSpecialNavigationRules) {
            Assert.assertTrue(Whitebox.invokeMethod(covidJsonFormFragment, "formHasSpecialNavigationRules", formName));
        }
    }

    @Test
    public void testIs20minTimerPageShouldReturnFalse() throws Exception {
        Assert.assertFalse(Whitebox.invokeMethod(covidJsonFormFragment, "is20minTimerPage", "none"));
    }

    @Test
    public void testCreateRDTJsonFormFragmentPresenterShouldReturnCovidJsonFormFragmentPresenter() throws Exception {
        RDTJsonFormFragmentPresenter presenter = Whitebox.invokeMethod(covidJsonFormFragment, "createRDTJsonFormFragmentPresenter");
        Assert.assertEquals(CovidJsonFormFragmentPresenter.class.getName(), presenter.getClass().getName());
    }
}