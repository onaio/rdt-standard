package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;

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
                addOnDoneAction(referenceEditText, doneButton, widgetArgs);
            }
        });
        return views;
    }
}
