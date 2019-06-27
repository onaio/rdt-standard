package io.ona.rdt_app.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.views.CustomTextView;
import com.vijay.jsonwizard.widgets.LabelFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.R;
import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;

import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;

/**
 * Created by Vincent Karuri on 20/06/2019
 */
public class RDTLabelFactory extends LabelFactory {

    private String HAS_DRAWABLE_END = "has_drawable_end";

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener
                                               listener, boolean popup) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        ConstraintLayout rootLayout = (ConstraintLayout) views.get(0);
        CustomTextView labelText = rootLayout.findViewById(com.vijay.jsonwizard.R.id.label_text);
        if (jsonObject.optBoolean(HAS_DRAWABLE_END)) {
            labelText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0);
            labelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(jsonObject.optString(NEXT));
                }
            });
        }

        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener listener) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);

        ConstraintLayout rootLayout = (ConstraintLayout) views.get(0);
        CustomTextView labelText = rootLayout.findViewById(com.vijay.jsonwizard.R.id.label_text);
        if (jsonObject.optBoolean(HAS_DRAWABLE_END)) {
            labelText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0);
            labelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(jsonObject.optString(NEXT));
                }
            });
        }

        return views;
    }
}
