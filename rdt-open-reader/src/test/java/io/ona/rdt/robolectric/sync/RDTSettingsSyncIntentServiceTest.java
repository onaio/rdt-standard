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
import io.ona.rdt.sync.RDTSyncIntentService;
import io.ona.rdt.util.Constants;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Config(shadows = { BaseJobShadow.class, SyncIntentServiceShadow.class, LocationServiceHelperShadow.class})
public class RDTSettingsSyncIntentServiceTest extends IntentServiceRobolectricTest {

    private RDTSyncIntentService syncIntentService;
    private static final String MOCK_LOCATION_TREE_JSON = "{\"locationsHierarchy\":{\"map\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"children\":{\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 1\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 2\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 2\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 3\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 3\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"}},\"id\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Division 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Division 1\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"}}},\"parentChildren\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":[\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\"]}}}";

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
        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        Gson gson = new Gson();
        LocationTree locationTree = gson.fromJson(MOCK_LOCATION_TREE_JSON, LocationTree.class);
        Assert.assertEquals(gson.toJson(locationTree), allSharedPreferences.getPreference(Constants.Preference.LOCATION_TREE));
    }
}
