package io.ona.rdt.robolectric.util;

import android.location.Location;
import android.view.View;

import com.rey.material.widget.TextView;
import com.vijay.jsonwizard.customviews.GpsDialog;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowLooper;

import java.util.Set;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.GpsDialogShadow;
import io.ona.rdt.util.RDTGpsDialog;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 04/08/2020
 */
public class RDTGpsDialogTest extends RobolectricTest {

    private GpsDialog gpsDialog;

    private RDTGpsDialog rdtGpsDialog;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        gpsDialog = new GpsDialog(RuntimeEnvironment.application,
                mock(View.class), mock(TextView.class), mock(TextView.class),
                mock(TextView.class), mock(TextView.class));
        rdtGpsDialog = new RDTGpsDialog(gpsDialog);
    }

    @Test
    public void testOnLocationChangedShouldRecordLocationChange() {
        JsonFormFragment formFragment = mock(JsonFormFragment.class);
        Location location = mock(Location.class);
        rdtGpsDialog.setFormFragment(formFragment);

        rdtGpsDialog.onLocationChanged(location);

        // assert navigates to next step
        ShadowLooper.runUiThreadTasksIncludingDelayedTasks();
        verify(rdtGpsDialog.getFormFragment()).next();

        // assert parent methods are called
        Set<Object> args = GpsDialogShadow.getArgs();
        assertTrue(args.contains(location));
        assertTrue(args.contains("saveAndDismiss"));
    }
}
