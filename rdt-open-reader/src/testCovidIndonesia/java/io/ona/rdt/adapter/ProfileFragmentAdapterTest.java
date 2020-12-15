package io.ona.rdt.adapter;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.junit.Assert;
import org.junit.Test;
import org.robolectric.Robolectric;

import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.fragment.CovidPatientVisitFragment;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;

public class ProfileFragmentAdapterTest extends RobolectricTest {

    private static final int FRAGMENT_COUNT = 2;
    private static final int INDEX_COVID_PATIENT_PROFILE_FRAGMENT = 0;
    private static final int INDEX_COVID_PATIENT_VISIT_FRAGMENT = 1;

    private ProfileFragmentAdapter adapter;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        CovidPatientProfileActivity activity = Robolectric.buildActivity(CovidPatientProfileActivity.class, getIntent()).create().resume().get();
        adapter = new ProfileFragmentAdapter(activity);
    }

    @Test
    public void testCreateFragment() {

        Fragment covidPatientProfileFragment = adapter.createFragment(INDEX_COVID_PATIENT_PROFILE_FRAGMENT);
        Fragment covidPatientVisitFragment = adapter.createFragment(INDEX_COVID_PATIENT_VISIT_FRAGMENT);

        Assert.assertNotNull(covidPatientProfileFragment);
        Assert.assertNotNull(covidPatientVisitFragment);
        Assert.assertEquals(CovidPatientProfileFragment.class.getName(), covidPatientProfileFragment.getClass().getName());
        Assert.assertEquals(CovidPatientVisitFragment.class.getName(), covidPatientVisitFragment.getClass().getName());
    }

    @Test
    public void testGetItemCount() {
        Assert.assertEquals(FRAGMENT_COUNT, adapter.getItemCount());
    }

    private Intent getIntent() {
        Patient patient = new Patient("patient 1", "Male", "324-343-3sdf3-3", "23", 20, "11-8-2000");
        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, patient);
        return intent;
    }
}
