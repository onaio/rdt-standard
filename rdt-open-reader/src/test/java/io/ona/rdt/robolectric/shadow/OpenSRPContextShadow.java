package io.ona.rdt.robolectric.shadow;

import android.preference.PreferenceManager;

import net.sqlcipher.database.SQLiteDatabase;

import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.domain.UniqueId;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.ImageRepository;
import org.smartregister.repository.Repository;
import org.smartregister.repository.SettingsRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

/**
 * Created by Vincent Karuri on 24/07/2020
 */

@Implements(Context.class)
public class OpenSRPContextShadow extends Shadow {

    private UserService userService;
    private CommonRepository commonRepository;
    private static UniqueIdRepository uniqueIdRepository;
    private EventClientRepository eventClientRepository;
    private static AllSettings allSettings;
    private ImageRepository imageRepository;

    @Implementation
    public CommonRepository commonrepository(String tablename) {
        if (commonRepository == null) {
            commonRepository = Mockito.mock(CommonRepository.class);
        }
        return commonRepository;
    }

    @Implementation
    public UserService userService() {
        if (userService == null) {
            userService = Mockito.mock(UserService.class);
            doReturn("password").when(userService).getGroupId(any());
        }
        return userService;
    }

    @Implementation
    public UniqueIdRepository getUniqueIdRepository() {
        if (uniqueIdRepository == null) {
            uniqueIdRepository = Mockito.mock(UniqueIdRepository.class);
            UniqueId uniqueId = new UniqueId();
            uniqueId.setOpenmrsId("openmrsID1");
            doReturn(uniqueId).when(uniqueIdRepository).getNextUniqueId();
        }
        return uniqueIdRepository;
    }

    @Implementation
    public EventClientRepository getEventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = Mockito.mock(EventClientRepository.class);
        }
        return eventClientRepository;
    }

    @Implementation
    public AllSettings allSettings() {
        if (allSettings == null) {
            SettingsRepository settingsRepository = new SettingsRepository();
            ReflectionHelpers.setField(settingsRepository, "masterRepository", getMasterRepository());
            allSettings = new AllSettings(new AllSharedPreferences(PreferenceManager.getDefaultSharedPreferences(RuntimeEnvironment.application)), settingsRepository);
            ReflectionHelpers.setField(allSettings, "settingsRepository", settingsRepository);
        }
        return allSettings;
    }

    @Implementation
    public ImageRepository imageRepository() {
        if (imageRepository == null) {
            imageRepository = Mockito.spy(new ImageRepository());
            imageRepository.updateMasterRepository(getMasterRepository());
        }
        return imageRepository;
    }

    private Repository getMasterRepository() {
        Repository masterRepository = Mockito.mock(Repository.class);
        SQLiteDatabase db = Mockito.mock(SQLiteDatabase.class);
        doReturn(db).when(masterRepository).getWritableDatabase();
        return masterRepository;
    }

    public static void setUniqueIdRepository(UniqueIdRepository uniqueIdRepository) {
        OpenSRPContextShadow.uniqueIdRepository = uniqueIdRepository;
    }

    public static void setAllSettings(AllSettings allSettings) {
        OpenSRPContextShadow.allSettings = allSettings;
    }
}
