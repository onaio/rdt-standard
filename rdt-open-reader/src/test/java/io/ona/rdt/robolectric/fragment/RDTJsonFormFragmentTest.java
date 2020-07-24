package io.ona.rdt.robolectric.fragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.util.ReflectionHelpers;

import androidx.fragment.app.testing.FragmentScenario;
import io.ona.rdt.R;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Vincent Karuri on 24/07/2020
 */
public class RDTJsonFormFragmentTest extends RobolectricTest {

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testFragmentCreationShouldInitializeFragmentState() {
        FragmentScenario<RDTJsonFormFragment> fragmentScenario =
                FragmentScenario.launchInContainer(RDTJsonFormFragment.class,
                        null, R.style.AppTheme, null);

        fragmentScenario.onFragment(fragment -> {
            assertNotNull(fragment.getRootLayout());
        });
    }
}
