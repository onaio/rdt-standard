package io.ona.rdt.robolectric.activity;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.activity.CovidPatientRegisterActivity;
import io.ona.rdt.adapter.ProfileFragmentAdapter;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.fragment.CovidPatientVisitFragment;
import io.ona.rdt.presenter.CovidPatientProfileActivityPresenter;
import io.ona.rdt.presenter.PatientProfileActivityPresenter;
import io.ona.rdt.robolectric.shadow.MockCounter;
import io.ona.rdt.robolectric.shadow.UtilsShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class CovidPatientProfileActivityTest extends ActivityRobolectricTest {

    private static final int AGE = 10;
    private CovidPatientProfileActivity covidPatientProfileActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, new Patient("name", "sex", Constants.FormFields.ENTITY_ID, "patient_id", AGE, "dob"));
        covidPatientProfileActivity = Robolectric.buildActivity(CovidPatientProfileActivity.class, intent)
                .create().resume().get();
    }

    @Test
    public void testVerifyProfileFragmentViewPagerAdapterShouldReturnProfileFragmentAdapter() {
        ViewPager2 viewPage = covidPatientProfileActivity.findViewById(R.id.covid_patient_profile_fragment_container);
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
        Fragment fragment = Whitebox.invokeMethod(covidPatientProfileActivity, "getPatientProfileFragment");
        Assert.assertEquals(CovidPatientProfileFragment.class.getName(), fragment.getClass().getName());
    }

    @Test
    public void testVerifyPatientVisitFragmentCreationShouldReturnCovidPatientVisitFragment() {
        Fragment fragment = covidPatientProfileActivity.createPatientVisitFragment();
        Assert.assertEquals(CovidPatientVisitFragment.class.getName(), fragment.getClass().getName());
    }

    @Test
    public void testGetHomeActivityClassShouldReturnCovidPatientRegisterActivity() throws Exception {
        Class<?> clazz = Whitebox.invokeMethod(covidPatientProfileActivity, "getHomeActivityClass");
        Assert.assertEquals(CovidPatientRegisterActivity.class.getName(), clazz.getName());
    }

    @Test
    public void testonClickShouldVerifyBackPressedMethod() {
        View view = Mockito.mock(View.class);
        Mockito.when(view.getId()).thenReturn(R.id.btn_covid_back_to_patient_register);
        covidPatientProfileActivity.onClick(view);

        Context applicationContext = RDTApplication.getInstance().getApplicationContext();
        Intent expectedIntent = new Intent(covidPatientProfileActivity, CovidPatientRegisterActivity.class);
        Intent actualIntent = Shadows.shadowOf(new ContextWrapper(applicationContext)).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    @Test
    public void testOnDestroyShouldReturnNullWidgetMap() throws Exception {
        Whitebox.invokeMethod(covidPatientProfileActivity, "onDestroy");
        Assert.assertNull(ReflectionHelpers.getStaticField(FormKeyTextExtractionUtil.class, "formWidgetKeyToTextMap"));
    }

    @Test
    public void testUserAuthorizationVerificationTaskShouldVerifyMethodCalled() throws Exception {
        MockCounter counter = new MockCounter();
        UtilsShadow.setMockCounter(counter);
        Assert.assertEquals(0, UtilsShadow.getMockCounter().getCount());
        Whitebox.invokeMethod(covidPatientProfileActivity, "onResume");
        Assert.assertEquals(2, UtilsShadow.getMockCounter().getCount());
        UtilsShadow.setMockCounter(null);
    }

    @Override
    public Activity getActivity() {
        return covidPatientProfileActivity;
    }
}
