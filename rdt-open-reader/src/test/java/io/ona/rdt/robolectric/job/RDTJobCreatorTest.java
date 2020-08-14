package io.ona.rdt.robolectric.job;

import org.junit.Assert;
import org.junit.Test;
import org.smartregister.job.ImageUploadServiceJob;
import org.smartregister.job.PullUniqueIdsServiceJob;

import io.ona.rdt.job.ImageUploadSyncServiceJob;
import io.ona.rdt.job.RDTJobCreator;
import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public class RDTJobCreatorTest extends RobolectricTest {

    @Test
    public void testJobCreatorShouldReturnCorrectJobGivenATag() {
        RDTJobCreator jobCreator = new RDTJobCreator();
        Assert.assertTrue(jobCreator.create(RDTSyncServiceJob.TAG) instanceof RDTSyncServiceJob);
        Assert.assertTrue(jobCreator.create(ImageUploadSyncServiceJob.TAG) instanceof ImageUploadServiceJob);
        Assert.assertTrue(jobCreator.create(PullUniqueIdsServiceJob.TAG) instanceof PullUniqueIdsServiceJob);
        Assert.assertTrue(jobCreator.create(RDTSyncSettingsServiceJob.TAG) instanceof RDTSyncSettingsServiceJob);
        Assert.assertNull(jobCreator.create(""));
    }
}
