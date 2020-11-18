package io.ona.rdt.robolectric.widget;

import android.content.Intent;

import org.junit.Assert;
import org.robolectric.Shadows;

import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.widget.UWMalariaRDTCaptureFactory;
import io.ona.rdt.widget.UWRDTCaptureFactory;

import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

public class UWMalariaRDTCaptureFactoryTest extends UWRDTCaptureFactoryTest {

    protected void verifyCorrectActionsForRDTCapture(String entityId) {
        // verify rdt capture is launched
        Intent expectedIntent = new Intent(jsonFormActivity, CustomRDTCaptureActivity.class);
        Intent actualIntent = Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        Assert.assertEquals(entityId, actualIntent.getStringExtra(ENTITY_ID));
        Assert.assertEquals(rdtType, actualIntent.getStringExtra(UWRDTCaptureFactory.RDT_NAME));
        Assert.assertEquals(UWRDTCaptureFactory.CAPTURE_TIMEOUT_MS, actualIntent.getLongExtra(UWRDTCaptureFactory.CAPTURE_TIMEOUT, -1));
    }

    @Override
    protected UWRDTCaptureFactory getRdtCaptureFactory() {
        return new UWMalariaRDTCaptureFactory();
    }
}
