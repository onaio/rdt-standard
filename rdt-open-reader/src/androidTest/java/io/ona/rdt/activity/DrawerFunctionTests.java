package io.ona.rdt.activity;

import android.Manifest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.ona.rdt.utils.Constants;
import io.ona.rdt.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class DrawerFunctionTests {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule1 = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule2 = GrantPermissionRule.grant(Manifest.permission.INTERNET);


    Utils utils = new Utils();

    @Before
    public void setUp() throws InterruptedException{
        utils.logIn(Constants.rdtConfigs.rdt_username, Constants.rdtConfigs.rdt_password);

    }
    public void sync() throws InterruptedException {
        utils.openDrawer();
        onView(withText("Sync"))
                .perform(click());
        Thread.sleep(2000);

    }

    @After
    public void tearDown()throws InterruptedException{
        utils.openDrawer();
        utils.logOut();
    }
}
