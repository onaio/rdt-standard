package io.ona.rdt.sync;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.SyncIntentServiceShadow;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = { BaseJobShadow.class, SyncIntentServiceShadow.class })
public class RDTSyncIntentServiceTest extends RobolectricTest {

    private RDTSyncIntentService syncIntentService;

    @Before
    public void setUp() {
        syncIntentService = new RDTSyncIntentService();
    }

    @Test
    public void testOnHandleIntentShouldCallSuperAndInitiateImageUpload() {
        Assert.assertEquals(0, BaseJobShadow.getMockCounter().getCount());
        ReflectionHelpers.callInstanceMethod(syncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, new Intent()));
        Assert.assertEquals(1, BaseJobShadow.getMockCounter().getCount());
        Assert.assertEquals(1, SyncIntentServiceShadow.getMockCounter().getCount());
    }
}
