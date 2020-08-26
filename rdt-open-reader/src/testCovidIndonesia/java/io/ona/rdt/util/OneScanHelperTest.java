package io.ona.rdt.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.SparseArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class OneScanHelperTest {

    @Mock
    private Activity mActivity;

    @Mock
    private OneScanHelper.IntentResultCallback callback;

    @Mock
    private PackageManager packageManager;

    @Mock
    private SparseArray<OneScanHelper.IntentResultCallback> mCallbacks;

    private OneScanHelper.VersionRequest request;
    private List<ResolveInfo> list;
    private int mRequestCode = 1234;
    private OneScanHelper oneScanHelper;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        list = new ArrayList<>();
        request = new OneScanHelper.VersionRequest();

        Mockito.doReturn(packageManager).when(mActivity).getPackageManager();
        Mockito.doReturn(list).when(packageManager).queryIntentActivities(ArgumentMatchers.any(Intent.class), ArgumentMatchers.anyInt());

        oneScanHelper = new OneScanHelper(mActivity);

        ReflectionHelpers.setField(oneScanHelper, "mCallbacks", mCallbacks);
    }

    @After
    public void tearDown() {
        ReflectionHelpers.setField(oneScanHelper, "mCallbacks", null);
        list = null;
        request = null;
        oneScanHelper = null;
    }

    @Test
    public void sendShouldVerifyScenarioFirst() {

        oneScanHelper.send(request, callback);

        Mockito.verify(callback, Mockito.times(1)).onResult(Activity.RESULT_CANCELED, null);
    }

    @Test
    public void sendShouldVerifyScenarioTwo() {

        list.add(new ResolveInfo());

        oneScanHelper.send(request, callback);

        Mockito.verify(mCallbacks, Mockito.times(1)).put(mRequestCode, callback);
    }

    @Test
    public void doActivityResultShouldVerify() {

        Intent data = Mockito.mock(Intent.class);
        Bundle bundle = Mockito.mock(Bundle.class);

        Mockito.doReturn(bundle).when(data).getBundleExtra(ArgumentMatchers.anyString());
        Mockito.doReturn(callback).when(mCallbacks).get(mRequestCode);

        oneScanHelper.doActivityResult(mRequestCode, Activity.RESULT_OK, data);

        Mockito.verify(callback, Mockito.times(1)).onResult(Activity.RESULT_OK, bundle);
        Mockito.verify(mCallbacks, Mockito.times(1)).delete(mRequestCode);
    }
}
