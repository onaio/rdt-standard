package io.ona.rdt.presenter;

import org.json.JSONException;

import java.util.List;

import io.ona.rdt.contract.CovidPatientHistoryActivityContract;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.interactor.CovidPatientHistoryActivityInteractor;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryActivityPresenter {

    private CovidPatientHistoryActivityInteractor interactor;
    private CovidPatientHistoryActivityContract.View view;

    public CovidPatientHistoryActivityPresenter(CovidPatientHistoryActivityContract.View view) {
        interactor = new CovidPatientHistoryActivityInteractor();
        this.view = view;
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
