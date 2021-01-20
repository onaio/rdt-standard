package io.ona.rdt.robolectric.fragment;

import android.content.ContextWrapper;
import android.content.Intent;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.Shadows;

import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterFragmentPresenter;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.viewholder.CovidPatientRegisterViewHolder;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

public class CovidPatientRegisterFragmentTest extends RobolectricTest {

    private CovidPatientRegisterFragment covidPatientRegisterFragment;
    private PatientRegisterActivity patientRegisterActivity;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidPatientRegisterFragment = buildFragment();
    }

    @Test
    public void testCreatePatientRegisterFragmentPresenterShouldReturnCovidPatientRegisterFragmentPresenter() throws Exception {
        PatientRegisterFragmentPresenter presenter = Whitebox.invokeMethod(covidPatientRegisterFragment, "createPatientRegisterFragmentPresenter");
        Assert.assertEquals(CovidPatientRegisterFragmentPresenter.class.getName(), presenter.getClass().getName());
    }

    @Test
    public void testGetPatientRegisterViewHolderShouldReturnCovidPatientRegisterViewHolder() throws Exception {
        PatientRegisterViewHolder viewHolder = Whitebox.invokeMethod(covidPatientRegisterFragment, "getPatientRegisterViewHolder");
        Assert.assertEquals(CovidPatientRegisterViewHolder.class.getName(), viewHolder.getClass().getName());
    }

    @Test
    public void testGetPatientRegistrationFormShouldReturnCovidPatientRegistrationForm() throws Exception {
        Assert.assertEquals(CovidConstants.Form.COVID_PATIENT_REGISTRATION_FORM, Whitebox.invokeMethod(covidPatientRegisterFragment, "getPatientRegistrationForm"));
    }

    @Test
    public void testLaunchPatientProfileShouldVerifyStartCovidPatientProfileActivity() {
        covidPatientRegisterFragment.launchPatientProfile(new Patient("", "", ""));
        Intent expectedIntent = new Intent(patientRegisterActivity, CovidPatientProfileActivity.class);
        Intent actualIntent = Shadows.shadowOf(new ContextWrapper(patientRegisterActivity)).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
    }

    private CovidPatientRegisterFragment buildFragment() {

        CovidPatientRegisterFragment fragment = new CovidPatientRegisterFragment();

        patientRegisterActivity = Robolectric.buildActivity(PatientRegisterActivity.class).create().resume().get();
        FragmentManager fragmentManager = patientRegisterActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }
}
