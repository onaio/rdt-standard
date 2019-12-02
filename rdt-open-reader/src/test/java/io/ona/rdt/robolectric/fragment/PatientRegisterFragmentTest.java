package io.ona.rdt.robolectric.fragment;

import org.junit.Ignore;
import org.junit.Test;

import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.robolectric.BaseRobolectricTest;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

/**
 * Created by Vincent Karuri on 21/11/2019
 */
public class PatientRegisterFragmentTest extends BaseRobolectricTest {

    @Test
    @Ignore
    public void testPatientRegisterFragment() {
        PatientRegisterFragment registerFragment = new PatientRegisterFragment();
        startFragment(registerFragment);
        assertNotNull(registerFragment);
    }
}
