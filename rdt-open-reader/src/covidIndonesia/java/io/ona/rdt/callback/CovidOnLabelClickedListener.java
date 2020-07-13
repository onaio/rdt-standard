package io.ona.rdt.callback;

import com.vijay.jsonwizard.domain.WidgetArgs;

import io.ona.rdt.util.CovidConstants;

import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_ENTER_DELIVERY_DETAILS_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_OPT_IN_WBC_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SCAN_BARCODE_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_WBC_PAGE;
import static io.ona.rdt.util.CovidConstants.Step.COVID_XRAY_PAGE;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidOnLabelClickedListener extends OnLabelClickedListener {

    public CovidOnLabelClickedListener(WidgetArgs widgetArgs) {
        super(widgetArgs);
    }

    @Override
    protected String getNextStep() {
        String nextStep = "";
        switch (key) {
            case CovidConstants.FormFields.LBL_SCAN_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SCAN_BARCODE_PAGE);
                break;
            case CovidConstants.FormFields.LBL_ENTER_RDT_MANUALLY:
                nextStep = stepStateConfig.getStepStateObj().optString(MANUAL_EXPIRATION_DATE_ENTRY_PAGE);
                break;
            case CovidConstants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE);
                break;
            case CovidConstants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE);
                break;
            case CovidConstants.FormFields.LBL_ADD_XRAY_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_XRAY_PAGE);
                break;
            case CovidConstants.FormFields.LBL_SKIP_XRAY_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_OPT_IN_WBC_PAGE);
                break;
            case CovidConstants.FormFields.LBL_ADD_WBC_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_WBC_PAGE);
                break;
            case CovidConstants.FormFields.LBL_SKIP_WBC_RESULTS:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE);
                break;
            case CovidConstants.FormFields.LBL_SCAN_SAMPLE_BARCODE:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE);
                break;
            case CovidConstants.FormFields.LBL_ENTER_SAMPLE_DETAILS_MANUALLY:
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_ENTER_DELIVERY_DETAILS_PAGE);
                break;
        }
        return nextStep;
    }
}
