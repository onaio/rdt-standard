package io.ona.rdt_app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.presenter.RDTJsonFormFragmentPresenter;
import io.ona.rdt_app.interactor.RDTJsonFormInteractor;
import io.ona.rdt_app.util.RDTCaptureJsonFormUtils;

import static io.ona.rdt_app.util.Constants.REQUEST_CODE_GET_JSON;

/**
 * Created by Vincent Karuri on 12/06/2019
 */
public class RDTJsonFormFragment extends JsonFormFragment {

    private final String TAG = RDTJsonFormFragment.class.getName();
    private RDTCaptureJsonFormUtils jsonFormUtils = new RDTCaptureJsonFormUtils();

    public static JsonFormFragment getFormFragment(String stepName) {
        RDTJsonFormFragment jsonFormFragment = new RDTJsonFormFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stepName", stepName);
        jsonFormFragment.setArguments(bundle);
        return jsonFormFragment;
    }

    @Override
    protected void initializeBottomNavigation(JSONObject step, View rootView) {
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
                boolean isSaved = save(false);
                if (isSaved) {
                    try {
                        JSONObject formJsonObject = jsonFormUtils.getFormJsonObject("json.form/rdt-capture-form.json", getContext());
                        jsonFormUtils.startJsonForm(formJsonObject, getActivity(), REQUEST_CODE_GET_JSON);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getStackTrace().toString());
                    }
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
