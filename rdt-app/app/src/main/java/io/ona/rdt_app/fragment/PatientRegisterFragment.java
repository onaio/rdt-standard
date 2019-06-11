package io.ona.rdt_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

import io.ona.rdt_app.R;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;
import util.RDTCaptureJsonFormUtils;

public class PatientRegisterFragment extends BaseRegisterFragment {

    private final String TAG = PatientRegisterFragment.class.getName();

    private RDTCaptureJsonFormUtils formUtils;

    public PatientRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formUtils = new RDTCaptureJsonFormUtils();
    }

    @Override
    protected void initializePresenter() {
        // TODO: implement this
        if (getActivity() == null) {
            return;
        }
        presenter = new PatientRegisterFragmentPresenter();
    }

    @Override
    public void setUniqueID(String s) {
        // TODO: implement this
    }

    @Override
    public void setAdvancedSearchFormData(HashMap<String, String> hashMap) {
        // TODO: implement this
    }

    @Override
    protected String getMainCondition() {
        return null;
    }

    @Override
    protected String getDefaultSortQuery() {
        return null;
    }

    @Override
    protected void startRegistration() {
        // TODO: implement this
    }

    @Override
    protected void onViewClicked(View view) {
        // TODO: implement this
    }

    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showNotFoundPopup(String s) {
        // TODO: implement this
    }

    @Override
    public void setupViews(View view) {
        rootView.findViewById(R.id.btn_register_patient).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               try {
                   JSONObject formJsonObject = formUtils.getFormJsonObject("json.form/patient-registration-form.json", getContext());
                   formUtils.startJsonForm(formJsonObject, getActivity(), 1);
               } catch (JSONException e) {
                   Log.e(TAG, e.getStackTrace().toString());
               }
           }
        });
    }

    protected int getLayout() {
        return R.layout.fragment_patient_register;
    }
}
