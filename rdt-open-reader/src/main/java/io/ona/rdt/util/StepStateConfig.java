package io.ona.rdt.util;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by Vincent Karuri on 29/11/2019
 */
public class StepStateConfig {

    private static final String STEP_STATE_CONFIG_FILE = "step-config/step-state-config.json";
    private static StepStateConfig stepStateConfig;
    private static JSONObject stepStateObj;

    private StepStateConfig() { }

    public static StepStateConfig getInstance(Context context) {
        try {
            if (stepStateConfig == null) {
                stepStateConfig = new StepStateConfig();
                JSONObject configJSONObj = new RDTJsonFormUtils().getFormJsonObject(STEP_STATE_CONFIG_FILE, context);
                stepStateConfig.setStepStateObj(configJSONObj);
            }
        } catch (JSONException e) {
            Timber.e(e, "Error thrown when parsing StepStateConfig!");
        }
        return stepStateConfig;
    }

    public void destroyInstance() {
        stepStateConfig = null;
    }

    public void setStepStateObj(JSONObject stepStateObj) {
        StepStateConfig.stepStateObj = stepStateObj;
    }

    public JSONObject getStepStateObj() {
        return stepStateObj;
    }
}
