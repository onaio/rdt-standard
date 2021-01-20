package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Intent;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.adapter.ProfileFragmentAdapter;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.fragment.CovidPatientVisitFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;
import io.ona.rdt.util.Constants;

public class CovidPatientProfileActivityTest extends ActivityRobolectricTest {

    private static final int AGE = 10;
    private CovidPatientProfileActivity activity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, new Patient("name", "sex", Constants.FormFields.ENTITY_ID, "patient_id", AGE, "dob"));
        activity = Robolectric.buildActivity(CovidPatientProfileActivity.class, intent).create().get();
    }

    @Test
    public void testVerifyProfileFragmentViewPagerAdapterShouldReturnProfileFragmentAdapter() {
        ViewPager2 viewPage = activity.findViewById(R.id.covid_patient_profile_fragment_container);
        Assert.assertEquals(ProfileFragmentAdapter.class.getName(), viewPage.getAdapter().getClass().getName());
    }

    @Test
    public void testGetContentViewIdShouldReturnActivityCovidPatientProfileId() throws Exception {
        Assert.assertEquals(R.layout.activity_covid_patient_profile, (int) Whitebox.invokeMethod(activity, "getContentViewId"));
    }

    @Test
    public void testVerifyActivityPresenterShouldReturnCovidPatientProfileActivityPresenter() {
        PatientProfileActivityPresenter presenter = ReflectionHelpers.getField(activity, "presenter");
        Assert.assertEquals(CovidPatientProfileActivityPresenter.class.getName(), presenter.getClass().getName());
    }

    @Test
    public void testVerifyPatientProfileFragmentShouldReturnCovidPatientProfileFragment() throws Exception {
        Fragment fragment = Whitebox.invokeMethod(activity, "getPatientProfileFragment");
        Assert.assertEquals(CovidPatientProfileFragment.class.getName(), fragment.getClass().getName());
    }

    @Test
    public void testVerifyPatientVisitFragmentCreationShouldReturnCovidPatientVisitFragment() {
        Fragment fragment = activity.createPatientVisitFragment();
        Assert.assertEquals(CovidPatientVisitFragment.class.getName(), fragment.getClass().getName());
    }

    @Test
    public void testGetHomeActivityClassShouldReturnCovidPatientRegisterActivity() throws Exception {
        Class<?> clazz = Whitebox.invokeMethod(activity, "getHomeActivityClass");
        Assert.assertEquals(CovidPatientRegisterActivity.class.getName(), clazz.getName());
    }

    @Override
    public Activity getActivity() {
        return activity;
    }
}