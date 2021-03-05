package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.navigation.NavigationView;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.LoginActivity;
import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.PatientRegisterFragment;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.RDTJsonFormUtils;

import static android.app.Activity.RESULT_OK;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_RDT_PERMISSIONS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 22/07/2020
 */
public class PatientRegisterActivityTest extends ActivityRobolectricTest {

    private PatientRegisterActivity patientRegisterActivity;
    private long currentTimeInMillis = System.currentTimeMillis();

    @Mock
    private DrawerLayout drawerLayout;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        RDTApplication.getInstance().getContext().allSharedPreferences().savePreference(Constants.Preference.CTS_LATEST_SYNC_TIMESTAMP, String.valueOf(currentTimeInMillis));
        patientRegisterActivity = Robolectric.buildActivity(PatientRegisterActivity.class)
                .create()
                .resume()
                .get();
        ReflectionHelpers.setField(patientRegisterActivity, "drawerLayout", drawerLayout);
        patientRegisterActivity.startFormActivity("", "", new HashMap<>());
    }

    @Test
    public void testOnRequestPermissionsResultShouldAlertPermissionsAreRequiredIfDenied() {
        RDTJsonFormUtils formUtils = mock(RDTJsonFormUtils.class);
        ReflectionHelpers.setField(patientRegisterActivity, "formUtils", formUtils);
        patientRegisterActivity.onRequestPermissionsResult(REQUEST_RDT_PERMISSIONS, new String[]{},
                new int[]{PackageManager.PERMISSION_DENIED});
        verify(formUtils).showToast(any(Activity.class),
                eq(patientRegisterActivity.getString(R.string.rdt_permissions_required)));
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        assertNotNull(ReflectionHelpers.getField(patientRegisterActivity, "formUtils"));
        assertNotNull(ReflectionHelpers.getField(patientRegisterActivity, "presenter"));
        assertNotNull(ReflectionHelpers.getField(patientRegisterActivity, "mBaseFragment"));
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), patientRegisterActivity
                .getResources().getConfiguration().locale.getLanguage());
    }

    @Test(expected = RuntimeException.class)
    public void testOnActivityResultShouldThrowRuntimeException() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(JsonFormConstants.RESULT_INTENT.RUNTIME_EXCEPTION, new RuntimeException("Dummy Exception!"));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        ReflectionHelpers.callInstanceMethod(patientRegisterActivity, "onActivityResult",
                ReflectionHelpers.ClassParameter.from(int.class, REQUEST_CODE_GET_JSON),
                ReflectionHelpers.ClassParameter.from(int.class, JsonFormConstants.RESULT_CODE.RUNTIME_EXCEPTION_OCCURRED),
                ReflectionHelpers.ClassParameter.from(Intent.class, intent));
    }

    @Test
    public void testOnActivityResultShouldSaveForm() {
        PatientRegisterActivityPresenter presenter = mock(PatientRegisterActivityPresenter.class);
        ReflectionHelpers.setField(patientRegisterActivity, "presenter", presenter);

        JSONObject data = new JSONObject();
        Intent intent = new Intent();
        intent.putExtra("json", data.toString());

        ReflectionHelpers.callInstanceMethod(patientRegisterActivity, "onActivityResult",
                ReflectionHelpers.ClassParameter.from(int.class, REQUEST_CODE_GET_JSON),
                ReflectionHelpers.ClassParameter.from(int.class, RESULT_OK),
                ReflectionHelpers.ClassParameter.from(Intent.class, intent));
        verify(presenter).saveForm(eq(data.toString()), eq(patientRegisterActivity));
    }

    @Test(expected = RuntimeException.class)
    public void testOnActivityResultShouldThrowExceptionIfJsonFormActivityThrewException() {
        ReflectionHelpers.callInstanceMethod(patientRegisterActivity, "onActivityResult",
                ReflectionHelpers.ClassParameter.from(int.class, REQUEST_CODE_GET_JSON),
                ReflectionHelpers.ClassParameter.from(int.class, Activity.RESULT_CANCELED),
                ReflectionHelpers.ClassParameter.from(Intent.class, null));
    }

    @Test
    public void testOpenDrawerLayoutShouldOpenDrawer() {
        patientRegisterActivity.openDrawerLayout();
        verify(drawerLayout).openDrawer(eq(GravityCompat.START));
    }

    @Test
    public void testCloseDrawerLayoutShouldCloseDrawer() {
        patientRegisterActivity.closeDrawerLayout();
        verify(drawerLayout).closeDrawer(eq(GravityCompat.START));
    }

    @Test
    public void testOnFormSavedShouldRefreshListAfterFormIsSaved() {
        PatientRegisterFragment patientRegisterFragment = mock(PatientRegisterFragment.class);
        doReturn(mock(FragmentActivity.class)).when(patientRegisterFragment).getActivity();
        ReflectionHelpers.setField(patientRegisterActivity, "mBaseFragment", patientRegisterFragment);
        patientRegisterActivity.onFormSaved();
        verify(patientRegisterFragment).refreshListView();
    }

    @Test
    public void testGetLoginPageShouldGetCorrectLoginPage() {
        assertEquals(LoginActivity.class, ReflectionHelpers.callInstanceMethod(patientRegisterActivity, "getLoginPage"));
    }

    @Test
    public void testSelectDrawerItemShouldLogoutUser() {
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(R.id.menu_item_logout).when(menuItem).getItemId();
        patientRegisterActivity.selectDrawerItem(menuItem);
        verify(RDTApplication.getInstance().getContext().userService()).logoutSession();
    }

    @Config(shadows = { UtilsShadow.class })
    @Test
    public void testSelectDrawerItemShouldSyncDataFromServer() {
        MenuItem menuItem = mock(MenuItem.class);
        doReturn(R.id.menu_item_sync).when(menuItem).getItemId();

        MockCounter counter = new MockCounter();
        UtilsShadow.setMockCounter(counter);
        assertEquals(0, UtilsShadow.getMockCounter().getCount());
        patientRegisterActivity.selectDrawerItem(menuItem);
        assertEquals(1, UtilsShadow.getMockCounter().getCount());
    }

    @Test
    public void testLatestSyncDateShouldVerifyCurrentDate() {
        NavigationView navigationView = patientRegisterActivity.findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem syncMenuItem = menu.findItem(R.id.menu_item_sync);
        String lblSync = patientRegisterActivity.getResources().getString(R.string.lbl_latest_sync);
        Assert.assertEquals(String.format(lblSync, new SimpleDateFormat(PatientRegisterActivity.LATEST_SYNC_DATE_FORMAT, Locale.getDefault()).format(new Date(currentTimeInMillis))), syncMenuItem.getTitle());
    }

    @Override
    public Activity getActivity() {
        return patientRegisterActivity;
    }
}
