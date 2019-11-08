package io.ona.rdt.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ScrollView;

import com.rey.material.widget.Button;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.GpsFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.RDTGpsDialog;
import timber.log.Timber;

import static io.ona.rdt.util.Utils.convertDpToPixels;

/**
 * Created by Vincent Karuri on 19/08/2019
 */
public class RDTGpsFactory extends GpsFactory {

    private static final String TAG = RDTGpsFactory.class.getName();

    private WidgetArgs widgetArgs;

    @Override
    public List<View> getViewsFromJson(String stepName, final Context context, JsonFormFragment formFragment, JSONObject jsonObject,
                                       CommonListener listener, boolean popup) throws Exception {

        widgetArgs = new WidgetArgs();
        widgetArgs.withStepName(stepName)
                .withContext(context)
                .withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withListener(listener)
                .withPopup(popup);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        View rootLayout = views.get(0);

        hideTextFields(rootLayout);

        stretchWidgetToFullScreen(formFragment, context);

        new RDTJsonFormFragment().setNextButtonState(rootLayout.findViewById(R.id.record_button), true);

        rootLayout.findViewById(R.id.record_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLocationServiceDisabled(context)) {
                    showLocationServicesDialog(context);
                } else {
                    requestPermissionsForLocation(context);
                }
            }
        });

        return views;
    }

    private void hideTextFields(View rootLayout) {
        rootLayout.findViewById(R.id.altitude).setVisibility(View.GONE);
        rootLayout.findViewById(R.id.accuracy).setVisibility(View.GONE);
    }

    private void stretchWidgetToFullScreen(JsonFormFragment formFragment, Context context) {
        int margin = convertDpToPixels(context, context.getResources().getDimension(com.vijay.jsonwizard.R.dimen.bottom_navigation_margin));
        ScrollView scrollView = ((RDTJsonFormFragment) formFragment).getRootLayout().findViewById(com.vijay.jsonwizard.R.id.scroll_view);
        scrollView.setFillViewport(true);
        scrollView.setPadding(0, 0, 0, margin);
        scrollView.findViewById(com.vijay.jsonwizard.R.id.main_layout).setPadding(margin, 0, margin, 0);
    }

    @Override
    protected void customizeViews(Button recordButton, Context context) {
        gpsDialog = new RDTGpsDialog(gpsDialog);
        gpsDialog.setTitle("Please wait");
        ((RDTGpsDialog) gpsDialog).setFormFragment(widgetArgs.getFormFragment());
    }

    private void showLocationServicesDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setMessage("Location services are disabled. Please go to the phone settings to enable them.")
                .setPositiveButton( "Settings" , new
                        DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface paramDialogInterface, int paramInt) {
                                context.startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        })
                .setNegativeButton( "Cancel" , null )
                .show() ;
    }

    private boolean isLocationServiceDisabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE) ;
        boolean isGpsEnabled = false;
        boolean isNetworkEnabled = false;
        try {
            isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Timber.e(TAG, e);
        }

        return !isGpsEnabled && !isNetworkEnabled;
    }
}
