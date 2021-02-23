package io.ona.rdt.robolectric.sync;

import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.sync.RDTSettingsSyncIntentService;
import io.ona.rdt.util.Constants;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = {BaseJobShadow.class, UtilsShadow.class})
public class RDTSyncIntentServiceTest extends IntentServiceRobolectricTest {

    private RDTSettingsSyncIntentService settingsSyncIntentService;

    @Before
    public void setUp() {
        settingsSyncIntentService = new RDTSettingsSyncIntentService();
    }

    @Test
    public void testOnHandleIntentShouldNotPopulateTreeForNullParentId() {
        ReflectionHelpers.callInstanceMethod(settingsSyncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, null));
        Assert.assertTrue(RDTApplication.getInstance().getContext().allSharedPreferences()
                .getPreference(Constants.Preference.LOCATION_TREE).isEmpty());
    }

    @Test
    public void testOnHandleIntentShouldInitiateCoreSync() {
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        ReflectionHelpers.callInstanceMethod(settingsSyncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, null));
        Assert.assertEquals(RDTSyncServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
    }
}
