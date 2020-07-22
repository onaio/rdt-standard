package io.ona.rdt.robolectric.activity;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.robolectric.RobolectricTest;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Vincent Karuri on 22/07/2020
 */
public class PatientProfileActivityTest extends RobolectricTest {

    private ActivityController<PatientProfileActivity> controller;
    private PatientProfileActivity patientProfileActivity;
    private Intent intent;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Patient patient = new Patient("name", "sex", "entity_id", "patient_id");
        intent = new Intent();
        intent.putExtra(PATIENT, patient);

        controller = Robolectric.buildActivity(PatientProfileActivity.class, intent);
        patientProfileActivity = controller.create()
                .resume()
                .get();
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        assertNotNull(ReflectionHelpers.getField(patientProfileActivity, "presenter"));
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), patientProfileActivity
                .getResources().getConfiguration().locale.getLanguage());
        assertNotNull(patientProfileActivity.getSupportFragmentManager()
                .findFragmentById(R.id.patient_profile_fragment_container));
    }
}
