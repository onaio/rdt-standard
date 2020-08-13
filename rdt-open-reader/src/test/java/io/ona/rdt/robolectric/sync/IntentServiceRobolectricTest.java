package io.ona.rdt.robolectric.sync;

import org.junit.After;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;

/**
 * Created by Vincent Karuri on 12/08/2020
 */
public abstract class IntentServiceRobolectricTest extends RobolectricTest {

    @After
    public void tearDown() {
        BaseJobShadow.setJobTag(null);
    }
}
