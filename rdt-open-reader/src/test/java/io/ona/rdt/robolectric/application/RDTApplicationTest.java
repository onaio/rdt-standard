package io.ona.rdt.robolectric.application;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.RobolectricTest;

import static org.junit.Assert.assertNotNull;

/**
 * Created by Vincent Karuri on 27/07/2020
 */
public class RDTApplicationTest extends RobolectricTest {

    private RDTApplication application;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        application = RDTApplication.getInstance();
    }

    @Test
    public void testGetPresenterShouldReturnNonNullPresenter() {
        assertNotNull(application.getPresenter());
    }

    @Test
    public void testGetParasiteProfileRepositoryShouldReturnNonNullParasiteProfileRepository() {
        assertNotNull(application.getParasiteProfileRepository());
    }

    @Test
    public void testGetRepositoryShouldReturnNonNullRepository() {
        assertNotNull(application.getRepository());
    }

    @Test
    public void testGetRdtTestsRepositoryShouldReturnNonNullRepository() {
        assertNotNull(application.getRdtTestsRepository());
    }

    @Test
    public void testGetSyncUtilsShouldReturnNoNull() {
        Assert.assertNotNull(application.getSyncUtils());
    }
}
