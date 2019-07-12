package io.ona.rdt_app.widget;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.R;

public class RDTCountdownTimerFactory extends CountDownTimerFactory {

    private static final String COUNTDOWN_TIMER_RESULT_READY_KEY = "countdown_timer_results_ready";
    private JsonFormFragment formFragment;
    private JSONObject stepObject;
    private View rootLayout;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.formFragment = formFragment;
        this.stepObject = jsonObject;
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        rootLayout = views.get(0);
        return views;
    }

    @Override
    public void onCountdownFinish(Context context) {
        super.onCountdownFinish(context);
        if (stepObject.optString(JsonFormConstants.KEY).equals(COUNTDOWN_TIMER_RESULT_READY_KEY)) {
            ((TextView) rootLayout.findViewById(R.id.timerLabel)).setText(context.getString(R.string.time_expired));
            stopAlarm();
        } else {
            formFragment.next();
        }
    }
}
