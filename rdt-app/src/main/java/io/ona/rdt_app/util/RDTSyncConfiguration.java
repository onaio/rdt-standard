package io.ona.rdt_app.util;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.repository.AllSharedPreferences;

import io.ona.rdt_app.application.RDTApplication;

/**
 * Created by Vincent Karuri on 14/06/2019
 */
public class RDTSyncConfiguration extends SyncConfiguration {
    @Override
    public int getSyncMaxRetries() {
        return 0;
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
        return 0;
    }

    @Override
    public int getUniqueIdBatchSize() {
        return 0;
    }

    @Override
    public int getUniqueIdInitialBatchSize() {
        return 0;
    }

    @Override
    public SyncFilter getEncryptionParam() {
        return null;
    }

    @Override
    public boolean updateClientDetailsTable() {
        return false;
    }
}
