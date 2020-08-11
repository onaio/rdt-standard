package io.ona.rdt.robolectric.job;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.evernote.android.job.Job;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Shadows;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.sync.RDTSyncIntentService;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public class RDTSyncServiceJobTest extends RobolectricTest {

    @Test
    public void testOnRunJobShouldStartService() {
        RDTSyncServiceJob rdtSyncServiceJob = new RDTSyncServiceJob(RDTSyncIntentService.class);
        ReflectionHelpers.callInstanceMethod(rdtSyncServiceJob, "onRunJob",
                ReflectionHelpers.ClassParameter.from(Job.Params.class, null));

        Context applicationContext = RDTApplication.getInstance().getApplicationContext();
        Intent expectedIntent = new Intent(applicationContext, RDTSyncIntentService.class);
        Intent actualIntent = Shadows.shadowOf(new ContextWrapper(applicationContext)).getNextStartedService();
        Assert.assertNotNull(actualIntent);
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
