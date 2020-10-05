package io.ona.rdt.robolectric.sync;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.job.LocationStructureServiceJob;

import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.sync.RDTSettingsSyncIntentService;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = { BaseJobShadow.class })
public class RDTSyncIntentServiceTest extends IntentServiceRobolectricTest {

    private RDTSettingsSyncIntentService settingsSyncIntentServicee;

    @Before
    public void setUp() {
        settingsSyncIntentServicee = new RDTSettingsSyncIntentService();
    }

    @Test
    public void testOnHandleIntentShouldInitiateCoreSync() {
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        ReflectionHelpers.callInstanceMethod(settingsSyncIntentServicee, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, (Intent) null));
        Assert.assertEquals(RDTSyncServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
    }
}
