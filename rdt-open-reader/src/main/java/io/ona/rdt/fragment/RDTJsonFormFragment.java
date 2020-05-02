package io.ona.rdt.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.widgets.CountDownTimerFactory;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Step.TWENTY_MIN_COUNTDOWN_TIMER_PAGE;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment implements RDTJsonFormFragmentContract.View {

    private final String TAG = RDTJsonFormFragment.class.getName();
    private static int currentStep = 1; // step of the fragment coming into view
    private static int prevStep; // step of the fragment coming out of view
    private boolean moveBackOneStep = false;
    private View rootLayout;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        String stepNum = stepName.substring(4);
        prevStep = currentStep;
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

        String currStep = "step" + currentStep;
        boolean isNextButtonEnabled = true;
        try {
            // Disable bottom navigation for the 20min countdown timer
            if (is20minTimerPage(currStep)) {
                isNextButtonEnabled = false;
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
        setNextButtonState(rootView.findViewById(com.vijay.jsonwizard.R.id.next_button), isNextButtonEnabled);

        rootView.findViewById(com.vijay.jsonwizard.R.id.previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveForm();
            }
        });

        rootView.findViewById(com.vijay.jsonwizard.R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object isSubmit = v.getTag(R.id.submit);
                getFragmentPresenter().performNextButtonAction(currStep, isSubmit);
            }
        });
    }

    private boolean is20minTimerPage(String currStep) throws JSONException {
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
    public void moveToNextStep() {
        next();
    }

    @Override
    public void saveForm() {
        save(true);
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

    public RDTJsonFormFragmentContract.Presenter getFragmentPresenter() {
        return (RDTJsonFormFragmentContract.Presenter) presenter;
    }

    public static int getCurrentStep() {
        return currentStep;
    }

    public static void setCurrentStep(int currStep) {
        currentStep = currStep;
    }


    /**
     *
     * Replace current fragment in container with the next {@link JsonFormFragment}
     * Also uses the step name as the name of the fragment to be replaced and added to the backstack
     *
     * @param next
     */
    @Override
    public void transactThis(JsonFormFragment next) {
        getActivity().getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(com.vijay.jsonwizard.R.anim.enter_from_right, com.vijay.jsonwizard.R.anim.exit_to_left, com.vijay.jsonwizard.R.anim.enter_from_left,
                        com.vijay.jsonwizard.R.anim.exit_to_right).replace(com.vijay.jsonwizard.R.id.container, next).addToBackStack("step" + prevStep)
                .commitAllowingStateLoss(); // use https://stackoverflow.com/a/10261449/9782187
    }

    public View getRootLayout() {
        return rootLayout;
    }

    public RDTJsonFormActivity getRdtActivity() {
        return (RDTJsonFormActivity) getActivity();
    }
}
