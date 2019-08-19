package io.ona.rdt_app.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;

import com.rey.material.widget.Button;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.GpsFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.fragment.RDTJsonFormFragment;

/**
 * Created by Vincent Karuri on 19/08/2019
 */
public class RDTGpsFactory extends GpsFactory {

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, JsonFormFragment formFragment, JSONObject jsonObject,
                                       CommonListener listener, boolean popup) throws Exception {

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        View rootLayout = views.get(0);

        Pair<Integer, Integer> screenDimens = getScreenDimens(context, formFragment);
        rootLayout.setLayoutParams(new RelativeLayout.LayoutParams(screenDimens.first, screenDimens.second));
        return views;
    }

    private Pair<Integer, Integer> getScreenDimens(Context context, JsonFormFragment formFragment) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        float dpHeight = displayMetrics.heightPixels;

        return new Pair<>((int) dpWidth, (int) dpHeight);
    }

    @Override
    protected void customizeViews(Button recordButton, Context context) {
        // do nothing
    }

    @Override
    protected void showGpsDialog() {
        super.showGpsDialog();
    }
}
