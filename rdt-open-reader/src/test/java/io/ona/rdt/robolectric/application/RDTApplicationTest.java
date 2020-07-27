package io.ona.rdt.robolectric.application;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.RobolectricTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class RDTApplicationTest extends RobolectricTest {

    private RDTApplication application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        application = (RDTApplication) RuntimeEnvironment.application;
    }

    @Test
    public void testGetPasswordShouldGetCorrectPassword() {
        application.getContext().allSharedPreferences().updateANMUserName("user");
        assertEquals("password", application.getPassword());
    }

    @Test
    public void testGetPresenterShouldReturnNonNullPresenter() {
        assertNotNull(application.getPresenter());
    }

    @Test
    public void testGetParasiteProfileRepositoryShouldReturnNonNullParasiteProfileRepository() {
        assertNotNull(application.getParasiteProfileRepository());
    }
}
