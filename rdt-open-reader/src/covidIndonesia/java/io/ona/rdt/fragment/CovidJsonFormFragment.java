package io.ona.rdt.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormFragment extends RDTJsonFormFragment {

    private Set<String> formsWithSpecialNavigationRules = new HashSet<>(Arrays.asList(RDT_TEST, COVID_RDT_TEST));

    @Override
    protected boolean formHasSpecialNavigationRules(String formName) {
        return formsWithSpecialNavigationRules.contains(formName);
    }

    @Override
    protected boolean is20minTimerPage(String currStep) {
        return false;
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        presenter = new CovidJsonFormFragmentPresenter(this, RDTJsonFormInteractor.getInstance());
        return presenter;
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
