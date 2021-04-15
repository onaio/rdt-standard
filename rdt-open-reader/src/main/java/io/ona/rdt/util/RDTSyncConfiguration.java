package io.ona.rdt.util;

import android.text.TextUtils;

import org.smartregister.SyncConfiguration;
import org.smartregister.SyncFilter;
import org.smartregister.domain.jsonmapping.Location;
import org.smartregister.domain.jsonmapping.util.LocationTree;
import org.smartregister.domain.jsonmapping.util.TreeNode;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.sync.helper.LocationServiceHelper;
import org.smartregister.view.activity.BaseLoginActivity;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.activity.LoginActivity;
import io.ona.rdt.application.RDTApplication;
import timber.log.Timber;

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
        return BuildConfig.SYNC_FILTER_PARAM.equals(Constants.Config.LOCATION)
                ? SyncFilter.LOCATION_ID : SyncFilter.PROVIDER;
    }

    @Override
    public String getSyncFilterValue() {
        AllSharedPreferences sharedPreferences = RDTApplication.getInstance().getContext().userService().getAllSharedPreferences();
        String provider = sharedPreferences.fetchRegisteredANM();
        if (BuildConfig.SYNC_FILTER_PARAM.equals(Constants.Config.LOCATION)) {
            LocationServiceHelper locationServiceHelper = LocationServiceHelper.getInstance();

            String locationId = sharedPreferences.fetchDefaultLocalityId(provider);
            LocationTree locationTree = locationServiceHelper.getLocationHierarchy(locationId);

            if (locationTree != null) {
                List<String> locationIds = fetchLocationIds(locationTree.getLocationsHierarchy(), new ArrayList<>());
                return TextUtils.join(",", locationIds);
            } else {
                return locationId;
            }
        } else {
            return provider;
        }
    }

    private List<String> fetchLocationIds(LinkedHashMap<String, TreeNode<String, Location>> map, List<String> locationIds) {
        if (map != null) {
            for (Map.Entry<String, TreeNode<String, Location>> e : map.entrySet()) {
                locationIds.add(e.getKey());

                TreeNode<String, Location> node = e.getValue();
                fetchLocationIds(node.getChildren(), locationIds);
            }
        }
        return locationIds;
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

    @Override
    public String getOauthClientId() {
        return BuildConfig.OAUTH_CLIENT_ID;
    }

    @Override
    public String getOauthClientSecret() {
        return BuildConfig.OAUTH_CLIENT_SECRET;
    }

    @Override
    public Class<? extends BaseLoginActivity> getAuthenticationActivity() {
        Class loginActivityClass = LoginActivity.class;
        try {
            loginActivityClass = Class.forName(BuildConfig.LOGIN_ACTIVITY);
        } catch (ClassNotFoundException e) {
            Timber.e(e);
        }
        return loginActivityClass;
    }

    @Override
    public boolean disableSyncToServerIfUserIsDisabled() {
        return true;
    }
}
