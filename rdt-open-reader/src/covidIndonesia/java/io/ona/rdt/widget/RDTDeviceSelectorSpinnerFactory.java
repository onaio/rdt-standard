package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.customviews.MaterialSpinner;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.SpinnerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import io.ona.rdt.util.CovidRDTJsonFormUtils;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 02/12/2020
 */
public class RDTDeviceSelectorSpinnerFactory extends SpinnerFactory {

    private final CovidRDTJsonFormUtils formUtils = new CovidRDTJsonFormUtils();

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {

        WidgetArgs widgetArgs = new WidgetArgs().withFormFragment(formFragment).withContext(context)
                .withStepName(stepName).withJsonObject(jsonObject).withPopup(popup).withListener(listener);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        MaterialSpinner spinner = getSpinner((RelativeLayout) views.get(0));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                listener.onItemSelected(parent, view, position, id);
                if (position > -1) {
                    populateRDTConfirmationPageDetails(widgetArgs);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                listener.onNothingSelected(parent);
            }
        });

        return views;
    }

    private void populateRDTConfirmationPageDetails(WidgetArgs widgetArgs) {
        try {
            JSONObject rdtTypeField = formUtils.getField(widgetArgs.getStepName(),
                    widgetArgs.getJsonObject().getString(JsonFormConstants.KEY), widgetArgs.getContext());
            String deviceId = rdtTypeField.getString(JsonFormConstants.VALUE);
            formUtils.populateRDTDetailsConfirmationPage(widgetArgs, deviceId);
        } catch (JSONException | IOException | FHIRParserException e) {
            Timber.e(e);
        }
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
