package io.ona.rdt.robolectric.fragment;

import org.junit.After;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.Lifecycle;
import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public abstract class FragmentRobolectricTest extends RobolectricTest {

    @After
    public void tearDown() {
        getFragmentScenario().moveToState(Lifecycle.State.DESTROYED);
    }

    public abstract FragmentScenario getFragmentScenario();
}
