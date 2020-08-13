package io.ona.rdt.robolectric.job;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.evernote.android.job.Job;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Shadows;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.service.ImageUploadSyncService;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public class ImageUploadSyncServiceJobTest extends RobolectricTest {

    @Test
    public void testOnRunJobShouldStartService() {
        ImageUploadSyncServiceJob imageUploadSyncServiceJob = new ImageUploadSyncServiceJob();
        ReflectionHelpers.callInstanceMethod(imageUploadSyncServiceJob, "onRunJob",
                ReflectionHelpers.ClassParameter.from(Job.Params.class, null));

        Context applicationContext = RDTApplication.getInstance().getApplicationContext();
        Intent expectedIntent = new Intent(applicationContext, ImageUploadSyncService.class);
        Intent actualIntent = Shadows.shadowOf(new ContextWrapper(applicationContext)).getNextStartedService();
        Assert.assertNotNull(actualIntent);
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }
}
