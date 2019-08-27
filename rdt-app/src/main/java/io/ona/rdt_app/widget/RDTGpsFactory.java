package io.ona.rdt_app.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ScrollView;

import com.rey.material.widget.Button;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.GpsFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.R;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;
import io.ona.rdt_app.util.RDTGpsDialog;

/**
 * Created by Vincent Karuri on 19/08/2019
 */
public class RDTGpsFactory extends GpsFactory {

    private WidgetArgs widgetArgs;

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, JsonFormFragment formFragment, JSONObject jsonObject,
                                       CommonListener listener, boolean popup) throws Exception {

        widgetArgs = new WidgetArgs();
        widgetArgs.withStepName(stepName)
                .withContext(context)
                .withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withListener(listener)
                .withPopup(popup);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        View rootLayout = views.get(0);

        hideTextFields(rootLayout);

        stretchWidgetToFullScreen(formFragment, context);

        new RDTJsonFormFragment().setNextButtonState(rootLayout.findViewById(R.id.record_button), true);

        return views;
    }

    private void hideTextFields(View rootLayout) {
        rootLayout.findViewById(R.id.altitude).setVisibility(View.GONE);
        rootLayout.findViewById(R.id.accuracy).setVisibility(View.GONE);
    }

    private void stretchWidgetToFullScreen(JsonFormFragment formFragment, Context context) {
        int margin = convertDpToPixels(context, context.getResources().getDimension(com.vijay.jsonwizard.R.dimen.bottom_navigation_margin));
        ScrollView scrollView = ((RDTJsonFormFragment) formFragment).getRootLayout().findViewById(com.vijay.jsonwizard.R.id.scroll_view);
        scrollView.setFillViewport(true);
        scrollView.setPadding(0, 0, 0, margin);
        scrollView.findViewById(com.vijay.jsonwizard.R.id.main_layout).setPadding(margin, 0, margin, 0);
    }

    private int convertDpToPixels(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();

        return (int) (dp * displayMetrics.density);
    }

    @Override
    protected void customizeViews(Button recordButton, Context context) {
        gpsDialog = new RDTGpsDialog(gpsDialog);
        gpsDialog.setTitle("Please wait");
        ((RDTGpsDialog) gpsDialog).setFormFragment(widgetArgs.getFormFragment());
    }
}
