package io.ona.rdt_app.util;

import android.content.Context;
import android.os.Handler;
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

    public RDTGpsDialog(GpsDialog gpsDialog) {
        super(gpsDialog.getContext(), gpsDialog.getDataView(), gpsDialog.getLatitudeTV(), gpsDialog.getLongitudeTV(), gpsDialog.getAltitudeTV(), gpsDialog.getAccuracyTV());
    }

    @Override
    protected void init() {
        super.init();
        Button okButton = this.findViewById(com.vijay.jsonwizard.R.id.ok_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAndDismiss();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFormFragment().next();
                    }
                }, 760);
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
