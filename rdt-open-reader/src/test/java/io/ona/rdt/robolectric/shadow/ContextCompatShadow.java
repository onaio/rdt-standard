package io.ona.rdt.robolectric.shadow;

import android.content.Context;
import android.content.pm.PackageManager;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Vincent Karuri on 10/08/2020
 */

@Implements(ContextCompat.class)
public class ContextCompatShadow extends Shadow {

    @Implementation
    public static int checkSelfPermission(@NonNull Context context, @NonNull String permission) {
        return PackageManager.PERMISSION_GRANTED;
    }
}
