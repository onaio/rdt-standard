package io.ona.rdt.robolectric.shadow;

/**
 * Created by Vincent Karuri on 22/07/2020
 */

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.smartregister.repository.FormDataRepository;

@Implements(FormDataRepository.class)
public class FormDataRepositoryShadow extends Shadow {

    @Implementation
    public void  __constructor__() {

    }
}
