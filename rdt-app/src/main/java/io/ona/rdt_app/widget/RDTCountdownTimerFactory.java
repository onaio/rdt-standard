package io.ona.rdt_app.widget;

import android.content.Context;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.widget.TextView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.R;

public class RDTCountdownTimerFactory extends CountDownTimerFactory {

    public static final String COUNTDOWN_TIMER_RESULT_READY_KEY = "countdown_timer_results_ready";
    private View rootLayout;
    WidgetArgs widgetArgs;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        widgetArgs = new WidgetArgs();
        widgetArgs.withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withContext(context);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        rootLayout = views.get(0);
        return views;
    }

    @Override
    public void onCountdownFinish(Context context) {
        super.onCountdownFinish(context);
        TextView tvInstructions = rootLayout.findViewById(R.id.timerLabel);
        performOnExpiredAction(context, tvInstructions);
    }

    private void performOnExpiredAction(Context context, TextView tvInstructions) {
        if (widgetArgs.getJsonObject().optString(JsonFormConstants.KEY).equals(COUNTDOWN_TIMER_RESULT_READY_KEY)) {
            tvInstructions.setText(context.getString(R.string.time_expired));
        } else {
            widgetArgs.getFormFragment().next();
        }
        vibrate();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) widgetArgs.getContext().getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(3000, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            vibrator.vibrate(3000);
        }
    }
}
