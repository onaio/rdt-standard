package io.ona.rdt.widget;

import android.content.Context;
import android.location.LocationManager;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 08/11/2019
 */
public class RDTGpsFactoryTest {

    private RDTGpsFactory gpsFactory;

    @Before
    public void setUp() {
        gpsFactory = new RDTGpsFactory();
    }

    @Test
    public void testIsLocationServiceDisabledShouldReturnCorrectStatus() throws Exception {
        Context context = mock(Context.class);
        LocationManager locationManager = mock(LocationManager.class);
        doReturn(locationManager).when(context).getSystemService(Context.LOCATION_SERVICE);

        // if both network and gps disabled
        doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, "isLocationServiceDisabled", context);

        assertTrue(isLocationServiceDisabled);

        // if gps enabled
        doReturn(true).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, "isLocationServiceDisabled", context);

        assertFalse(isLocationServiceDisabled);

        // if network available
        doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        doReturn(true).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, "isLocationServiceDisabled", context);

        assertFalse(isLocationServiceDisabled);
    }
}
