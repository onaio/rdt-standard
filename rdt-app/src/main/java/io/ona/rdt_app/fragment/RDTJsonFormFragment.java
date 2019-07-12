package io.ona.rdt_app.fragment;

import android.os.Bundle;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONObject;

import io.ona.rdt_app.R;
import io.ona.rdt_app.activity.RDTJsonFormActivity;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt_app.util.Constants;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment {

    private static int currentStep;

    public static JsonFormFragment getFormFragment(String stepName) {
        String stepNum = stepName.substring(stepName.length() - 1, stepName.length());
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
        rootView.findViewById(com.vijay.jsonwizard.R.id.previous_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(false);
            }
        });
        rootView.findViewById(com.vijay.jsonwizard.R.id.next_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object isSubmit = v.getTag(R.id.submit);
                String currStep = "step" + currentStep;
                if ("step8".equals(currStep)) {
                    String rdtType = ((RDTJsonFormActivity) getActivity()).getRdtType();
                    if (Constants.CARESTART_RDT.equals(rdtType)) {
                        JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment("step12");
                        transactThis(nextFragment);
                    }
                } else if (isSubmit != null && Boolean.valueOf(isSubmit.toString())) {
                    save(false);
                } else {
                    next();
                }
            }
        });
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
}
