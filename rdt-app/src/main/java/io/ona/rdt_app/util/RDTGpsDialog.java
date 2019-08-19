package io.ona.rdt_app.util;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.rey.material.widget.TextView;
import com.vijay.jsonwizard.customviews.GpsDialog;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

/**
 * Created by Vincent Karuri on 19/08/2019
 */
public class RDTGpsDialog extends GpsDialog {

    private JsonFormFragment formFragment;

    public RDTGpsDialog(Context context, View dataView, TextView latitudeTV, TextView longitudeTV, TextView altitudeTV, TextView accuracyTV) {
       super(context, dataView, latitudeTV, longitudeTV, altitudeTV, accuracyTV);
    }

    public RDTGpsDialog(GpsDialog gpsDialog) {
        RDTGpsDialog(gpsDialog.getContext(), gpsDialog.getDataView(), gpsDialog.getLatitudeTV(), gpsDialog.getLongitudeTV(), gpsDialog.getAltitudeTV(), gpsDialog.getAccuracyTV());
    }

    @Override
    protected void init() {
        super.init();
        Button okButton = this.findViewById(com.vijay.jsonwizard.R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndDismiss();
                getFormFragment().next();
            }
        });
    }

    public JsonFormFragment getFormFragment() {
        return formFragment;
    }

    public void setFormFragment(JsonFormFragment formFragment) {
        this.formFragment = formFragment;
    }
}
