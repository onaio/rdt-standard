package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.util.Constants;

@Config(shadows = {UtilsShadow.class})
public class CovidPatientProfileActivityTest extends ActivityRobolectricTest {

    private CovidPatientProfileActivity covidPatientProfileActivity;

    @Before
    public void setUp() {
        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, new Patient("name", "sex", "entity_id"));
        covidPatientProfileActivity = Robolectric.buildActivity(CovidPatientProfileActivity.class, intent)
                .create().resume().get();
    }

    @Test
    public void testUserAuthorizationVerificationTaskShouldReturnReturnValidActivityName() {
        Assert.assertEquals(covidPatientProfileActivity.getClass().getSimpleName(), UtilsShadow.getActiveContext().getClass().getSimpleName());
    }

    @Override
    public Activity getActivity() {
        return covidPatientProfileActivity;
    }
}
