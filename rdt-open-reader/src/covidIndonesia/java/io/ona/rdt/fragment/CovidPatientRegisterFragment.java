package io.ona.rdt.fragment;

import android.content.Intent;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import org.smartregister.domain.FetchStatus;

import java.lang.ref.WeakReference;

import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.CovidPatientRegisterFragmentPresenter;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.viewholder.CovidPatientRegisterViewHolder;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

import static io.ona.rdt.util.CovidConstants.Form.COVID_PATIENT_REGISTRATION_FORM;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterFragment extends PatientRegisterFragment {

    @Override
    protected PatientRegisterFragmentPresenter createPatientRegisterFragmentPresenter() {
        return new CovidPatientRegisterFragmentPresenter(this);
    }

    @Override
    public void launchPatientProfile(Patient patient) {
        CovidRDTJsonFormUtils.launchPatientProfile(patient, new WeakReference<>(getActivity()));
    }

    @Override
    protected PatientRegisterViewHolder getPatientRegisterViewHolder() {
        return new CovidPatientRegisterViewHolder(getActivity(),
                registerActionHandler, paginationViewHandler, this);
    }

    @Override
    protected String getPatientRegistrationForm() {
        return COVID_PATIENT_REGISTRATION_FORM;
    }

    @Override
    protected String getDefaultSortQuery() {
        return Constants.DBConstants.LAST_INTERACTED_WITH + " DESC";
    }

    @Override
    public void onSyncComplete(FetchStatus fetchStatus) {
        super.onSyncComplete(fetchStatus);
        RDTApplication.getInstance()
                .getContext()
                .allSharedPreferences()
                .savePreference(Constants.Preference.CTS_LATEST_SYNC_TIMESTAMP, String.valueOf(System.currentTimeMillis()));

        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(new Intent(PatientRegisterActivity.ACTION_UPDATE_LATEST_SYNC_DATE));
    }
}
