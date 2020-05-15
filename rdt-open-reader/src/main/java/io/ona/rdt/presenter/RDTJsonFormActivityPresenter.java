package io.ona.rdt.presenter;

import org.json.JSONArray;
import org.json.JSONException;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Step.DISABLED_BACK_PRESS_PAGES;
import static io.ona.rdt.util.Utils.isCovidApp;

/**
 * Created by Vincent Karuri on 16/08/2019
 */
public class RDTJsonFormActivityPresenter implements RDTJsonFormActivityContract.Presenter {

    private RDTJsonFormActivityContract.View activity;

    public RDTJsonFormActivityPresenter(RDTJsonFormActivityContract.View activity) {
        this.activity = activity;
    }

    @Override
    public void onBackPress() {
        try {
            String currentStep = "step" + RDTJsonFormFragment.getCurrentStep();
            StepStateConfig stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();
            JSONArray disabledBackPressPgs = stepStateConfig.getStepStateObj().optJSONArray(DISABLED_BACK_PRESS_PAGES);
            // disable backpress for timer, rdt capture and expiration date screens
            if (!isDisabledBackPress(disabledBackPressPgs, currentStep) || isCovidApp()) {
                activity.onBackPress();
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    private boolean isDisabledBackPress(JSONArray disabledBackPressPgs, String currentStep) throws JSONException {
        for (int i = 0; i < disabledBackPressPgs.length(); i++) {
            if (currentStep.equals(disabledBackPressPgs.getString(i))) {
                return true;
            }
        }
        return false;
    }
}
