package io.ona.rdt.robolectric.shadow;

import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;

import io.ona.rdt.util.Utils;


/**
 * Created by Vincent Karuri on 20/07/2020
 */

@Implements(Utils.class)
public class UtilsShadow extends Shadow {

    private static MockCounter mockCounter;

    @Implementation
    public static void scheduleJobsImmediately() {
        getMockCounter().setCount(getMockCounter().getCount() + 1);
    }

    public static synchronized void setMockCounter(MockCounter mockCounter) {
        UtilsShadow.mockCounter = mockCounter;
    }

    public static synchronized MockCounter getMockCounter() {
        return mockCounter;
    }
}
