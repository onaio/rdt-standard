package io.ona.rdt.robolectric.activity;

import android.app.Activity;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;

import io.ona.rdt.activity.CovidLoginActivity;
import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.util.CovidConstants;

public class CovidLoginActivityTest extends ActivityRobolectricTest {

    private CovidLoginActivity activity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        activity = Robolectric.buildActivity(CovidLoginActivity.class).get();
    }

    @Test
    public void testGetHomeActivityClass() throws Exception {
        Class<?> clazz = Whitebox.invokeMethod(activity, "getHomeActivityClass");
        Assert.assertEquals(CovidPatientRegisterActivity.class.getName(), clazz.getName());
    }

    @Test
    public void testGetRegisterTableName() throws Exception {
        Assert.assertEquals(CovidConstants.Table.COVID_PATIENTS, Whitebox.invokeMethod(activity, "getRegisterTableName"));
    }


    @Override
    public Activity getActivity() {
        return activity;
    }
}
