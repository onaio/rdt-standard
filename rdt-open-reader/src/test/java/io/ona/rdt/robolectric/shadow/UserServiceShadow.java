package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.service.UserService;

/**
 * Created by Vincent Karuri on 27/07/2020
 */

@Implements(UserService.class)
public class UserServiceShadow extends Shadow {

    @Implementation
    public String getGroupId(String userName) {
        return "password";
    }
}
