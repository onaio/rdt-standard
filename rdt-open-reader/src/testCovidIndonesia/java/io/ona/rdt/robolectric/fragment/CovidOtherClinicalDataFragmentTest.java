package io.ona.rdt.robolectric.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.util.ReflectionHelpers;

import io.ona.rdt.R;
import io.ona.rdt.activity.CovidPatientProfileActivity;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.fragment.CovidOtherClinicalDataFragment;
import io.ona.rdt.presenter.CovidOtherClinicalDataFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidOtherClinicalDataFragmentTest extends RobolectricTest {

    @Mock
    private CovidOtherClinicalDataFragmentPresenter presenter;

    private CovidOtherClinicalDataFragment covidOtherClinicalDataFragment;
    private Patient patient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        covidOtherClinicalDataFragment = buildFragment();
        ReflectionHelpers.setField(covidOtherClinicalDataFragment, "presenter", presenter);
    }

    @Test
    public void testOnClickShouldVerifyLaunchForm() {
        View view = Mockito.mock(View.class);

        Mockito.when(view.getId()).thenReturn(R.id.tv_covid_wbc);
        covidOtherClinicalDataFragment.onClick(view);

        Mockito.verify(presenter).launchForm(covidOtherClinicalDataFragment.getActivity(), CovidConstants.Form.WBC_FORM, patient);
    }

    @Test
    public void testOnDestroyViewShouldVerifyTheRelevantViewVisibility() {
        View patientProfileTabbedFragmentContainer = covidOtherClinicalDataFragment.getActivity().findViewById(R.id.patient_profile_tabbed_fragment_container);
        View patientProfileFragmentContainer = covidOtherClinicalDataFragment.getActivity().findViewById(R.id.patient_profile_fragment_container);

        Assert.assertEquals(View.GONE, patientProfileTabbedFragmentContainer.getVisibility());
        Assert.assertEquals(View.VISIBLE, patientProfileFragmentContainer.getVisibility());

        covidOtherClinicalDataFragment.onDestroyView();

        Assert.assertEquals(View.VISIBLE, patientProfileTabbedFragmentContainer.getVisibility());
        Assert.assertEquals(View.GONE, patientProfileFragmentContainer.getVisibility());
    }

    private CovidOtherClinicalDataFragment buildFragment() {
        final int ten = 10;
        patient = new Patient("name", "sex", Constants.FormFields.ENTITY_ID, "patient_id", ten, "dob");

        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.FormFields.PATIENT, patient);

        Intent intent = new Intent();
        intent.putExtra(Constants.FormFields.PATIENT, patient);

        CovidOtherClinicalDataFragment fragment = new CovidOtherClinicalDataFragment();
        fragment.setArguments(bundle);

        CovidPatientProfileActivity covidPatientProfileActivity = Robolectric.buildActivity(CovidPatientProfileActivity.class, intent).create().resume().get();
        FragmentManager fragmentManager = covidPatientProfileActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(fragment, null);
        fragmentTransaction.commit();

        return fragment;
    }
}
