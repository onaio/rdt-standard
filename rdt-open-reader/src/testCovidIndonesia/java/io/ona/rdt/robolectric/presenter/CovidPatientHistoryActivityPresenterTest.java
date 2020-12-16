package io.ona.rdt.robolectric.presenter;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.util.ReflectionHelpers;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.interactor.CovidPatientHistoryActivityInteractor;
import io.ona.rdt.presenter.CovidPatientHistoryActivityPresenter;
import io.ona.rdt.robolectric.RobolectricTest;

public class CovidPatientHistoryActivityPresenterTest extends RobolectricTest {

    private static final int LIST_SIZE = 1;
    private CovidPatientHistoryActivityPresenter covidPatientHistoryActivityPresenter;

    @Before
    public void setUp() {
        covidPatientHistoryActivityPresenter = new CovidPatientHistoryActivityPresenter(null);
    }

    @Test
    public void testGetPatientHistoryEntries() throws JSONException {

        String patientKey = "patient_key";
        String patientVal = "patient_val";

        PatientHistoryEntry patientHistoryEntry = new PatientHistoryEntry(patientKey, patientVal);
        List<PatientHistoryEntry> list = new ArrayList<>(LIST_SIZE);
        list.add(patientHistoryEntry);

        CovidPatientHistoryActivityInteractor interactor = Mockito.mock(CovidPatientHistoryActivityInteractor.class);
        ReflectionHelpers.setField(covidPatientHistoryActivityPresenter, "interactor", interactor);

        Mockito.when(interactor.getPatientHistoryEntries(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString())).thenReturn(list);
        List<PatientHistoryEntry> patientList = covidPatientHistoryActivityPresenter.getPatientHistoryEntries("", "", "");

        Assert.assertEquals(LIST_SIZE, patientList.size());
        Assert.assertEquals(patientKey, patientList.get(LIST_SIZE - 1).getKey());
        Assert.assertEquals(patientVal, patientList.get(LIST_SIZE - 1).getValue());
    }
}
