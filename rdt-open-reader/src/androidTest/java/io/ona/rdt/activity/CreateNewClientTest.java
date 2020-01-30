package io.ona.rdt.activity;


import android.Manifest;
import android.support.test.espresso.action.TypeTextAction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.ona.rdt.R;
import io.ona.rdt.utils.Constants;
import io.ona.rdt.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

public class CreateNewClientTest {

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
    public void setUp() throws InterruptedException {
        utils.logIn(Constants.rdtConfigs.rdt_username, Constants.rdtConfigs.rdt_password);
    }

    @Test
    public void createNewClient() throws InterruptedException {
        onView(withId(R.id.btn_register_patient)).perform(click());
        onView(withText("Name")).perform(typeText("Another"));
        Thread.sleep(5000);
    }
}
