package io.ona.rdt.shadow;

import android.app.Activity;
import android.location.Location;
import android.os.Looper;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;

import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.ona.rdt.robolectric.shadow.MockCounter;

/**
 * Created by Vincent Karuri on 10/12/2020
 */

@Implements(LocationServices.class)
public class LocationServicesShadow extends Shadow {

    private static FusedLocationProviderClient fusedLocationProviderClient;

    @Implementation
    public static FusedLocationProviderClient getFusedLocationProviderClient(@NonNull Activity var0) {
        return fusedLocationProviderClient;
    }

    public static void setFusedLocationProviderClient(FusedLocationProviderClient fusedLocationProviderClient) {
        LocationServicesShadow.fusedLocationProviderClient = fusedLocationProviderClient;
    }
}
