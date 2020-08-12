package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.job.BaseJob;

import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.sync.RDTSyncIntentService;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Implements(BaseJob.class)
public class BaseJobShadow extends Shadow {

    private static MockCounter mockCounter = new MockCounter();

    @Implementation
    public static void scheduleJobImmediately(String jobTag) {
        mockCounter.setCount(1);
    }

    public static MockCounter getMockCounter() {
        return mockCounter;
    }
}
