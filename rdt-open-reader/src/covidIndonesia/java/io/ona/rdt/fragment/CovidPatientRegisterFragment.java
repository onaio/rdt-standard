package io.ona.rdt.fragment;

import android.app.Activity;
import android.content.Intent;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;

import java.lang.ref.WeakReference;

import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.viewholder.CovidPatientRegisterViewHolder;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterFragment extends PatientRegisterFragment {

    @Override
    public void launchPatientProfile(Patient patient) {
        RDTJsonFormUtils.launchPatientProfile(patient, new WeakReference<>(getActivity()));
    }

    @Override
    public void initializeAdapter() {
        CovidPatientRegisterViewHolder viewHolder = new CovidPatientRegisterViewHolder(getActivity(),
                registerActionHandler, paginationViewHandler, this);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, viewHolder, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }
}
