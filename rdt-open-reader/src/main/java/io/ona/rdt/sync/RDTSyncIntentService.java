package io.ona.rdt.sync;

import android.content.Intent;

import com.google.gson.Gson;

import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.job.LocationStructureServiceJob;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.LocationServiceHelper;
import org.smartregister.sync.intent.SyncIntentService;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.Utils;

import static io.ona.rdt.util.Utils.isImageSyncEnabled;

/**
 * Created by Vincent Karuri on 28/08/2019
 */
public class RDTSyncIntentService extends SyncIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationStructureServiceJob.scheduleJobImmediately(LocationStructureServiceJob.TAG);
        super.onHandleIntent(intent);
        saveLocationTree();
        if (isImageSyncEnabled()) {
            ImageUploadSyncServiceJob.scheduleJobImmediately(ImageUploadSyncServiceJob.TAG);
        }
    }

    private void saveLocationTree() {
        String parentLocationId = Utils.getParentLocationId();
        if (parentLocationId == null) {
            return;
        }
        LocationTree locationTree = LocationServiceHelper.getInstance().getLocationHierarchy(parentLocationId);
        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        String locationTreeJson = new Gson().toJson(locationTree);
        allSharedPreferences.savePreference(Constants.Preference.LOCATION_TREE, locationTreeJson);
    }
}
