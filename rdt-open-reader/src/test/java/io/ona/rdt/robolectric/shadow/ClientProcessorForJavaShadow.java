package io.ona.rdt.robolectric.shadow;

import android.content.Context;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.sync.ClientProcessorForJava;

import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 12/08/2020
 */

@Implements(ClientProcessorForJava.class)
public class ClientProcessorForJavaShadow extends Shadow {

    private static ClientProcessorForJava clientProcessorForJava;

    @Implementation
    public static ClientProcessorForJava getInstance(Context context) {
        if (clientProcessorForJava == null) {
            clientProcessorForJava = mock(ClientProcessorForJava.class);
        }
        return clientProcessorForJava;
    }
}
