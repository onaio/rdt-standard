package io.ona.rdt.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.views.CustomTextView;
import com.vijay.jsonwizard.widgets.LabelFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.callback.OnLabelClickedListener;

/**
 * Created by Vincent Karuri on 20/06/2019
 */
public class RDTLabelFactory extends LabelFactory {

    public static String HAS_DRAWABLE_END = "has_drawable_end";
    public static String CENTER_LABEL = "center_label";
    private WidgetArgs widgetArgs;

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener
                                               listener, boolean popup) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        widgetArgs = new WidgetArgs();
        widgetArgs.withJsonObject(jsonObject)
                .withFormFragment(formFragment)
                .withContext(context)
                .withStepName(stepName);

        enhanceLabels(views, jsonObject);
        return views;
    }

    private void enhanceLabels(List<View> views, JSONObject jsonObject)  {
        if (views.isEmpty()) { return; }

        ConstraintLayout rootLayout = (ConstraintLayout) views.get(0);
        CustomTextView labelText = rootLayout.findViewById(com.vijay.jsonwizard.R.id.label_text);
        if (jsonObject.optBoolean(CENTER_LABEL)) {
            labelText.setGravity(Gravity.CENTER);
        }

        if (jsonObject.optBoolean(HAS_DRAWABLE_END)) {
            labelText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0);
            labelText.setOnClickListener(new OnLabelClickedListener(widgetArgs));
        }
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }
}
