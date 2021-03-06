package io.ona.rdt.robolectric.shadow;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import androidx.annotation.NonNull;

import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 20/07/2020
 */

@Implements(FirebaseCrashlytics.class)
public class FirebaseCrashlyticsShadow {

    private static FirebaseCrashlytics firebaseCrashlytics;

    @NonNull
    @Implementation
    public static FirebaseCrashlytics getInstance() {
        if (firebaseCrashlytics == null) {
            firebaseCrashlytics = mock(FirebaseCrashlytics.class);
        }
       return firebaseCrashlytics;
    }
}
