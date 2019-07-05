package io.ona.rdt_app.viewholder;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.smartregister.commonregistry.CommonPersonObjectClient;
import org.smartregister.cursoradapter.RecyclerViewProvider;
import org.smartregister.util.Utils;
import org.smartregister.view.contract.SmartRegisterClient;
import org.smartregister.view.contract.SmartRegisterClients;
import org.smartregister.view.dialog.FilterOption;
import org.smartregister.view.dialog.ServiceModeOption;
import org.smartregister.view.dialog.SortOption;
import org.smartregister.view.viewholder.OnClickFormLauncher;

import java.text.MessageFormat;

import io.ona.rdt_app.R;
import io.ona.rdt_app.util.Constants;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 17/06/2019
 */
public class PatientRegisterViewHolder implements RecyclerViewProvider<PatientRegisterViewHolder.RegisterViewHolder> {

    private final String TAG = PatientRegisterViewHolder.class.getName();

    private Context context;
    private final View.OnClickListener registerActionHandler;
    private final View.OnClickListener paginationClickListener;

    public PatientRegisterViewHolder(Context context, View.OnClickListener registerActionHandler, View.OnClickListener paginationClickListener) {
        this.context = context;
        this.registerActionHandler = registerActionHandler;
        this.paginationClickListener = paginationClickListener;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, RegisterViewHolder viewHolder) {
        CommonPersonObjectClient patient = (CommonPersonObjectClient) client;
        String patientName = Utils.getValue(patient.getColumnmaps(), Constants.DBConstants.NAME, true);
        String patientAge = Utils.getValue(patient.getColumnmaps(), Constants.DBConstants.AGE, true);
        String sex = Utils.getValue(patient.getColumnmaps(), Constants.DBConstants.SEX, true);
        String baseEntityId = patient.getCaseId();
        String nameAndAge = createNameAndAgeLabel(patientName, patientAge);

        viewHolder.patientNameAndAge.setText(nameAndAge);
        viewHolder.patientSex.setText(sex);
        viewHolder.rowItem.setTag(R.id.base_entity_id, baseEntityId);

        attachPatientOnclickListener(viewHolder.rowItem);
    }

    private String createNameAndAgeLabel(String name, String age) {
        return name + ", " + age;
    }

    private void attachPatientOnclickListener(View view) {
        view.setOnClickListener(registerActionHandler);
    }

    @Override
    public void getFooterView(RecyclerView.ViewHolder viewHolder, int currentPageCount, int totalCount, boolean hasNextPage, boolean hasPreviousPage) {
        FooterViewHolder footerViewHolder = (FooterViewHolder) viewHolder;
        footerViewHolder.pageInfoView.setText(
                MessageFormat.format(context.getString(R.string.str_page_info), currentPageCount,
                        totalCount));

        footerViewHolder.nextPageView.setVisibility(hasNextPage ? View.VISIBLE : View.INVISIBLE);
        footerViewHolder.previousPageView.setVisibility(hasPreviousPage ? View.VISIBLE : View.INVISIBLE);

        footerViewHolder.nextPageView.setOnClickListener(paginationClickListener);
        footerViewHolder.previousPageView.setOnClickListener(paginationClickListener);
    }

    @Override
    public SmartRegisterClients updateClients(FilterOption villageFilter, ServiceModeOption serviceModeOption, FilterOption searchFilter, SortOption sortOption) {
        return null;
    }

    @Override
    public void onServiceModeSelected(ServiceModeOption serviceModeOption) {
        // won't implement
    }

    @Override
    public OnClickFormLauncher newFormLauncher(String formName, String entityId, String metaData) {
        return null;
    }

    @Override
    public LayoutInflater inflater() {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RegisterViewHolder createViewHolder(ViewGroup parent) {
        final View view = inflater().inflate(R.layout.register_row_item, parent, false);
        view.findViewById(R.id.btn_record_rdt_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new RDTJsonFormUtils().launchForm((Activity) context, RDT_TEST_FORM, view.getTag(R.id.base_entity_id).toString());
                } catch (JSONException e) {
                    Log.e(TAG, e.getStackTrace().toString());
                }
            }
        });

        return new RegisterViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder createFooterHolder(ViewGroup parent) {
        View view = inflater().inflate(R.layout.smart_register_pagination, parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public boolean isFooterViewHolder(RecyclerView.ViewHolder viewHolder) {
        return FooterViewHolder.class.isInstance(viewHolder);
    }

    public class RegisterViewHolder extends RecyclerView.ViewHolder {
        public View rowItem;
        public TextView patientNameAndAge;
        public TextView patientSex;

        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rowItem = itemView.getRootView();
            this.patientNameAndAge = itemView.findViewById(R.id.tv_patient_name_and_age);
            this.patientSex = itemView.findViewById(R.id.tv_sex);
        }
    }
}
