package io.ona.rdt_app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.R;
import io.ona.rdt_app.activity.RDTJsonFormActivity;
import io.ona.rdt_app.contract.RDTJsonFormFragmentContract;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt_app.util.Constants;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment {

    private final String TAG = RDTJsonFormFragment.class.getName();
    private static int currentStep;
    private boolean moveBackOneStep = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isMoveBackOneStep()) {
            getActivity().onBackPressed();
            moveBackOneStep = false;
        }
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        String stepNum = stepName.substring(4);
        currentStep = Integer.valueOf(stepNum);
        RDTJsonFormFragment jsonFormFragment = new RDTJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected void initializeBottomNavigation(final JSONObject step, View rootView) {
        super.initializeBottomNavigation(step, rootView);
        // Handle initialization of the countdown timer bottom navigation
        String currStep = "step" + currentStep;
        boolean buttonEnabled = true;
        if ("step12".equals(currStep)) {
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
                String currStepStr = "step" + currentStep;
                if ("step8".equals(currStepStr)) {
                    String rdtType = ((RDTJsonFormActivity) getActivity()).getRdtType();
                    if (Constants.CARESTART_RDT.equals(rdtType)) {
                        JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment("step14");
                        transactThis(nextFragment);
                    } else {
                        next();
                    }
                } else if ("step5".equals(currStepStr)) {
                    try {
                        ((RDTJsonFormFragmentContract.Presenter) presenter).saveForm();
                        next();
                    } catch (JSONException e) {
                        Log.e(TAG, e.getStackTrace().toString());
                    }
                } else if (isSubmit != null && Boolean.valueOf(isSubmit.toString())) {
                    save(false);
                } else {
                    next();
                }
            }
        });
    }

    public void setNextButtonState(View rootView, boolean buttonEnabled) {
        Button button = rootView.findViewById(com.vijay.jsonwizard.R.id.next_button);
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

    @Override
    public void backClick() {
        AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.AppThemeAlertDialog).setTitle(R.string.confirm_close_title)
                .setMessage(R.string.confirm_close_msg).setNegativeButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        CountDownTimerFactory.stopAlarm();
                    }
                }).setPositiveButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "No button on dialog in " + JsonFormActivity.class.getCanonicalName());
                    }
                }).create();

        dialog.show();
    }

    public boolean isMoveBackOneStep() {
        return moveBackOneStep;
    }

    public void setMoveBackOneStep(boolean moveBackOneStep) {
        this.moveBackOneStep = moveBackOneStep;
    }
}
