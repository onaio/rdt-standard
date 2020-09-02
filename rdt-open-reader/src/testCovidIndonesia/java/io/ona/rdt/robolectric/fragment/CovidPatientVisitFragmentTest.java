package io.ona.rdt.robolectric.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.recyclerview.widget.RecyclerView;
import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.fragment.CovidPatientVisitFragment;
import io.ona.rdt.presenter.CovidPatientVisitFragmentPresenter;
import io.ona.rdt.util.Constants;
import io.ona.rdt.viewholder.CovidPatientVisitViewHolder;

/**
 * Created by Vincent Karuri on 21/08/2020
 */
public class CovidPatientVisitFragmentTest extends FragmentRobolectricTest {

    private CovidPatientVisitFragment covidPatientVisitFragment;
    private FragmentScenario<CovidPatientVisitFragment> fragmentScenario;

    @Before
    public void setUp() {
        final int ten = 10;
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.FormFields.PATIENT, new Patient("name", "sex", Constants.FormFields.ENTITY_ID, "patient_id", ten));
        fragmentScenario = FragmentScenario.launchInContainer(CovidPatientVisitFragment.class, bundle,
                R.style.AppTheme, null);
        fragmentScenario.onFragment(
                fragment -> {
                    covidPatientVisitFragment = fragment;
                }
        );
    }

    @Test
    public void testOnCreateViewShouldCorrectlyPopulatePatientVisits() {
        CovidPatientVisitFragmentPresenter presenter = Mockito.mock(CovidPatientVisitFragmentPresenter.class);
        List<Visit> visits = new ArrayList<>();
        Visit visit = new Visit("visit1", "date1");
        visits.add(visit);
        visit = new Visit("visit2", "date2");
        visits.add(visit);
        visit = new Visit("visit3", "date3");
        visits.add(visit);

        Mockito.doReturn(visits).when(presenter).getPatientVisits(ArgumentMatchers.anyString());
        ReflectionHelpers.setField(covidPatientVisitFragment, "presenter", presenter);
        View rootLayout = covidPatientVisitFragment.onCreateView(LayoutInflater.from(RDTApplication.getInstance()),
                (ViewGroup) covidPatientVisitFragment.getView().getParent(), null);

        final int visitOne = 1;
        final int visitTwo = 2;
        final int visitThree = 3;

        RecyclerView visitList = rootLayout.findViewById(R.id.covid_patient_visit_list);
        final int zero = 0;
        final int hundred = 100;
        final int thousand = 1000;
        visitList.measure(zero, zero);
        visitList.layout(zero, zero, hundred, thousand);

        assertListValuesAreCorrect(visitList, visitOne);
        assertListValuesAreCorrect(visitList, visitTwo);
        assertListValuesAreCorrect(visitList, visitThree);
    }

    private void assertListValuesAreCorrect(RecyclerView visitList, int position) {
        CovidPatientVisitViewHolder viewHolder = (CovidPatientVisitViewHolder) visitList
                .findViewHolderForAdapterPosition(position - 1);
        Assert.assertEquals(String.format("visit%d", position), viewHolder.getTvVisitName().getText());
        Assert.assertEquals(String.format("date%d", position), viewHolder.getTvDateOfVisit().getText());
    }

    @Override
    public FragmentScenario getFragmentScenario() {
        return fragmentScenario;
    }
}
