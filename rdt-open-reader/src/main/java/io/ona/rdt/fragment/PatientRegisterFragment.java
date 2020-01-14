package io.ona.rdt.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.EditText;

import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

import static io.ona.rdt.util.Constants.Form.PATIENT_REGISTRATION_FORM;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.Constants.FormFields.PATIENT;

public class PatientRegisterFragment extends BaseRegisterFragment implements PatientRegisterFragmentContract.View, View.OnClickListener {

    public PatientRegisterFragment() {
        initializePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.tablename = "rdt_patients";
    }

    @Override
    public void onResume() {
        super.onResume();
        setRefreshList(true);
        onResumption();
    }

    @Override
    protected void initializePresenter() {
        presenter = new PatientRegisterFragmentPresenter(this);
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
        return getPresenter().getMainCondition();
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
        launchPatientProfile((Patient) view.getTag(R.id.patient_tag));
    }

    private void launchPatientProfile(Patient patient) {
        Intent intent = new Intent(getActivity(), PatientProfileActivity.class);
        intent.putExtra(PATIENT, patient);
        startActivity(intent, null);
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
        clientsProgressView = view.findViewById(R.id.client_list_progress);
        clientsView = view.findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        clientsView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        clientsView.setLayoutManager(layoutManager);

        DividerItemDecoration itemDecor = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        clientsView.addItemDecoration(itemDecor);

        rootView.findViewById(R.id.btn_register_patient).setOnClickListener(this);
        rootView.findViewById(R.id.drawerMenu).setOnClickListener(this);

        initializeAdapter();
    }

    @Override
    public void initializeAdapter() {
        PatientRegisterViewHolder viewHolder = new PatientRegisterViewHolder(getActivity(),
                registerActionHandler, paginationViewHandler, this);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, viewHolder, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_patient_register;
    }

    public PatientRegisterFragmentPresenter getPresenter() {
        return (PatientRegisterFragmentPresenter) presenter;
    }

    @Override
    public EditText getSearchView() {
        return rootView.findViewById(R.id.edit_text_search);
    }

    @Override
    public View getSearchCancelView() {
        return rootView.findViewById(org.smartregister.R.id.btn_search_cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_patient:
                getPresenter().launchForm(getActivity(), PATIENT_REGISTRATION_FORM, null);
                break;
            case R.id.drawerMenu:
                getParentView().openDrawerLayout();
                break;
            case R.id.btn_record_rdt_test:
                launchRDTTestForm(v);
                break;
            default:
                // do nothing
        }
    }

    private void launchRDTTestForm(View view) {
        final Patient patient = (Patient) view.getTag(R.id.patient_tag);
        getPresenter().launchForm(getActivity(), RDT_TEST_FORM, patient);
    }

    private PatientRegisterActivityContract.View getParentView() {
        return ((PatientRegisterActivityContract.View) getActivity());
    }
}
