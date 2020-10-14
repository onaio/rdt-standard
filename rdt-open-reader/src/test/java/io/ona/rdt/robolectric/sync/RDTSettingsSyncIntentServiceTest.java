package io.ona.rdt.robolectric.sync;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.job.LocationStructureServiceJob;

import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.RDTRepositoryShadow;
import io.ona.rdt.robolectric.shadow.SQLiteDatabaseShadow;
import io.ona.rdt.robolectric.shadow.SyncIntentServiceShadow;
import io.ona.rdt.sync.RDTSyncIntentService;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = { BaseJobShadow.class, SyncIntentServiceShadow.class, RDTRepositoryShadow.class, SQLiteDatabaseShadow.class})
public class RDTSettingsSyncIntentServiceTest extends IntentServiceRobolectricTest {

    private RDTSyncIntentService syncIntentService;

    @Before
    public void setUp() {
        syncIntentService = new RDTSyncIntentService();
    }

    @Test
    public void testOnHandleIntentShouldCallSuperAndInitiateImageUpload() {
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        ReflectionHelpers.callInstanceMethod(syncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, new Intent()));
        Assert.assertEquals(1, SyncIntentServiceShadow.getMockCounter().getCount());
        Assert.assertEquals(LocationStructureServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
        Assert.assertEquals(ImageUploadSyncServiceJob.TAG, BaseJobShadow.getJobTags().get(1));
    }
}
