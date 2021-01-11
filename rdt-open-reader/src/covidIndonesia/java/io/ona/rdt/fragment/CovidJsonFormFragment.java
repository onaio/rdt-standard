package io.ona.rdt.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormFragment extends RDTJsonFormFragment {

    public Set<String> formsWithSpecialNavigationRules = new HashSet<>(Arrays.asList(RDT_TEST, COVID_RDT_TEST));

    @Override
    protected boolean formHasSpecialNavigationRules(String formName) {
        return formsWithSpecialNavigationRules.contains(formName);
    }

    @Override
    protected boolean is20minTimerPage(String currStep) {
        return false;
    }

    @Override
    protected RDTJsonFormFragmentPresenter createRDTJsonFormFragmentPresenter() {
        return new CovidJsonFormFragmentPresenter(this, RDTJsonFormInteractor.getInstance());
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        String stepNum = stepName.substring(4);
        prevStep = currentStep;
        currentStep = Integer.parseInt(stepNum);
        CovidJsonFormFragment jsonFormFragment = new CovidJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }
}
