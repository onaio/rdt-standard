package io.ona.rdt.repository;

import org.smartregister.domain.db.EventClient;
import org.smartregister.repository.EventClientRepository;

import java.util.List;

import io.ona.rdt.application.RDTApplication;

/**
 * Created by Vincent Karuri on 21/08/2020
 */
public class PatientHistoryRepository {

    private final EventClientRepository eventClientRepository = RDTApplication.getInstance()
            .getContext().getEventClientRepository();

    public List<EventClient> getEventsByUniqueDate(String baseEntityId) {
        return eventClientRepository.fetchEventClientsCore(
            String.format(
                    "SELECT %s, SUBSTR(%s, %s, %s) visit_date FROM event WHERE %s=? GROUP BY 2",
                    EventClientRepository.event_column.json.toString(),
                    EventClientRepository.event_column.dateCreated.toString(), "1", "10",
                    EventClientRepository.event_column.baseEntityId.toString()
            ),
            new String[]{baseEntityId}
        );
    }

    public List<EventClient> getEventsByEventType(String baseEntityId, String eventType, String date) {
        return eventClientRepository.fetchEventClientsCore(
            String.format(
                    "SELECT %s, SUBSTR(%s, %s, %s) AS visit_date FROM event " +
                            "WHERE %s=? AND %s=? AND visit_date=?",
                    EventClientRepository.event_column.json.toString(),
                    EventClientRepository.event_column.dateCreated.toString(), "1", "10",
                    EventClientRepository.event_column.baseEntityId.toString(),
                    EventClientRepository.event_column.eventType.toString()
            ),
            new String[]{baseEntityId, eventType, date}
        );
    }
}
