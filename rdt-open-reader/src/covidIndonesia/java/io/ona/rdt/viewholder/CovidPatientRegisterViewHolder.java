package io.ona.rdt.viewholder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import io.ona.rdt.R;

/**
 * Created by Vincent Karuri on 14/07/2020
 */
public class CovidPatientRegisterViewHolder extends PatientRegisterViewHolder {

    public CovidPatientRegisterViewHolder(Context context, View.OnClickListener registerActionHandler, View.OnClickListener paginationClickListener) {
        super(context, registerActionHandler, paginationClickListener);
    }

    public CovidPatientRegisterViewHolder(Context context, View.OnClickListener registerActionHandler, View.OnClickListener paginationClickListener, View.OnClickListener launchRDTTestListener) {
        super(context, registerActionHandler, paginationClickListener, launchRDTTestListener);
    }

    @Override
    public RegisterViewHolder createViewHolder(ViewGroup parent) {
        final View view = inflater().inflate(R.layout.register_row_item, parent, false);
        view.findViewById(R.id.btn_record_rdt_test).setVisibility(View.GONE);
        return new RegisterViewHolder(view);
    }
}
