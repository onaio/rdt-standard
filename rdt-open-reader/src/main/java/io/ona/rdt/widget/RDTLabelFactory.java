package io.ona.rdt.widget;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.views.CustomTextView;
import com.vijay.jsonwizard.widgets.LabelFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;

/**
 * Created by Vincent Karuri on 20/06/2019
 */
public class RDTLabelFactory extends LabelFactory {

    private String HAS_DRAWABLE_END = "has_drawable_end";
    private String CENTER_LABEL = "center_label";

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener
                                               listener, boolean popup) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        ConstraintLayout rootLayout = (ConstraintLayout) views.get(0);
        CustomTextView labelText = rootLayout.findViewById(com.vijay.jsonwizard.R.id.label_text);
        if (jsonObject.optBoolean(CENTER_LABEL)) {
            labelText.setGravity(Gravity.CENTER);
        }

        if (jsonObject.optBoolean(HAS_DRAWABLE_END)) {
            final String key = jsonObject.getString(KEY);
            labelText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0);
            labelText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Constants.LBL_CARE_START.equals(key)) {
                        ((RDTJsonFormActivity) formFragment.getActivity()).setRdtType(Constants.CARESTART_RDT);
                    } else {
                        ((RDTJsonFormActivity) formFragment.getActivity()).setRdtType(Constants.ONA_RDT);
                    }
                    ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(jsonObject.optString(NEXT));
                }
            });
        }

        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }
}