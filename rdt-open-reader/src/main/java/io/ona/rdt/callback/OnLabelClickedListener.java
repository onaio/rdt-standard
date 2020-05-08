package io.ona.rdt.callback;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;
import timber.log.Timber;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static io.ona.rdt.util.Constants.Step.COLLECT_RESPIRATORY_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_STORE_RESPIRATORY_SAMPLE_PAGE;
import static io.ona.rdt.util.Constants.Step.COVID_TEST_COMPLETE_PAGE;
import static io.ona.rdt.util.Constants.Step.RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE;
import static io.ona.rdt.util.Constants.Step.CONDUCT_COVID_RDT_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_COVID_BARCODE_PAGE;
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
        try {
            JSONObject jsonObject = widgetArgs.getJsonObject();
            RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
            final String key = jsonObject.optString(KEY, "");
            StepStateConfig stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();

            String nextStep = "";
            if (Constants.FormFields.LBL_CARE_START.equals(key)) {
                formFragment.getRdtActivity().setRdtType(Constants.RDTType.CARESTART_RDT);
                nextStep = stepStateConfig.getStepStateObj().optString(SCAN_CARESTART_PAGE);
            } else if (Constants.FormFields.LBL_SCAN_QR_CODE.equals(key)) {
                formFragment.getRdtActivity().setRdtType(Constants.RDTType.ONA_RDT);
                nextStep = stepStateConfig.getStepStateObj().optString(SCAN_QR_PAGE);
            } else if (Constants.FormFields.LBL_SCAN_BARCODE.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(SCAN_COVID_BARCODE_PAGE);
            } else if (Constants.FormFields.LBL_ENTER_RDT_MANUALLY.equals(key)) {
                // todo: add next step
            } else if (Constants.FormFields.LBL_CONDUCT_RDT.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(CONDUCT_COVID_RDT_PAGE);
            } else if (Constants.FormFields.LBL_SKIP_RDT_TEST.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE);
            } else if (Constants.FormFields.LBL_COLLECT_RESPIRATORY_SAMPLE.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(COLLECT_RESPIRATORY_SPECIMEN_PAGE);
            } else if (Constants.FormFields.LBL_SKIP_RESPIRATORY_SAMPLE_COLLECTION.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_TEST_COMPLETE_PAGE);
            } else if (Constants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE);
            } else if (Constants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL.equals(key)) {
                nextStep = stepStateConfig.getStepStateObj().optString(COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE);
            }
            ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(nextStep);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
