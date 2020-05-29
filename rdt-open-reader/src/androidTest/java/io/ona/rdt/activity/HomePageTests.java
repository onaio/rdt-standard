package io.ona.rdt.activity;

import android.Manifest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import io.ona.rdt.utils.Constants;
import io.ona.rdt.utils.Utils;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

public class HomePageTests {


    @Rule
    public ActivityTestRule<LoginActivity> mActivityTestRule = new ActivityTestRule<>(LoginActivity.class);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule = GrantPermissionRule.grant(Manifest.permission.CAMERA);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule1 = GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule mRuntimePermissionRule2 = GrantPermissionRule.grant(Manifest.permission.INTERNET);


    private Utils utils = new Utils();

    @Before
    public void setUp() throws InterruptedException{
        utils.logIn(Constants.rdtConfigs.rdt_username, Constants.rdtConfigs.rdt_password);
    }

    @Test
    public void searchClient() throws InterruptedException{
        onView(withHint("Search"))
                .perform(clearText(), typeText(Constants.rdtConfigs.searchName));
        Thread.sleep(5000);
        onView(withText(Constants.rdtConfigs.searchName + ", 10")).check(matches(isDisplayed()));

     }

     public void tearDown() {
        utils.openDrawer();
        utils.logOut();
     }

}
