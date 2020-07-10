package io.ona.rdt.callback;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONObject;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static io.ona.rdt.util.Constants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_CONDUCT_RDT_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_ENTER_DELIVERY_DETAILS_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_OPT_IN_WBC_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_SCAN_BARCODE_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_TEST_COMPLETE_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_WBC_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_XRAY_PAGE;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;

/**
 * Created by Vincent Karuri on 27/11/2019
 */
public class OnLabelClickedListener implements View.OnClickListener {

    private WidgetArgs widgetArgs;

    public OnLabelClickedListener(WidgetArgs widgetArgs) {
        this.widgetArgs = widgetArgs;
    }

    @Override
    public void onClick(View v) {
        JSONObject jsonObject = widgetArgs.getJsonObject();
        RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
        final String key = jsonObject.optString(KEY, "");
        StepStateConfig stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();

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
            case Constants.FormFields.LBL_SCAN_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SCAN_BARCODE_PAGE);
                break;
            case Constants.FormFields.LBL_ENTER_RDT_MANUALLY:
                nextStep = stepStateConfig.getStepStateObj().optString(MANUAL_EXPIRATION_DATE_ENTRY_PAGE);
                break;
            case Constants.FormFields.LBL_CONDUCT_RDT:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_CONDUCT_RDT_PAGE);
                break;
            case Constants.FormFields.LBL_SKIP_RDT_TEST:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE);
                break;
            case Constants.FormFields.LBL_COLLECT_RESPIRATORY_SAMPLE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE);
                break;
            case Constants.FormFields.LBL_SKIP_RESPIRATORY_SAMPLE_COLLECTION:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_TEST_COMPLETE_PAGE);
                break;
            case Constants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE);
                break;
            case Constants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE);
                break;
            case Constants.FormFields.LBL_ADD_XRAY_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_XRAY_PAGE);
                break;
            case Constants.FormFields.LBL_SKIP_XRAY_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_OPT_IN_WBC_PAGE);
                break;
            case Constants.FormFields.LBL_ADD_WBC_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_WBC_PAGE);
                break;
            case Constants.FormFields.LBL_SKIP_WBC_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE);
                break;
            case Constants.FormFields.LBL_SCAN_SAMPLE_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE);
                break;
            case Constants.FormFields.LBL_ENTER_SAMPLE_DETAILS_MANUALLY:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_ENTER_DELIVERY_DETAILS_PAGE);
                break;
        }
        ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(nextStep);
    }
}
