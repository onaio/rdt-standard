package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.job.BaseJob;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Implements(BaseJob.class)
public class BaseJobShadow extends Shadow {

    private static final List<String> jobTags = new ArrayList<>();

    @Implementation
    public static void scheduleJobImmediately(String jobTag) {
        jobTags.add(jobTag);
    }

    @Implementation
    public static void scheduleJob(String jobTag, Long start, Long flex) {
        jobTags.add(jobTag);
    }

    public static List<String> getJobTags() {
        return jobTags;
    }
}
