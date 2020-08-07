package io.ona.rdt.robolectric.widget;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.view.View;
import android.widget.ScrollView;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.util.ReflectionHelpers;

import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.RDTGpsDialog;
import io.ona.rdt.widget.RDTGpsFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.STEP1;
import static io.ona.rdt.util.Utils.convertDpToPixels;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.smartregister.util.Utils.isEmptyCollection;

/**
 * Created by Vincent Karuri on 08/11/2019
 */

public class RDTGpsFactoryTest extends RobolectricTest {

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
    private RDTJsonFormActivity jsonFormActivity;

    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        mockMethods();
        gpsFactory = new RDTGpsFactory();
        jsonFormActivity = Robolectric.buildActivity(RDTJsonFormActivity.class,
                getJsonFormActivityIntent())
                .create()
                .resume()
                .get();
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

    @Test
    public void testGetViewsFromJsonShouldCorrectlyInitializeWidget() throws Exception {

        List<View> views = gpsFactory.getViewsFromJson("step1", jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        assertFalse(isEmptyCollection(views));
        WidgetArgs widgetArgs = ReflectionHelpers.getField(gpsFactory, "widgetArgs");
        assertEquals(formFragment, widgetArgs.getFormFragment());
        assertEquals(jsonFormActivity, widgetArgs.getContext());
        assertEquals(jsonObject, widgetArgs.getJsonObject());
        assertEquals(commonListener, widgetArgs.getListener());
        assertFalse(widgetArgs.isPopup());

        View rootLayout = views.get(0);
        assertTrue(rootLayout.findViewById(R.id.record_button).isEnabled());
        assertEquals(View.GONE, rootLayout.findViewById(R.id.altitude).getVisibility());
        assertEquals(View.GONE, rootLayout.findViewById(R.id.accuracy).getVisibility());

        int margin = convertDpToPixels(jsonFormActivity, jsonFormActivity.getResources()
                        .getDimension(com.vijay.jsonwizard.R.dimen.bottom_navigation_margin));
        verify(scrollView).setFillViewport(eq(true));
        verify(scrollView).setPadding(eq(0), eq(0), eq(0), eq(margin));
        verify(mainLayout).setPadding(eq(margin), eq(0), eq(margin), eq(0));

        RDTGpsDialog gpsDialog = ReflectionHelpers.getField(gpsFactory, "gpsDialog");
        assertNotNull(gpsDialog);
        assertEquals(formFragment, gpsDialog.getFormFragment());
    }

    @Test
    public void testShowLocationServicesDialogShouldShowDialogForDisabledLocationServices() throws Exception {
        List<View> views = gpsFactory.getViewsFromJson("step1", jsonFormActivity,
                formFragment, jsonObject, commonListener, false);

        View rootLayout = views.get(0);
        rootLayout.findViewById(R.id.record_button).performClick();
        assertNotNull(ShadowAlertDialog.getLatestAlertDialog());
    }


    private Intent getJsonFormActivityIntent() throws JSONException {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.put(STEP1, new JSONObject());
        mJSONObject.put(JsonFormConstants.ENCOUNTER_TYPE, "encounter_type");

        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.JSON, mJSONObject.toString());

        return intent;
    }

    private void mockMethods() {
        formFragmentRootLayout = mock(View.class);
        doReturn(formFragmentRootLayout).when(formFragment).getRootLayout();

        scrollView = mock(ScrollView.class);
        doReturn(scrollView).when(formFragmentRootLayout).findViewById(eq(com.vijay.jsonwizard.R.id.scroll_view));

        mainLayout = mock(View.class);
        doReturn(mainLayout).when(scrollView).findViewById(eq(com.vijay.jsonwizard.R.id.main_layout));
    }
}
