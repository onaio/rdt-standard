package io.ona.rdt.robolectric.fragment;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;

import io.ona.rdt.activity.PatientRegisterActivity;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterFragmentPresenter;
import io.ona.rdt.presenter.PatientRegisterFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.viewholder.CovidPatientRegisterViewHolder;
import io.ona.rdt.viewholder.PatientRegisterViewHolder;

public class CovidPatientRegisterFragmentTest extends RobolectricTest {

    private CovidPatientRegisterFragment covidPatientRegisterFragment;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidPatientRegisterFragment = buildFragment();
    }

    @Test
    public void testCreatePatientRegisterFragmentPresenter() throws Exception {
        PatientRegisterFragmentPresenter presenter = Whitebox.invokeMethod(covidPatientRegisterFragment, "createPatientRegisterFragmentPresenter");
        Assert.assertEquals(CovidPatientRegisterFragmentPresenter.class.getName(), presenter.getClass().getName());
    }

    @Test
    public void testGetPatientRegisterViewHolder() throws Exception {
        PatientRegisterViewHolder viewHolder = Whitebox.invokeMethod(covidPatientRegisterFragment, "getPatientRegisterViewHolder");
        Assert.assertEquals(CovidPatientRegisterViewHolder.class.getName(), viewHolder.getClass().getName());
    }

    @Test
    public void testGetPatientRegistrationForm() throws Exception {
        Assert.assertEquals(CovidConstants.Form.COVID_PATIENT_REGISTRATION_FORM, Whitebox.invokeMethod(covidPatientRegisterFragment, "getPatientRegistrationForm"));
    }

    private CovidPatientRegisterFragment buildFragment() {

        CovidPatientRegisterFragment fragment = new CovidPatientRegisterFragment();

        PatientRegisterActivity patientRegisterActivity = Robolectric.buildActivity(PatientRegisterActivity.class).create().resume().get();
        FragmentManager fragmentManager = patientRegisterActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }
}
