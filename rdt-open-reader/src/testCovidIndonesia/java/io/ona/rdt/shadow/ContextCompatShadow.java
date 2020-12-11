package io.ona.rdt.shadow;

import android.content.Context;
import android.content.pm.PackageManager;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Vincent Karuri on 10/12/2020
 */

@Implements(ContextCompat.class)
public class ContextCompatShadow extends Shadow {

    private static boolean isLocationPermissionGranted;

    @Implementation
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return isLocationPermissionGranted() ? PackageManager.PERMISSION_GRANTED : PackageManager.PERMISSION_DENIED;
    }

    public static boolean isLocationPermissionGranted() {
        return isLocationPermissionGranted;
    }

    public static void setIsLocationPermissionGranted(boolean isLocationPermissionGranted) {
        ContextCompatShadow.isLocationPermissionGranted = isLocationPermissionGranted;
    }
}
