package io.ona.rdt.util;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.repository.AllSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.application.RDTApplication;

/**
 * Created by Vincent Karuri on 14/06/2019
 */
public class RDTSyncConfiguration extends SyncConfiguration {

    private final int CONNECT_TIMEOUT = 150000;
    private final int READ_TIMEOUT = 150000;

    @Override
    public int getSyncMaxRetries() {
        return BuildConfig.MAX_SYNC_RETRIES;
    }

    @Override
    public SyncFilter getSyncFilterParam() {
        return SyncFilter.PROVIDER;
    }

    @Override
    public String getSyncFilterValue() {
        AllSharedPreferences sharedPreferences = RDTApplication.getInstance().getContext().userService().getAllSharedPreferences();
        return sharedPreferences.fetchRegisteredANM();
    }

    @Override
    public int getUniqueIdSource() {
        return BuildConfig.OPENMRS_UNIQUE_ID_SOURCE;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_BATCH_SIZE;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return BuildConfig.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return SyncFilter.PROVIDER;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return false;
    }

    @Override
    public int getReadTimeout() {
        return READ_TIMEOUT;
    }

    @Override
    public int getConnectTimeout() {
        return CONNECT_TIMEOUT;
    }

    @Override
    public List<String> getSynchronizedLocationTags() {
        return new ArrayList<>();
    }

    @Override
    public String getTopAllowedLocationLevel() {
        return "";
    }
}
