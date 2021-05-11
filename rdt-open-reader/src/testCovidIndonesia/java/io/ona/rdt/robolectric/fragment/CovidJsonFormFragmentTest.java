package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentFactory;
import androidx.fragment.app.testing.FragmentScenario;

import com.vijay.jsonwizard.interfaces.JsonApi;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;

public class CovidJsonFormFragmentTest extends FragmentRobolectricTest {

    private FragmentScenario<CovidJsonFormFragment> fragmentScenario;
    private CovidJsonFormFragment covidJsonFormFragment;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", "step1");
        fragmentScenario = FragmentScenario.launchInContainer(CovidJsonFormFragment.class, bundle, R.style.AppTheme, new FragmentFactory(){
            @NonNull
            @NotNull
            @Override
            public Fragment instantiate(@NonNull @NotNull ClassLoader classLoader, @NonNull @NotNull String className) {
                CovidJsonFormFragment jsonFormFragment = new CovidJsonFormFragment();
                JsonApi jsonApi = Mockito.mock(JsonApi.class);
                jsonFormFragment.setmJsonApi(jsonApi);
                ReflectionHelpers.setField(jsonFormFragment, "presenter", Mockito.mock(CovidJsonFormFragmentPresenter.class));
                return jsonFormFragment;
            }
        });
        fragmentScenario.onFragment(fragment -> covidJsonFormFragment = fragment);
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
        RDTJsonFormFragmentPresenter presenter = Whitebox.invokeMethod(covidJsonFormFragment, "createPresenter");
        Assert.assertEquals(CovidJsonFormFragmentPresenter.class.getName(), presenter.getClass().getName());
    }

    @Override
    public FragmentScenario<CovidJsonFormFragment> getFragmentScenario() {
        return fragmentScenario;
    }
}
