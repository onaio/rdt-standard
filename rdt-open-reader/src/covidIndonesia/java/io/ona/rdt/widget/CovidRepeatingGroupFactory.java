package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 05/11/2020
 */
public class CovidRepeatingGroupFactory extends RepeatingGroupFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        View rootLayout = views.get(0);

        final WidgetArgs widgetArgs = new WidgetArgs()
                .withStepName(stepName)
                .withContext(context)
                .withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withListener(listener)
                .withPopup(popup);

        final MaterialEditText referenceEditText = rootLayout.findViewById(R.id.reference_edit_text);
        final ImageButton doneButton = rootLayout.findViewById(R.id.btn_repeating_group_done);

        doneButton.setOnClickListener(null);
        referenceEditText.setOnEditorActionListener(null);
        referenceEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                updateCountObject(widgetArgs);
                addOnDoneAction(referenceEditText, doneButton, widgetArgs);
            }
        });

        updateCountObject(widgetArgs);
        return views;
    }

    private void updateCountObject(WidgetArgs widgetArgs) {
        try {
            JSONArray stepFields = getStepFields(getJsonApi(widgetArgs).getStep(widgetArgs.getStepName()));
            for (int i = 0; i < stepFields.length(); i++) {
                JSONObject stepObject = stepFields.getJSONObject(i);
                if ((widgetArgs.getJsonObject().getString(JsonFormConstants.KEY) + "_count").equals(stepObject.getString("key"))
                        && StringUtils.isBlank(stepObject.optString(JsonFormConstants.VALUE, ""))) {
                    stepObject.put(JsonFormConstants.VALUE, "0");
                }
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    private JsonApi getJsonApi(WidgetArgs widgetArgs) {
        Context context = widgetArgs.getContext();
        return context instanceof JsonApi ? (JsonApi) context : null;
    }

    private JSONArray getStepFields (JSONObject step) {
        return step.optJSONArray(JsonFormConstants.FIELDS);
    }
}
