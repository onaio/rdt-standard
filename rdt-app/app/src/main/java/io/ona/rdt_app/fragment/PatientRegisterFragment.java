package io.ona.rdt_app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.cursoradapter.RecyclerViewPaginatedAdapter;
import org.smartregister.view.fragment.BaseRegisterFragment;

import java.util.HashMap;

import io.ona.rdt_app.R;
import io.ona.rdt_app.contract.PatientRegisterFragmentContract;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt_app.viewholder.PatientRegisterViewHolder;
import util.RDTCaptureJsonFormUtils;

public class PatientRegisterFragment extends BaseRegisterFragment implements PatientRegisterFragmentContract.View {

    private final String TAG = PatientRegisterFragment.class.getName();

    private RDTCaptureJsonFormUtils formUtils;

    public PatientRegisterFragment() {
        initializePresenter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        formUtils = new RDTCaptureJsonFormUtils();
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

        initializeAdapter();
    }

    @Override
    public void initializeAdapter() {
        PatientRegisterViewHolder viewHolder = new PatientRegisterViewHolder(getActivity(), registerActionHandler, paginationViewHandler);
        clientAdapter = new RecyclerViewPaginatedAdapter(null, viewHolder, context().commonrepository(this.tablename));
        clientAdapter.setCurrentlimit(20);
        clientsView.setAdapter(clientAdapter);
    }

    protected int getLayout() {
        return R.layout.fragment_patient_register;
    }
    
    public PatientRegisterFragmentPresenter getPresenter() {
        return (PatientRegisterFragmentPresenter) presenter;
    }
}
