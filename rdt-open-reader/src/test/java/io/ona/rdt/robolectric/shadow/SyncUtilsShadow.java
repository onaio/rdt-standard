package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.util.SyncUtils;

@Implements(SyncUtils.class)
public class SyncUtilsShadow extends Shadow {

    private boolean isLogout;

    @Implementation
    public boolean verifyAuthorization() {
        return false;
    }

    @Implementation
    public void logoutUser() {
        isLogout = true;
    }

    public boolean isLogout() {
        return isLogout;
    }
}
