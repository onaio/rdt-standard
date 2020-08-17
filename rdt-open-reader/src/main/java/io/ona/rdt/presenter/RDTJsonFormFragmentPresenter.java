package io.ona.rdt.presenter;

import android.widget.LinearLayout;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.interactor.RDTJsonFormFragmentInteractor;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.FormFields.MANUAL_EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.Step.BLOT_PAPER_TASK_PAGE;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;
import static io.ona.rdt.util.Constants.Step.TAKE_IMAGE_OF_RDT_PAGE;
import static io.ona.rdt.util.Utils.isExpired;
import static io.ona.rdt.widget.RDTExpirationDateReaderFactory.conditionallyMoveToNextStep;
import static org.smartregister.util.JsonFormUtils.FIELDS;
import static org.smartregister.util.JsonFormUtils.getFieldValue;
import static org.smartregister.util.JsonFormUtils.getJSONArray;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public class RDTJsonFormFragmentPresenter extends JsonFormFragmentPresenter implements RDTJsonFormFragmentContract.Presenter {

    private RDTJsonFormFragmentContract.View rdtFormFragment;
    private StepStateConfig stepStateConfig;
    private RDTJsonFormFragmentInteractor rdtJsonFormFragmentInteractor;

    public RDTJsonFormFragmentPresenter(RDTJsonFormFragmentContract.View rdtFormFragment, JsonFormInteractor jsonFormInteractor) {
        super((JsonFormFragment) rdtFormFragment, jsonFormInteractor);
        this.rdtFormFragment = rdtFormFragment;
        this.rdtJsonFormFragmentInteractor = new RDTJsonFormFragmentInteractor();
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
            JsonFormFragment next = RDTJsonFormFragment.getFormFragment(getNextStep());
            this.getView().hideKeyBoard();
            this.getView().transactThis(next);
            return true;
        }
        return false;
    }

    @Override
    public boolean hasNextStep() {
        return !getNextStep().isEmpty();
    }

    private String getNextStep() {
        return mStepDetails.optString(JsonFormConstants.NEXT);
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
        rdtJsonFormFragmentInteractor.saveForm(new JSONObject(getView().getCurrentJsonState()), null);
    }

    @Override
    public void performNextButtonAction(String currentStep, Object isSubmit) {
        handleMalariaTestFormClicks(getStepStateConfig(), currentStep, isSubmit);
    }

    private void handleMalariaTestFormClicks(StepStateConfig stepStateConfig, String currentStep, Object isSubmit) {
        if (isCurrentStep(BLOT_PAPER_TASK_PAGE, currentStep)) {
            String rdtType = rdtFormFragment.getRDTType();
            if (Constants.RDTType.CARESTART_RDT.equals(rdtType)) {
                JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(
                        stepStateConfig.getStepStateObj().optString(TAKE_IMAGE_OF_RDT_PAGE));

                rdtFormFragment.transactFragment(nextFragment);
            } else {
                rdtFormFragment.navigateToNextStep();
            }
        }  else {
            handleCommonTestFormClicks(isSubmit, currentStep);
        }
    }

    private void saveFormAndMoveToNextStep() throws JSONException {
        saveForm();
        rdtFormFragment.navigateToNextStep();
    }

    @Override
    public void handleCommonTestFormClicks(Object isSubmit, String currentStep) {
        try {
            if (isCurrentStep(PRODUCT_EXPIRED_PAGE, currentStep)) {
                saveFormAndMoveToNextStep();
            } else if (isCurrentStep(MANUAL_EXPIRATION_DATE_ENTRY_PAGE, currentStep)) {
                navigateFromManualExpirationDateEntryPage(getStepStateConfig().getStepStateObj().optString(PRODUCT_EXPIRED_PAGE));;
            }  else {
                submitOrMoveToNextStep(isSubmit);
            }
        } catch (JSONException e) {
            Timber.e(e);
        } catch (ParseException e) {
            Timber.e(e);
        }
    }

    @Override
    public void submitOrMoveToNextStep(Object isSubmit) {
        if (isSubmit != null && Boolean.parseBoolean(isSubmit.toString())) {
            rdtFormFragment.saveForm();
        } else {
            rdtFormFragment.navigateToNextStep();
        }
    }

    private void navigateFromManualExpirationDateEntryPage(String expiredPageStep) throws ParseException {
        JsonFormFragment formFragment = (JsonFormFragment) rdtFormFragment;
        String dateStr = getDateStr(formFragment, stepStateConfig);

        if (StringUtils.isBlank((dateStr))) { return; }

        Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
        conditionallyMoveToNextStep(formFragment, expiredPageStep, isExpired(date));
    }

    private String getDateStr(JsonFormFragment formFragment, StepStateConfig stepStateConfig) {
        JSONObject stepStateObj = stepStateConfig.getStepStateObj();
        return getFieldValue(getJSONArray(formFragment.getStep(
                stepStateObj.optString(MANUAL_EXPIRATION_DATE_ENTRY_PAGE)), FIELDS), MANUAL_EXPIRATION_DATE);
    }

    private boolean isCurrentStep(String key, String currentStep) {
        return currentStep.equals(getStepStateConfig().getStepStateObj().optString(key));
    }

    private StepStateConfig getStepStateConfig() {
        if (this.stepStateConfig == null) {
            this.stepStateConfig =  RDTApplication.getInstance().getStepStateConfiguration();
        }
        return this.stepStateConfig;
    }
}
