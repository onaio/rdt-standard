package io.ona.rdt.robolectric.shadow;

import android.content.Intent;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.sync.intent.SyncIntentService;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Implements(SyncIntentService.class)
public class SyncIntentServiceShadow extends Shadow {

    private static MockCounter mockCounter = new MockCounter();

    @Implementation
    protected void onHandleIntent(Intent intent) {
        mockCounter.setCount(1);
    }

    public static MockCounter getMockCounter() {
        return mockCounter;
    }
}
