package io.ona.rdt.interactor;

import org.smartregister.domain.db.EventClient;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.domain.Visit;
import io.ona.rdt.repository.PatientHistoryRepository;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryFragmentInteractor {

    private PatientHistoryRepository patientHistoryRepository;

    public List<Visit> getVisits(String baseEntityId) {
        List<EventClient> eventClients = patientHistoryRepository.getEventsByUniqueDate(baseEntityId);
        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < visits.size(); i++) {
            visits.add(new Visit(String.format("Visit %d", i + 1), visits.get(i).getDateOfVisit()));
        }
        return visits;
    }
}
