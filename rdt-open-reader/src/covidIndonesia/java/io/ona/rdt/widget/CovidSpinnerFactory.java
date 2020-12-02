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

import io.ona.rdt.R;
import io.ona.rdt.util.Utils;

/**
 * Created by Vincent Karuri on 02/12/2020
 */
public class CovidSpinnerFactory extends SpinnerFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        MaterialSpinner spinner = getSpinner((RelativeLayout) views.get(0));
        final AdapterView.OnItemSelectedListener onItemSelectedListener = spinner.getOnItemSelectedListener();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Utils.showToastInFG(context, context.getString(R.string.rdt_not_supported));
                onItemSelectedListener.onItemSelected(parent, view, position, id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                onItemSelectedListener.onNothingSelected(parent);
            }
        });

        return views;
    }

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
