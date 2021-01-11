package io.ona.rdt.robolectric.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidOtherClinicalDataFragment;
import io.ona.rdt.fragment.CovidPatientProfileFragment;
import io.ona.rdt.presenter.CovidPatientProfileFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidPatientProfileFragmentTest extends RobolectricTest {

    @Mock
    private CovidPatientProfileFragmentPresenter presenter;

    private CovidPatientProfileFragment covidPatientProfileFragment;
    private Patient patient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidPatientProfileFragment = buildFragment();
        ReflectionHelpers.setField(covidPatientProfileFragment, "patientProfileFragmentPresenter", presenter);
    }

    @Test
    public void testOnClickShouldVerifyLaunchForm() {
        View view = Mockito.mock(View.class);

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_delivery_details);
        covidPatientProfileFragment.onClick(view);
        Mockito.verify(presenter).launchForm(covidPatientProfileFragment.getActivity(), CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM, patient);
    }

    @Test
    public void testLaunchOtherClinicalDataFragmentShouldVerifyLaunchFragmentClass() {
        View view = Mockito.mock(View.class);
        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_support_investigation);

        Fragment nullableActiveFragment = covidPatientProfileFragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.patient_profile_fragment_container);
        Assert.assertNull(nullableActiveFragment);

        covidPatientProfileFragment.onClick(view);

        Fragment activeFragment = covidPatientProfileFragment.getActivity().getSupportFragmentManager().findFragmentById(R.id.patient_profile_fragment_container);
        Assert.assertNotNull(activeFragment);
        Assert.assertEquals(CovidOtherClinicalDataFragment.class.getName(), activeFragment.getClass().getName());
    }

    @Test
    public void testGetFormNameShouldVerifyRelevantFormNames() throws Exception {
        View view = Mockito.mock(View.class);

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_delivery_details);
        Assert.assertEquals(CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM, Whitebox.invokeMethod(covidPatientProfileFragment, "getFormName", view));

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_rdt);
        Assert.assertEquals(CovidConstants.Form.COVID_RDT_TEST_FORM, Whitebox.invokeMethod(covidPatientProfileFragment, "getFormName", view));

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_sample_collection);
        Assert.assertEquals(CovidConstants.Form.SAMPLE_COLLECTION_FORM, Whitebox.invokeMethod(covidPatientProfileFragment, "getFormName", view));

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_symptoms_and_history);
        Assert.assertEquals(CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM, Whitebox.invokeMethod(covidPatientProfileFragment, "getFormName", view));
    }

    private CovidPatientProfileFragment buildFragment() {
        final int ten = 10;
        patient = new Patient("name", "sex", Constants.FormFields.ENTITY_ID, "patient_id", ten, "dob");

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.FormFields.PATIENT, patient);

        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, patient);

        CovidPatientProfileFragment fragment = new CovidPatientProfileFragment();
        fragment.setArguments(bundle);

        CovidPatientProfileActivity covidPatientProfileActivity = Robolectric.buildActivity(CovidPatientProfileActivity.class, intent).create().resume().get();
        FragmentManager fragmentManager = covidPatientProfileActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }
}
