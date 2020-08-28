package io.ona.rdt.presenter;

import org.json.JSONException;

import java.util.List;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.interactor.CovidPatientHistoryFragmentInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryFragmentPresenter {

    private CovidPatientHistoryFragmentInteractor interactor;

    public CovidPatientHistoryFragmentPresenter() {
        interactor = new CovidPatientHistoryFragmentInteractor();
    }

    public List<PatientHistoryEntry> getPatientHistoryEntries(String baseEntityId, String eventType, String date) {
        List<PatientHistoryEntry> patientHistoryEntries = null;
        try {
            patientHistoryEntries = interactor.getPatientHistoryEntries(baseEntityId, eventType, date);
        } catch (JSONException e) {
            Timber.e(e);
        }
        return patientHistoryEntries;
    }
}
