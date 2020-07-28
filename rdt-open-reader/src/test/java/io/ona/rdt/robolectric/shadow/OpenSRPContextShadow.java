package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;
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

    @Implementation
    public CommonRepository commonrepository(String tablename) {
        return mock(CommonRepository.class);
    }

    @Implementation
    public UserService userService() {
        if (userService == null) {
            userService = mock(UserService.class);
            doReturn("password").when(userService).getGroupId(any());
        }
        return userService;
    }
}
