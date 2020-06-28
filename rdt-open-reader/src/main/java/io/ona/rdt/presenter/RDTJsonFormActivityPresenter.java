package io.ona.rdt.presenter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.Step.COVID_DISABLED_BACK_PRESS_PAGES;
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
            // disable backpress for timer, rdt capture and expiration date screens
            if (!isDisabledBackPress(currentStep, activity.getmJSONObject().optString(ENCOUNTER_TYPE))) {
                activity.onBackPress();
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    private boolean isDisabledBackPress(String currentStep, String encounterType) throws JSONException {
        JSONObject stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration().getStepStateObj();
        JSONArray malariaDisabledBackPressPgs = stepStateConfig.optJSONArray(DISABLED_BACK_PRESS_PAGES);
        JSONArray covidDisabledBackPressPgs = stepStateConfig.optJSONArray(COVID_DISABLED_BACK_PRESS_PAGES);
        for (int i = 0; i < malariaDisabledBackPressPgs.length(); i++) {
            if (isCovidApp() && RDT_TEST.equals(encounterType)
                    && currentStep.equals(covidDisabledBackPressPgs.getString(i))) {
                return true;
            } else if (currentStep.equals(malariaDisabledBackPressPgs.getString(i))) {
                return true;
            }
        }
        return false;
    }
}
