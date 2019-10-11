package io.ona.rdt_app.presenter;

import android.util.Log;
import android.widget.LinearLayout;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.contract.RDTJsonFormFragmentContract;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.util.Constants;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public class RDTJsonFormFragmentPresenter extends JsonFormFragmentPresenter implements RDTJsonFormFragmentContract.Presenter {

    private final String TAG = RDTJsonFormFragmentPresenter.class.getName();

    private RDTJsonFormInteractor interactor;
    private RDTJsonFormFragmentContract.View formFragment;

    public RDTJsonFormFragmentPresenter(RDTJsonFormFragmentContract.View formFragment, JsonFormInteractor jsonFormInteractor) {
        super((JsonFormFragment) formFragment, jsonFormInteractor);
        this.interactor = (RDTJsonFormInteractor) jsonFormInteractor;
        this.formFragment = formFragment;
    }

    @Override
    public boolean onNextClick(LinearLayout mainView) {
        checkAndStopCountdownAlarm();
        this.validateAndWriteValues();
        boolean validateOnSubmit = this.validateOnSubmit();
        if (validateOnSubmit || this.isFormValid()) {
            return this.moveToNextStep();
        } else {
            this.getView().showSnackBar(this.getView().getContext().getResources().getString(com.vijay.jsonwizard.R.string.json_form_on_next_error_msg));
            return false;
        }
    }

    private boolean moveToNextStep() {
        if (hasNextStep()) {
            JsonFormFragment next = RDTJsonFormFragment.getFormFragment(this.mStepDetails.optString("next"));
            this.getView().hideKeyBoard();
            this.getView().transactThis(next);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasNextStep() {
        return !mStepDetails.optString("next").isEmpty();
    }

    public void moveToNextStep(String stepName) {
        JsonFormFragment next = RDTJsonFormFragment.getFormFragment(stepName);
        this.getView().hideKeyBoard();
        this.getView().transactThis(next);
    }

    @Override
    public void setUpToolBar() {
        super.setUpToolBar();
        getView().updateVisibilityOfNextAndSave(false, false);
    }

    @Override
    public void saveForm() throws JSONException {
        interactor.saveForm(new JSONObject(getView().getCurrentJsonState()));
    }

    @Override
    public void performNextButtonAction(String currentStep, Object isSubmit) {
        if ("step9".equals(currentStep)) {
            String rdtType = formFragment.getRDTType();
            if (Constants.CARESTART_RDT.equals(rdtType)) {
                JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment("step15");
                formFragment.transactFragment(nextFragment);
            } else {
                formFragment.moveToNextStep();
            }
        } else if ("step6".equals(currentStep)) {
            try {
                saveForm();
                formFragment.moveToNextStep();
            } catch (JSONException e) {
                Log.e(TAG, e.getStackTrace().toString());
            }
        } else if (isSubmit != null && Boolean.valueOf(isSubmit.toString())) {
            formFragment.saveForm();
        } else {
            formFragment.moveToNextStep();
        }
    }
}
