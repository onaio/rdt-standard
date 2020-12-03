package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.vijay.jsonwizard.customviews.MaterialSpinner;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.SpinnerFactory;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Vincent Karuri on 02/12/2020
 */
public class RDTDeviceSelectorSpinnerFactory extends SpinnerFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        MaterialSpinner spinner = getSpinner((RelativeLayout) views.get(0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listener.onNothingSelected(parent);
            }
        });

        return views;
    }

    // This is required since the spinner widget assigns a random id to the spinner (not sure why)
    private MaterialSpinner getSpinner(RelativeLayout relativeLayout) {
        for (int i = 0; i < relativeLayout.getChildCount(); i++) {
            View child = relativeLayout.getChildAt(i);
            if (child instanceof MaterialSpinner) {
                return (MaterialSpinner) child;
            }
        }
        return null;
    }
}
