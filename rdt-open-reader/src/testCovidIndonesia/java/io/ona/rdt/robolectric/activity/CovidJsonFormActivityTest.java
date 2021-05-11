package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.rengwuxian.materialedittext.MaterialEditText;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.activity.CovidJsonFormActivity;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.shadow.ContextCompatShadow;
import io.ona.rdt.shadow.LocationServicesShadow;

/**
 * Created by Vincent Karuri on 16/09/2020
 */
public class CovidJsonFormActivityTest extends JsonFormActivityTest {

    private CovidJsonFormActivity jsonFormActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        jsonFormActivity = Robolectric.buildActivity(CovidJsonFormActivity.class, intent).create().get();
        jsonFormActivity = Mockito.spy(jsonFormActivity);
    }

    @Test
    public void testDispatchTouchEventShouldClearFocusOnMaterialEditTextWhenUserScrollsDown() {
        MotionEvent motionEvent = Mockito.mock(MotionEvent.class);
        Mockito.doReturn(MotionEvent.ACTION_DOWN).when(motionEvent).getAction();

        MaterialEditText editText = new MaterialEditText(jsonFormActivity);
        editText.requestFocus();
        ((ViewGroup) jsonFormActivity.findViewById(android.R.id.content).getRootView()).addView(editText);

        Assert.assertTrue(editText.hasFocus());

        Mockito.doReturn(editText).when(jsonFormActivity).getCurrentFocus();
        jsonFormActivity.dispatchTouchEvent(motionEvent);

        Assert.assertFalse(editText.hasFocus());
    }

    @Config(shadows = {RDTJsonFormUtilsShadow.class})
    @Test
    public void testOnCreateShouldRequestShowLocationServicesDialogIfLocationServicesAreDisabled() {
        jsonFormActivity = Robolectric.buildActivity(CovidJsonFormActivity.class, intent).get();
        RDTJsonFormUtilsShadow.setMockCounter(new MockCounter());
        RDTJsonFormUtilsShadow.setIsLocationServiceDisabled(true);

        Assert.assertEquals(0, RDTJsonFormUtilsShadow.getMockCounter().getCount());

        ReflectionHelpers.callInstanceMethod(jsonFormActivity, "onCreate", ReflectionHelpers.ClassParameter.from(Bundle.class, null));

        Assert.assertEquals(1, RDTJsonFormUtilsShadow.getMockCounter().getCount());
        RDTJsonFormUtilsShadow.setMockCounter(null);
        RDTJsonFormUtilsShadow.setIsLocationServiceDisabled(false);
    }

    @Config(shadows = { LocationServicesShadow.class, ContextCompatShadow.class })
    @Test
    public void testOnCreateShouldStartCollectingLocationData() {
        jsonFormActivity = Mockito.spy(Robolectric.buildActivity(CovidJsonFormActivity.class, intent).get());
        ContextCompatShadow.setIsLocationPermissionGranted(true);

        FusedLocationProviderClient fusedLocationProviderClient = Mockito.mock(FusedLocationProviderClient.class);
        Task task = Mockito.mock(Task.class);
        Mockito.doReturn(task).when(fusedLocationProviderClient).getLastLocation();
        LocationServicesShadow.setFusedLocationProviderClient(fusedLocationProviderClient);

        ReflectionHelpers.callInstanceMethod(jsonFormActivity, "onCreate", ReflectionHelpers.ClassParameter.from(Bundle.class, null));
        Mockito.verify(fusedLocationProviderClient).requestLocationUpdates(ArgumentMatchers.any(LocationRequest.class), ArgumentMatchers.any(LocationCallback.class), ArgumentMatchers.any());
        Mockito.verify(task).addOnSuccessListener(ArgumentMatchers.any(Activity.class), ArgumentMatchers.any(OnSuccessListener.class));

        RDTJsonFormUtilsShadow.setMockCounter(null);
        ContextCompatShadow.setIsLocationPermissionGranted(false);
        LocationServicesShadow.setFusedLocationProviderClient(null);
    }

    @Override
    public Activity getActivity() {
        return jsonFormActivity;
    }
}
