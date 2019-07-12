package io.ona.rdt_app.widget;

import android.content.Context;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONObject;

import java.util.List;

public class RDTCountdownTimerFactory extends CountDownTimerFactory {

    private JsonFormFragment formFragment;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.formFragment = formFragment;
        return super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
    }

    @Override
    public void onCountdownFinish(Context context) {
        super.onCountdownFinish(context);
        // Go to next
        formFragment.next();
    }
}
