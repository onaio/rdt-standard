package io.ona.rdt.util;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.smartregister.Context;
import org.smartregister.SyncFilter;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.service.UserService;
import org.smartregister.view.activity.BaseLoginActivity;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.activity.LoginActivity;
import io.ona.rdt.application.RDTApplication;

import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 14/11/2019
 */
@PrepareForTest({RDTApplication.class})
public class RDTSyncConfigurationTest extends PowerMockTest {

    @Test
    public void testRDTSyncConfigurationInitialization() {
        mockStaticMethods();
        RDTSyncConfiguration syncConfiguration = new RDTSyncConfiguration();
        Assert.assertEquals(150000, syncConfiguration.getConnectTimeout());
        Assert.assertEquals(150000, syncConfiguration.getReadTimeout());
        Assert.assertEquals(BuildConfig.OPENMRS_UNIQUE_ID_INITIAL_BATCH_SIZE, syncConfiguration.getUniqueIdInitialBatchSize());
        Assert.assertEquals(BuildConfig.OPENMRS_UNIQUE_ID_BATCH_SIZE, syncConfiguration.getUniqueIdBatchSize());
        Assert.assertEquals(BuildConfig.OPENMRS_UNIQUE_ID_SOURCE, syncConfiguration.getUniqueIdSource());
        Assert.assertEquals(SyncFilter.PROVIDER, syncConfiguration.getSyncFilterParam());
        Assert.assertEquals(BuildConfig.MAX_SYNC_RETRIES, syncConfiguration.getSyncMaxRetries());
        Assert.assertEquals("provider", syncConfiguration.getSyncFilterValue());
        Assert.assertEquals(BuildConfig.OAUTH_CLIENT_ID, syncConfiguration.getOauthClientId());
        Assert.assertEquals(BuildConfig.OAUTH_CLIENT_SECRET, syncConfiguration.getOauthClientSecret());
        Assert.assertTrue(BaseLoginActivity.class.isAssignableFrom(syncConfiguration.getAuthenticationActivity()));
        assertFalse(syncConfiguration.updateClientDetailsTable());
    }

    private void mockStaticMethods() {
        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        RDTApplication rdtApplication = mock(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);


        Context drishtiContext = mock(Context.class);
        UserService userService = mock(UserService.class);
        AllSharedPreferences allSharedPreferences = mock(AllSharedPreferences.class);
        doReturn(userService).when(drishtiContext).userService();
        doReturn(allSharedPreferences).when(userService).getAllSharedPreferences();
        doReturn("provider").when(allSharedPreferences).fetchRegisteredANM();

        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);
    }
}
