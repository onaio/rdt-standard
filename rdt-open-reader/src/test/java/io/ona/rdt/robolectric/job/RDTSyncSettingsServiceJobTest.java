package io.ona.rdt.robolectric.job;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.evernote.android.job.Job;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.robolectric.Shadows;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.sync.RDTSettingsSyncIntentService;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public class RDTSyncSettingsServiceJobTest extends RobolectricTest {

    @Test
    public void testOnRunJobShouldStartService() {
        RDTSyncSettingsServiceJob rdtSyncSettingsServiceJob = new RDTSyncSettingsServiceJob();
        ReflectionHelpers.callInstanceMethod(rdtSyncSettingsServiceJob, "onRunJob",
        ReflectionHelpers.ClassParameter.from(Job.Params.class, null));

        Context applicationContext = RDTApplication.getInstance().getApplicationContext();
        Intent expectedIntent = new Intent(applicationContext, RDTSettingsSyncIntentService.class);
        Intent actualIntent = Shadows.shadowOf(new ContextWrapper(applicationContext)).getNextStartedService();
        Assert.assertNotNull(actualIntent);
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
