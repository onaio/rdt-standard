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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;

/**
 * Created by Vincent Karuri on 20/06/2019
 */
public class RDTLabelFactory extends LabelFactory implements View.OnClickListener {

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

        enhanceLabels(views, jsonObject, formFragment);
        return views;
    }

    private void enhanceLabels(List<View> views, JSONObject jsonObject, JsonFormFragment formFragment) throws JSONException {
        ConstraintLayout rootLayout = (ConstraintLayout) views.get(0);
        CustomTextView labelText = rootLayout.findViewById(com.vijay.jsonwizard.R.id.label_text);
        if (jsonObject.optBoolean(CENTER_LABEL)) {
            labelText.setGravity(Gravity.CENTER);
        }

        if (jsonObject.optBoolean(HAS_DRAWABLE_END)) {
            labelText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.ic_next_arrow, 0);
            labelText.setOnClickListener(this);
        }
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, final JsonFormFragment formFragment,
                                       final JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = widgetArgs.getJsonObject();
        RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
        final String key = jsonObject.optString(KEY, "");
        if (Constants.LBL_CARE_START.equals(key)) {
            formFragment.getRdtActivity().setRdtType(Constants.CARESTART_RDT);
        } else {
            formFragment.getRdtActivity().setRdtType(Constants.ONA_RDT);
        }
        ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(jsonObject.optString(NEXT));
    }
}
