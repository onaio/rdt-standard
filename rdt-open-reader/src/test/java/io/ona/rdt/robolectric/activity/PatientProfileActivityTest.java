package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.util.ReflectionHelpers;

import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.R;
import io.ona.rdt.activity.PatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;

import static io.ona.rdt.util.Constants.FormFields.PATIENT;
import static io.ona.rdt.util.Constants.RequestCodes.REQUEST_CODE_GET_JSON;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 22/07/2020
 */
public class PatientProfileActivityTest extends ActivityRobolectricTest {

    @Mock
    private PatientProfileActivityPresenter presenter;

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
        patientProfileActivity = controller.create().get();

        ReflectionHelpers.setField(patientProfileActivity, "presenter", presenter);
    }

    @Test
    public void testOnCreateShouldCorrectlyInitializeActivity() {
        assertNotNull(ReflectionHelpers.getField(patientProfileActivity, "presenter"));
        assertEquals(new Locale(BuildConfig.LOCALE).getLanguage(), patientProfileActivity
                .getResources().getConfiguration().locale.getLanguage());
        assertNotNull(patientProfileActivity.getSupportFragmentManager()
                .findFragmentById(R.id.patient_profile_fragment_container));
    }

    @Test(expected = RuntimeException.class)
    public void testOnActivityResultShouldThrowRuntimeException() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(JsonFormConstants.RESULT_INTENT.RUNTIME_EXCEPTION, new RuntimeException("Dummy Exception!"));
        Intent intent = new Intent();
        intent.putExtras(bundle);
        ReflectionHelpers.callInstanceMethod(patientProfileActivity, "onActivityResult",
                ReflectionHelpers.ClassParameter.from(int.class, REQUEST_CODE_GET_JSON),
                ReflectionHelpers.ClassParameter.from(int.class, JsonFormConstants.RESULT_CODE.RUNTIME_EXCEPTION_OCCURRED),
                ReflectionHelpers.ClassParameter.from(Intent.class, intent));
    }

    @Test
    public void testOnActivityResultShouldSaveForm() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", "value");
        Intent intent = new Intent();
        intent.putExtra("json", jsonObject.toString());

        ReflectionHelpers.callInstanceMethod(patientProfileActivity, "onActivityResult",
                ReflectionHelpers.ClassParameter.from(int.class, REQUEST_CODE_GET_JSON),
                ReflectionHelpers.ClassParameter.from(int.class, Activity.RESULT_OK),
                ReflectionHelpers.ClassParameter.from(Intent.class, intent));

        verify(presenter).saveForm(eq(jsonObject.toString()), isNull());
    }

    @Override
    public Activity getActivity() {
        return patientProfileActivity;
    }
}
