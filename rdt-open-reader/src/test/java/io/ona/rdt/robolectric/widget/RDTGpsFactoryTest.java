package io.ona.rdt.robolectric.widget;

import android.content.Context;
import android.location.LocationManager;
import android.view.View;
import android.widget.ScrollView;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ReflectionHelpers;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.RDTGpsDialog;
import io.ona.rdt.util.Utils;
import io.ona.rdt.widget.RDTGpsFactory;

/**
 * Created by Vincent Karuri on 08/11/2019
 */

public class RDTGpsFactoryTest extends WidgetFactoryRobolectricTest {

    @Mock
    private RDTJsonFormFragment formFragment;
    @Mock
    private CommonListener commonListener;
    @Mock
    private JSONObject jsonObject;
    @Mock
    private View formFragmentRootLayout;
    @Mock
    private ScrollView scrollView;
    @Mock
    private View mainLayout;

    private RDTGpsFactory gpsFactory;
    private final String STEP1 = "step1";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockMethods();
        gpsFactory = new RDTGpsFactory();
    }

    @Test
    public void testIsLocationServiceDisabledShouldReturnCorrectStatus() throws Exception {
        Context context = Mockito.mock(Context.class);
        LocationManager locationManager = Mockito.mock(LocationManager.class);
        Mockito.doReturn(locationManager).when(context).getSystemService(Context.LOCATION_SERVICE);
        String methodName = "isLocationServiceDisabled";
        
        // if both network and gps disabled
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, methodName, context);

        Assert.assertTrue(isLocationServiceDisabled);

        // if gps enabled
        Mockito.doReturn(true).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, methodName, context);

        Assert.assertFalse(isLocationServiceDisabled);

        // if network available
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(true).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = Whitebox.invokeMethod(gpsFactory, methodName, context);

        Assert.assertFalse(isLocationServiceDisabled);
    }

    @Test
    public void testGetViewsFromJsonShouldCorrectlyInitializeWidget() throws Exception {

        List<View> views = gpsFactory.getViewsFromJson(STEP1, jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        Assert.assertFalse(org.smartregister.util.Utils.isEmptyCollection(views));
        WidgetArgs widgetArgs = ReflectionHelpers.getField(gpsFactory, "widgetArgs");
        Assert.assertEquals(formFragment, widgetArgs.getFormFragment());
        Assert.assertEquals(jsonFormActivity, widgetArgs.getContext());
        Assert.assertEquals(jsonObject, widgetArgs.getJsonObject());
        Assert.assertEquals(commonListener, widgetArgs.getListener());
        Assert.assertFalse(widgetArgs.isPopup());

        View rootLayout = views.get(0);
        Assert.assertTrue(rootLayout.findViewById(R.id.record_button).isEnabled());
        Assert.assertEquals(View.GONE, rootLayout.findViewById(R.id.altitude).getVisibility());
        Assert.assertEquals(View.GONE, rootLayout.findViewById(R.id.accuracy).getVisibility());

        int margin = Utils.convertDpToPixels(jsonFormActivity, jsonFormActivity.getResources()
                        .getDimension(com.vijay.jsonwizard.R.dimen.bottom_navigation_margin));
        Mockito.verify(scrollView).setFillViewport(ArgumentMatchers.eq(true));
        Mockito.verify(scrollView).setPadding(ArgumentMatchers.eq(0), ArgumentMatchers.eq(0), ArgumentMatchers.eq(0), ArgumentMatchers.eq(margin));
        Mockito.verify(mainLayout).setPadding(ArgumentMatchers.eq(margin), ArgumentMatchers.eq(0), ArgumentMatchers.eq(margin), ArgumentMatchers.eq(0));

        RDTGpsDialog gpsDialog = ReflectionHelpers.getField(gpsFactory, "gpsDialog");
        Assert.assertNotNull(gpsDialog);
        Assert.assertEquals(formFragment, gpsDialog.getFormFragment());
    }

    @Test
    public void testShowLocationServicesDialogShouldShowDialogForDisabledLocationServices() throws Exception {
        List<View> views = gpsFactory.getViewsFromJson(STEP1, jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        View rootLayout = views.get(0);
        rootLayout.findViewById(R.id.record_button).performClick();
        Assert.assertNotNull(ShadowAlertDialog.getLatestAlertDialog());
    }

    private void mockMethods() {
        formFragmentRootLayout = Mockito.mock(View.class);
        Mockito.doReturn(formFragmentRootLayout).when(formFragment).getRootLayout();

        scrollView = Mockito.mock(ScrollView.class);
        Mockito.doReturn(scrollView).when(formFragmentRootLayout).findViewById(ArgumentMatchers.eq(com.vijay.jsonwizard.R.id.scroll_view));

        mainLayout = Mockito.mock(View.class);
        Mockito.doReturn(mainLayout).when(scrollView).findViewById(ArgumentMatchers.eq(com.vijay.jsonwizard.R.id.main_layout));
    }
}
