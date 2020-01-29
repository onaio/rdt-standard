package io.ona.rdt.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.ona.rdt.R;

/**
 * Created by Vincent Karuri on 29/01/2020
 */
public class TestsProfileFragment extends Fragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_tests_profile, container, false);
        rootLayout.findViewById(R.id.btn_back_to_patient_profile).setOnClickListener(this);
        rootLayout.findViewById(R.id.tv_back_to_profile_text).setOnClickListener(this);
        return rootLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back_to_profile_text:
            case R.id.btn_back_to_patient_profile:
                getActivity().getSupportFragmentManager().popBackStack();
                break;
            default:
                // do nothing
        }
    }
}
