package io.ona.rdt.robolectric.shadow;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.Context;
import org.smartregister.commonregistry.CommonRepository;

import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 24/07/2020
 */

@Implements(Context.class)
public class OpenSRPContextShadow extends Shadow {

    @Implementation
    public CommonRepository commonrepository(String tablename) {
        return mock(CommonRepository.class);
    }
}
