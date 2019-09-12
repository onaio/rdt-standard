package io.ona.rdt_app.util;

import android.location.Location;
import android.os.Handler;

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

    public JsonFormFragment getFormFragment() {
        return formFragment;
    }

    public void setFormFragment(JsonFormFragment formFragment) {
        this.formFragment = formFragment;
    }

    @Override
    public void onLocationChanged(Location location) {
        super.onLocationChanged(location);
        saveAndDismiss();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getFormFragment().next();
            }
        }, 900);
    }
}
