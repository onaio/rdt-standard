package io.ona.rdt.robolectric.activity;

import android.app.Activity;

import org.junit.After;
import org.junit.Before;
import org.smartregister.util.LangUtils;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public abstract class ActivityRobolectricTest extends RobolectricTest {

    @Before
    public void updateLocale() {
        LangUtils.saveLanguage(RDTApplication.getInstance(), BuildConfig.LOCALE);
    }

    @After
    public void tearDown() {
        getActivity().finish();
    }

    public abstract Activity getActivity();
}
