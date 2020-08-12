package io.ona.rdt.robolectric.shadow;

import org.mockito.Mockito;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.service.UserService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 24/07/2020
 */

@Implements(Context.class)
public class OpenSRPContextShadow extends Shadow {

    private UserService userService;
    private CommonRepository commonRepository;
    private UniqueIdRepository uniqueIdRepository;
    private EventClientRepository eventClientRepository;

    @Implementation
    public CommonRepository commonrepository(String tablename) {
        if (commonRepository == null) {
            commonRepository = mock(CommonRepository.class);
        }
        return commonRepository;
    }

    @Implementation
    public UserService userService() {
        if (userService == null) {
            userService = mock(UserService.class);
            doReturn("password").when(userService).getGroupId(any());
        }
        return userService;
    }

   @Implementation
    public UniqueIdRepository getUniqueIdRepository() {
        if (uniqueIdRepository == null) {
            uniqueIdRepository = mock(UniqueIdRepository.class);
        }
        return uniqueIdRepository;
    }

    @Implementation
    public EventClientRepository getEventClientRepository() {
        if (eventClientRepository == null) {
            eventClientRepository = mock(EventClientRepository.class);
        }
        return eventClientRepository;
    }
}
