package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.job.BaseJob;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Implements(BaseJob.class)
public class BaseJobShadow extends Shadow {

    private static String jobTag;

    @Implementation
    public static void scheduleJobImmediately(String jobTag) {
        BaseJobShadow.jobTag = jobTag;
    }

    public static String getJobTag() {
        return jobTag;
    }

    public static void setJobTag(String jobTag) {
        BaseJobShadow.jobTag = jobTag;
    }
}
