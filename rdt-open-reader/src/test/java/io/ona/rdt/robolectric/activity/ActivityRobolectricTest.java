package io.ona.rdt.robolectric.activity;

import android.app.Activity;

import org.junit.After;

import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public abstract class ActivityRobolectricTest extends RobolectricTest {

    @After
    public void tearDown() {
        getActivity().finish();
    }

    public abstract Activity getActivity();
}
