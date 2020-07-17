package io.ona.rdt.fragment;

import android.os.Bundle;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.presenter.CovidJsonFormFragmentPresenter;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormFragment extends RDTJsonFormFragment {

    @Override
    protected boolean formHasSpecialNavigationRules(String eventType) {
        return RDT_TEST.equals(eventType) || COVID_RDT_TEST.equals(eventType);
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
