package io.ona.rdt.viewholder;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.commons.lang3.StringUtils;
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
import timber.log.Timber;

import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.Constants;

/**
 * Created by Vincent Karuri on 17/06/2019
 */
public class PatientRegisterViewHolder implements RecyclerViewProvider<PatientRegisterViewHolder.RegisterViewHolder> {

    private Context context;
    private final View.OnClickListener registerActionHandler;
    private final View.OnClickListener paginationClickListener;
    private View.OnClickListener launchRDTTestListener;

    public PatientRegisterViewHolder(Context context, View.OnClickListener registerActionHandler, View.OnClickListener paginationClickListener) {
        this.context = context;
        this.registerActionHandler = registerActionHandler;
        this.paginationClickListener = paginationClickListener;
    }

    public PatientRegisterViewHolder(Context context, View.OnClickListener registerActionHandler, View.OnClickListener paginationClickListener, View.OnClickListener launchRDTTestListener) {
        this(context, registerActionHandler, paginationClickListener);
        this.launchRDTTestListener = launchRDTTestListener;
    }

    @Override
    public void getView(Cursor cursor, SmartRegisterClient client, RegisterViewHolder viewHolder) {
        CommonPersonObjectClient commonPersonObjectClient = (CommonPersonObjectClient) client;
        String patientName = Utils.getValue(commonPersonObjectClient.getColumnmaps(), Constants.DBConstants.NAME, true);
        String patientAge = Utils.getValue(commonPersonObjectClient.getColumnmaps(), Constants.DBConstants.AGE, true);
        String sex = Utils.getValue(commonPersonObjectClient.getColumnmaps(), Constants.DBConstants.SEX, true);
        String baseEntityId = commonPersonObjectClient.getCaseId();
        String patientId = Utils.getValue(commonPersonObjectClient.getColumnmaps(), Constants.DBConstants.PATIENT_ID, true);
        String nameAndAge = createNameAndAgeLabel(patientName, patientId, patientAge);

        if(patientName.contains("coba")){
            String aaa = "";
        }
        String address = Utils.getValue(commonPersonObjectClient.getColumnmaps(), Constants.DBConstants.RESIDENTIAL_ADDRESS, true);
        String nameAndGender = createNameAndGender(patientId, patientName, sex);



        final Patient patient = new Patient(patientName, sex, baseEntityId, patientId, address);
        viewHolder.tvPatientNameAndAge.setText(nameAndGender);
//        viewHolder.tvPatientSex.setText(sex);
        viewHolder.tvPatientId.setText(patientId);
        viewHolder.tvAddress.setText(address);
        viewHolder.rowItem.setTag(R.id.patient_tag, patient);
        viewHolder.btnRecordRDTTest.setTag(R.id.patient_tag, patient);
        viewHolder.btnRecordRDTTest.setOnClickListener(launchRDTTestListener);

        attachPatientOnclickListener(viewHolder.rowItem);
    }

    private String createNameAndAgeLabel(String patientName, String patientId, String age) {
        long formattedAge = StringUtils.isBlank(age) ? 10 : Math.round(Double.valueOf(age));
        String patientIdentifier =  StringUtils.isBlank(patientName) ? patientId : patientName;
        return patientIdentifier + ", " + formattedAge;
    }

    private String createNameAndGender(String patientId, String patientName, String sex){
        String patientIdentifier =  StringUtils.isBlank(patientName) ? patientId : patientName;
        return patientIdentifier + " (" + sex + ")";

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
        public TextView tvPatientNameAndAge;
        //        public TextView tvPatientSex;
        public TextView tvAddress;
        public TextView tvPatientId;
        public TextView btnRecordRDTTest;

        public RegisterViewHolder(@NonNull View itemView) {
            super(itemView);
            this.rowItem = itemView.getRootView();
            this.tvPatientNameAndAge = itemView.findViewById(R.id.tv_patient_name_and_age);
//            this.tvPatientSex = itemView.findViewById(R.id.tv_sex);
            this.tvAddress = itemView.findViewById(R.id.tv_address);
            this.tvPatientId = itemView.findViewById(R.id.tv_patient_id);
            this.btnRecordRDTTest = itemView.findViewById(R.id.btn_record_rdt_test);
        }
    }
}
