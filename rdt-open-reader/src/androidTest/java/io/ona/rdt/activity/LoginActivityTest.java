package io.ona.rdt.activity;


import android.Manifest;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.GrantPermissionRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.ona.rdt.R;
import io.ona.rdt.utils.Order;
import io.ona.rdt.utils.OrderedRunner;
import io.ona.rdt.utils.Utils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withHint;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
//@RunWith(AndroidJUnit4.class)
@RunWith(OrderedRunner.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule1 = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule2 = GrantPermissionRule.grant(Manifest.permission.INTERNET);


    private Utils utils = new Utils();

    @Test
    @Order(order = 5)
    public void testSuccessfulLogin() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(typeText("indtester1"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edit_text))
                .perform(typeText("Amani123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withHint("Search")).check(matches(isDisplayed()));
        utils.openDrawer();
        Thread.sleep(500);
        utils.logOut();
    }

    @Test
    @Order(order = 1)
    public void testEmptyCredentials() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(clearText());
        onView(withId(R.id.login_password_edit_text))
                .perform(clearText());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withText("Please check the credentials."))
                .check(matches(isDisplayed()));

    }

    @Test
    @Order(order = 2)

    public void testEmptyPassword() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(typeText("demo"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edit_text))
                .perform(clearText());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withText("Please check the credentials."))
                .check(matches(isDisplayed()));

    }

    @Test
    @Order(order = 3)
    public void testEmptyUsername() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(clearText());
        onView(withId(R.id.login_password_edit_text))
                .perform(typeText("Amani123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withText("Please check the credentials."))
                .check(matches(isDisplayed()));

    }

    @Order(order = 6)
    public void testUnauthorizedUserGroup() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(typeText("indtester2"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edit_text))
                .perform(typeText("Amani123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withText("Your user group is not allowed to access this device."))
                .check(matches(isDisplayed()));

    }

    @Test
    @Order(order = 4)
    public void testInvalidCredentials() throws InterruptedException {
        onView(withId(R.id.login_user_name_edit_text))
                .perform(typeText("demo"), closeSoftKeyboard());
        onView(withId(R.id.login_password_edit_text))
                .perform(typeText("Amani123"), closeSoftKeyboard());
        onView(withId(R.id.login_login_btn))
                .perform(click());
        Thread.sleep(5000);
        onView(withText("Please check the credentials."))
                .check(matches(isDisplayed()));

    }
}