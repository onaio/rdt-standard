package io.ona.rdt.services;

import android.content.Intent;

import org.smartregister.location.helper.LocationHelper;
import org.smartregister.sync.helper.LocationServiceHelper;
import org.smartregister.sync.intent.SyncAllLocationsIntentService;

import timber.log.Timber;

public class RDTSyncAllLocationsIntentService extends SyncAllLocationsIntentService {

    @Override
    protected void onHandleIntent(Intent intent) {
        LocationServiceHelper locationServiceHelper = LocationServiceHelper.getInstance();

        try {
            LocationHelper locationHelper = LocationHelper.getInstance();
            locationHelper.setParentAndChildLocationIds(locationHelper.getDefaultLocation());
            locationServiceHelper.fetchAllLocations(locationHelper.getParentLocationId());
        } catch (Exception e) {
            Timber.e(e);
        }
    }
}