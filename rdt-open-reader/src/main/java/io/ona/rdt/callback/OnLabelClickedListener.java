package io.ona.rdt.callback;

import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONObject;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt.util.Constants;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NEXT;

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
        if (Constants.LBL_CARE_START.equals(key)) {
            formFragment.getRdtActivity().setRdtType(Constants.CARESTART_RDT);
        } else {
            formFragment.getRdtActivity().setRdtType(Constants.ONA_RDT);
        }
        ((RDTJsonFormFragmentPresenter) formFragment.getPresenter()).moveToNextStep(jsonObject.optString(NEXT));
    }
}
