package io.ona.rdt_app.util;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;

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
        return null;
    }

    @Override
    public String getSyncFilterValue() {
        return null;
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
