package io.ona.rdt_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

import io.ona.rdt_app.R;

public class PatientRegisterFragment extends BaseRegisterFragment {

    public PatientRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_patient_register, container, false);
    }

    @Override
    protected void initializePresenter() {
        // TODO: implement this
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
}
