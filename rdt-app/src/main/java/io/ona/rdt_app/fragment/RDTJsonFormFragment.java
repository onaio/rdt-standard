package io.ona.rdt_app.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONObject;

import io.ona.rdt_app.R;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt_app.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment {

    private static int currentStep;

    private final String TAG = RDTJsonFormFragment.class.getName();
    private RDTJsonFormUtils jsonFormUtils = new RDTJsonFormUtils();

    public static JsonFormFragment getFormFragment(String stepName) {
        String stepNum = stepName.substring(stepName.length() - 1);
        currentStep = Integer.valueOf(stepNum);
        RDTJsonFormFragment jsonFormFragment = new RDTJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected void initializeBottomNavigation(final JSONObject step, final View rootView) {
        super.initializeBottomNavigation(step, rootView);
        // Handle initialization of the countdown timer bottom navigation
        String currStep = "step" + currentStep;
        boolean buttonEnabled = true;
        if ("step7".equals(currStep)) {
            buttonEnabled = false;
        }
        setNextButtonState(rootView, buttonEnabled);

        rootView.findViewById(com.vijay.jsonwizard.R.id.previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(false);
            }
        });
        rootView.findViewById(com.vijay.jsonwizard.R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object isSubmit = v.getTag(R.id.submit);
                if (isSubmit != null && Boolean.valueOf(isSubmit.toString())) {
                    save(false);
                } else {
                    next();
                }
            }
        });
    }

    public void setNextButtonState(View rootView, boolean buttonEnabled) {
        Button button = rootView.findViewById(com.vijay.jsonwizard.R.id.next_button);
        if (button.isEnabled() == buttonEnabled) {
            return;
        }
        button.setEnabled(buttonEnabled);
        int bgColor;
        if (!buttonEnabled) {
            bgColor = Color.parseColor("#D1D1D1");
        } else {
            bgColor = Color.parseColor("#0192D4");
        }
        Drawable background = button.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(bgColor);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(bgColor);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(bgColor);
        }
    }

    @Override
    public boolean save(boolean skipValidation) {
        return super.save(skipValidation) && presenter.isFormValid();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        presenter = new RDTJsonFormFragmentPresenter(this, new RDTJsonFormInteractor());
        return presenter;
    }
}
