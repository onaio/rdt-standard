package io.ona.rdt.robolectric.presenter;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.Visit;
import io.ona.rdt.interactor.CovidPatientVisitFragmentInteractor;
import io.ona.rdt.presenter.CovidPatientVisitFragmentPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

public class CovidPatientVisitFragmentPresenterTest extends RobolectricTest {

    private static final int LIST_SIZE = 1;
    private CovidPatientVisitFragmentPresenter covidPatientVisitFragmentPresenter;

    @Before
    public void setUp() {
        covidPatientVisitFragmentPresenter = new CovidPatientVisitFragmentPresenter(null);
    }

    @Test
    public void testGetPatientVisits() {
        CovidPatientVisitFragmentInteractor interactor = Mockito.mock(CovidPatientVisitFragmentInteractor.class);
        ReflectionHelpers.setField(covidPatientVisitFragmentPresenter, "interactor", interactor);

        String visitName = "visit name";
        String dateOfVisit = "12-16-2020";
        Visit visit = new Visit(visitName, dateOfVisit);

        List<Visit> list = new ArrayList<>();
        list.add(visit);

        Mockito.when(interactor.getPatientVisits(ArgumentMatchers.anyString())).thenReturn(list);

        List<Visit> visitList = covidPatientVisitFragmentPresenter.getPatientVisits("");
        Assert.assertEquals(LIST_SIZE, visitList.size());
        Assert.assertEquals(visitName, visitList.get(LIST_SIZE - 1).getVisitName());
        Assert.assertEquals(dateOfVisit, visitList.get(LIST_SIZE - 1).getDateOfVisit());
    }
}
