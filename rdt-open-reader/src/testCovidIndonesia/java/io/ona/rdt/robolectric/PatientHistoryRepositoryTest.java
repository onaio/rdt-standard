package io.ona.rdt.robolectric;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.repository.PatientHistoryRepository;

import static org.mockito.ArgumentMatchers.eq;

/**
 * Created by Vincent Karuri on 21/08/2020
 */
public class PatientHistoryRepositoryTest extends RobolectricTest {

    private PatientHistoryRepository patientHistoryRepository;
    private EventClientRepository eventClientRepository;

    @Before
    public void setUp() {
        patientHistoryRepository = new PatientHistoryRepository();
        eventClientRepository =  RDTApplication.getInstance().getContext().getEventClientRepository();
    }

    @Test
    public void testGetEventsByEventTypeShouldExecuteCorrectQueryOnECRepository() {
        patientHistoryRepository.getEventsByEventType("base_entity_id", "event_type", "date");
        String query = "SELECT json, SUBSTR(dateCreated, 1, 10) AS visit_date FROM event WHERE baseEntityId=? AND eventType=? AND visit_date=?";
        String[] params = new String[]{"base_entity_id", "event_type", "date"};
        Mockito.verify(eventClientRepository).fetchEventClientsCore(eq(query), eq(params));
    }

    @Test
    public void testGetEventsByUniqueDateShouldExecuteCorrectQueryOnECRepository() {
        patientHistoryRepository.getEventsByUniqueDate("base_entity_id");
        String query = "SELECT json, SUBSTR(dateCreated, 1, 10) visit_date FROM event WHERE baseEntityId=? GROUP BY 2";
        String[] params = new String[]{"base_entity_id"};
        Mockito.verify(eventClientRepository).fetchEventClientsCore(eq(query), eq(params));
    }
}
