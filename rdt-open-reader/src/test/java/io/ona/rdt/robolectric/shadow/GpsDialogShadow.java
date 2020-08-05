package io.ona.rdt.robolectric.shadow;

import android.content.Context;
import android.location.Location;
import android.view.View;

import com.rey.material.widget.TextView;
import com.vijay.jsonwizard.customviews.GpsDialog;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadows.ShadowDialog;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Vincent Karuri on 04/08/2020
 */

@Implements(GpsDialog.class)
public class GpsDialogShadow extends ShadowDialog {

    private static Set<Object> args = new HashSet<>();

    @Implementation
    public void __constructor__(Context context, View dataView, TextView latitudeTV,
                                TextView longitudeTV, TextView altitudeTV, TextView accuracyTV) {
    }

    @Implementation
    public static void onLocationChanged(Location location) {
        args.add(location);
    }

    @Implementation
    public static void saveAndDismiss() {
        args.add("saveAndDismiss");
    }

    public static Set<Object> getArgs() {
        return args;
    }
}
