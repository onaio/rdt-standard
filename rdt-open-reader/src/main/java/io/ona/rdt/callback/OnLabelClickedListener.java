package io.ona.rdt.callback;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;

/**
 * Created by Vincent Karuri on 27/11/2019
 */
public class OnLabelClickedListener implements View.OnClickListener {

    protected final StepStateConfig stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();
    protected RDTJsonFormFragment formFragment;
    protected String key;

    public OnLabelClickedListener(WidgetArgs widgetArgs) {
        this.formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
        this.key = widgetArgs.getJsonObject().optString(KEY, "");
    }

    @Override
    public void onClick(View v) {
        ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(getNextStep());
    }

    protected String getNextStep() {
        String nextStep = "";
        switch (key) {
            case Constants.FormFields.LBL_CARE_START:
                formFragment.getRdtActivity().setRdtType(Constants.RDTType.CARESTART_RDT);
                nextStep = stepStateConfig.getStepStateObj().optString(SCAN_CARESTART_PAGE);
                break;
            case Constants.FormFields.LBL_SCAN_QR_CODE:
                formFragment.getRdtActivity().setRdtType(Constants.RDTType.ONA_RDT);
                nextStep = stepStateConfig.getStepStateObj().optString(SCAN_QR_PAGE);
                break;
        }
        return nextStep;
    }
}
