package io.ona.rdt.robolectric.sync;

import android.content.Intent;

import com.google.gson.Gson;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.job.LocationStructureServiceJob;
import org.smartregister.repository.AllSharedPreferences;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.LocationServiceHelperShadow;
import io.ona.rdt.robolectric.shadow.SyncIntentServiceShadow;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.sync.RDTSyncIntentService;
import io.ona.rdt.util.Constants;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = { BaseJobShadow.class, SyncIntentServiceShadow.class, LocationServiceHelperShadow.class, UtilsShadow.class})
public class RDTSyncIntentServiceTest extends IntentServiceRobolectricTest {

    private RDTSyncIntentService syncIntentService;

    @Before
    public void setUp() {
        syncIntentService = new RDTSyncIntentService();
    }

    @Test
    public void testOnHandleIntentShouldNotPopulateTreeForNullParentId() {
        RDTApplication.getInstance().getContext().allSharedPreferences()
                .savePreference(Constants.Preference.LOCATION_TREE, "");
        String initialLocationParentId = UtilsShadow.getParentLocationId();
        UtilsShadow.setParentLocationId(null);
        ReflectionHelpers.callInstanceMethod(syncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, null));
        Assert.assertTrue(RDTApplication.getInstance().getContext().allSharedPreferences()
                .getPreference(Constants.Preference.LOCATION_TREE).isEmpty());
        UtilsShadow.setParentLocationId(initialLocationParentId);
    }

    @Test
    public void testOnHandleIntentShouldCallSuperAndInitiateImageUpload() {
        RDTApplication.getInstance().getContext().allSharedPreferences().saveDefaultLocalityId("", "");
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        ReflectionHelpers.callInstanceMethod(syncIntentService, "onHandleIntent",
                ReflectionHelpers.ClassParameter.from(Intent.class, new Intent()));
        Assert.assertEquals(1, SyncIntentServiceShadow.getMockCounter().getCount());
        Assert.assertEquals(LocationStructureServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
        Assert.assertEquals(ImageUploadSyncServiceJob.TAG, BaseJobShadow.getJobTags().get(1));
        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        Gson gson = new Gson();
        LocationTree locationTree = gson.fromJson(LocationServiceHelperShadow.MOCK_LOCATION_TREE_JSON, LocationTree.class);
        Assert.assertEquals(gson.toJson(locationTree), allSharedPreferences.getPreference(Constants.Preference.LOCATION_TREE));
    }
}
