package io.ona.rdt.presenter;

import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;

/**
 * Created by Vincent Karuri on 14/07/2020
 */
public class CovidJsonFormFragmentPresenter extends RDTJsonFormFragmentPresenter {

    public CovidJsonFormFragmentPresenter(RDTJsonFormFragment rdtFormFragment, JsonFormInteractor jsonFormInteractor) {
        super(rdtFormFragment, jsonFormInteractor);
    }

    @Override
    public void performNextButtonAction(String currentStep, Object isSubmit) {
        handleCommonTestFormClicks(isSubmit, currentStep);
    }
}
