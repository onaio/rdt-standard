package io.ona.rdt.services;

import android.content.Intent;

import org.smartregister.sync.helper.LocationServiceHelper;
import org.smartregister.sync.intent.SyncAllLocationsIntentService;

import io.ona.rdt.util.Utils;
import timber.log.Timber;

public class RDTSyncAllLocationsIntentService extends SyncAllLocationsIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationServiceHelper locationServiceHelper = LocationServiceHelper.getInstance();

        try {
            locationServiceHelper.fetchAllLocations(Utils.getParentLocationId());
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}