package io.ona.rdt.widget;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.BarcodeFactory;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public abstract class RDTBarcodeFactory extends BarcodeFactory implements OnActivityResultListener {

    protected WidgetArgs widgetArgs;
    protected JSONObject stepStateConfig;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context,
                                       JsonFormFragment formFragment, final JSONObject jsonObject,
                                       CommonListener listener, boolean popup) {

        widgetArgs = new WidgetArgs();
        widgetArgs.withContext(context)
                .withJsonObject(jsonObject)
                .withFormFragment(formFragment)
                .withStepName(stepName);

        stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration().getStepStateObj();

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        RelativeLayout rootLayout = views == null ? null : (RelativeLayout) views.get(0);

        clickThenHideScanButton(rootLayout);

        ((JsonApi) context).getFormDataViews().clear(); // we do not need the edit text and it causes weird validation issues

        return views;
    }


    private void clickThenHideScanButton(RelativeLayout rootLayout) {
        if (rootLayout != null) {
            Button scanButton = rootLayout.findViewById(com.vijay.jsonwizard.R.id.scan_button);
            scanButton.setVisibility(View.GONE);
            scanButton.performClick();
        }
    }

    @Override
    protected void addOnBarCodeResultListeners(final Context context, final MaterialEditText editText) {
        editText.setVisibility(View.GONE);
        if (context instanceof JsonApi) {
            ((JsonApi) context).addOnActivityResultListener(BARCODE_REQUEST_CODE, this);
        }
    }

    protected void moveToNextStep() {
        if (!widgetArgs.getFormFragment().next()) {
            widgetArgs.getFormFragment().save(true);
        }
    }

    private boolean isRDTExpired(Date date) {
        return date != null && new Date().after(date);
    }

    protected void moveToNextStep(Date expDate) {
        if (!isRDTExpired(expDate)) {
            moveToNextStep();
        } else {
            navigateToUnusableProductPage();
        }
    }

    protected void navigateToUnusableProductPage() {
        JsonFormFragment formFragment = widgetArgs.getFormFragment();
        String expiredPageAddr = stepStateConfig.optString(PRODUCT_EXPIRED_PAGE, "step1");
        JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(expiredPageAddr);
        formFragment.transactThis(nextFragment);
    }
}
