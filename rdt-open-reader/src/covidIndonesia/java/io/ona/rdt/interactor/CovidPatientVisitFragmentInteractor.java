package io.ona.rdt.interactor;

import org.joda.time.DateTime;
import org.smartregister.domain.db.EventClient;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.contract.CovidPatientVisitFragmentContract;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.util.Utils;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 28/08/2020
 */
public class CovidPatientVisitFragmentInteractor {

    private PatientHistoryRepository patientHistoryRepository;
    private CovidPatientVisitFragmentContract.Presenter presenter;

    public CovidPatientVisitFragmentInteractor(CovidPatientVisitFragmentContract.Presenter presenter) {
        this.presenter = presenter;
        this.patientHistoryRepository = new PatientHistoryRepository();
    }

    public List<Visit> getPatientVisits(String baseEntityId) {
        List<EventClient> eventClients = patientHistoryRepository.getEventsByUniqueDate(baseEntityId);
        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < eventClients.size(); i++) {
            visits.add(new Visit(String.format(presenter.translateString(R.string.lbl_visit), i + 1),
                    formatDate(eventClients.get(i).getEvent().getEventDate())));
        }
        return visits;
    }

    private String formatDate(DateTime eventDate) {
        String date = null;
        try {
            date = Utils.convertDate(eventDate.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd");
        } catch (ParseException e) {
            Timber.e(e);
        }
        return date;
    }
}
