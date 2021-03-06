package io.ona.rdt.fragment;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.utils.Utils;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.Step.TWENTY_MIN_COUNTDOWN_TIMER_PAGE;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment implements RDTJsonFormFragmentContract.View {

    private boolean moveBackOneStep = false;
    private View rootLayout;
    private static String currentStep;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentStep = getArguments().getString(JsonFormConstants.STEPNAME);
        rootLayout = super.onCreateView(inflater, container, savedInstanceState);
        return rootLayout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isMoveBackOneStep()) {
            getActivity().onBackPressed();
            moveBackOneStep = false;
        }
    }

    public static JsonFormFragment getFormFragment(String stepName) {
        RDTJsonFormFragment jsonFormFragment = new RDTJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected void initializeBottomNavigation(final JSONObject step, View rootView) {
        super.initializeBottomNavigation(step, rootView);

        String currStep = getCurrentStep();
        boolean isNextButtonEnabled = true;

        // Disable bottom navigation for the 20min countdown timer
        if (is20minTimerPage(currStep)) {
            isNextButtonEnabled = false;
        }

        setNextButtonState(rootView.findViewById(com.vijay.jsonwizard.R.id.next_button), isNextButtonEnabled);

        rootView.findViewById(com.vijay.jsonwizard.R.id.previous_button).setOnClickListener(v -> {
            getJsonApi().setPreviousPressed(true);
            saveForm();
        });

        rootView.findViewById(com.vijay.jsonwizard.R.id.next_button).setOnClickListener(v -> {
            getJsonApi().setPreviousPressed(false);
            Object isSubmit = v.getTag(R.id.submit);
            String eventType = getJsonApi().getmJSONObject().optString(ENCOUNTER_TYPE);
            if (formHasSpecialNavigationRules(eventType)) {
                getFragmentPresenter().performNextButtonAction(currStep, isSubmit);
            } else {
                getFragmentPresenter().submitOrMoveToNextStep(isSubmit);
            }
        });
    }

    protected boolean formHasSpecialNavigationRules(String formName) {
        return RDT_TEST.equals(formName);
    }

    protected boolean is20minTimerPage(String currStep) {
        return currStep.equals(RDTApplication.getInstance().getStepStateConfiguration()
                .getStepStateObj()
                .optString(TWENTY_MIN_COUNTDOWN_TIMER_PAGE));
    }

    @Override
    public void setNextButtonState(View rootView, boolean buttonEnabled) {
        rootView.setEnabled(buttonEnabled);
        int bgColor;
        if (!buttonEnabled) {
            bgColor = Color.parseColor("#D1D1D1");
        } else {
            bgColor = Color.parseColor("#0192D4");
        }
        Drawable background = rootView.getBackground();
        if (background instanceof ShapeDrawable) {
            ((ShapeDrawable) background).getPaint().setColor(bgColor);
        } else if (background instanceof GradientDrawable) {
            ((GradientDrawable) background).setColor(bgColor);
        } else if (background instanceof ColorDrawable) {
            ((ColorDrawable) background).setColor(bgColor);
        }
    }

    @Override
    public void navigateToNextStep() {
        next();
    }

    @Override
    public void saveForm() {
        save(false);
    }

    @Override
    public void transactFragment(JsonFormFragment nextFragment) {
        transactThis(nextFragment);
    }

    @Override
    public String getRDTType() {
        return ((RDTJsonFormActivity) getActivity()).getRdtType();
    }

    @Override
    public boolean save(boolean skipValidation) {
        return super.save(skipValidation) && presenter.isFormValid();
    }

    @Override
    protected JsonFormFragmentPresenter createPresenter() {
        return new RDTJsonFormFragmentPresenter(this, RDTJsonFormInteractor.getInstance());
    }

    @Override
    public void backClick() {
        DialogInterface.OnClickListener negativeOnClickListener = (dialog, which) -> {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
            CountDownTimerFactory.stopAlarm();
        };

        DialogInterface.OnClickListener positiveOnClickListener = (dialog, which) -> dialog.dismiss();

        Utils.showAlertDialog(getContext(), getString(R.string.confirm_close_title), getString(R.string.confirm_close_msg),
                getString(R.string.yes), getString(R.string.no), negativeOnClickListener, positiveOnClickListener);
    }

    public boolean isMoveBackOneStep() {
        return moveBackOneStep;
    }

    public void setMoveBackOneStep(boolean moveBackOneStep) {
        this.moveBackOneStep = moveBackOneStep;
    }

    public RDTJsonFormFragmentContract.Presenter getFragmentPresenter() {
        return (RDTJsonFormFragmentContract.Presenter) presenter;
    }

    public static String getCurrentStep() {
        return currentStep;
    }

    public View getRootLayout() {
        return rootLayout;
    }

    public RDTJsonFormActivity getRdtActivity() {
        return (RDTJsonFormActivity) getActivity();
    }
}
