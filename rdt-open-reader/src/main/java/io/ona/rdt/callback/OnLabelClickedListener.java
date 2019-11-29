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
            String next = stepStateConfig.getStepStateObj().getString(SCAN_CARESTART_PAGE);
            if (Constants.LBL_CARE_START.equals(key)) {
                formFragment.getRdtActivity().setRdtType(Constants.CARESTART_RDT);
            } else {
                formFragment.getRdtActivity().setRdtType(Constants.ONA_RDT);
                next = stepStateConfig.getStepStateObj().getString(SCAN_QR_PAGE);
            }
            ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(next);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }
}
