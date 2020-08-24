package io.ona.rdt.robolectric;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.smartregister.repository.EventClientRepository;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.repository.PatientHistoryRepository;

/**
 * Created by Vincent Karuri on 21/08/2020
 */
public class PatientHistoryRepositoryTest extends RobolectricTest {

    private final String BASE_ENTITY_ID = "base_entity_id";
    private final String EVENT_TYPE = "event_type";
    private final String DATE = "date";

    private PatientHistoryRepository patientHistoryRepository;
    private EventClientRepository eventClientRepository;

    @Before
    public void setUp() {
        patientHistoryRepository = new PatientHistoryRepository();
        eventClientRepository =  RDTApplication.getInstance().getContext().getEventClientRepository();
    }

    @Test
    public void testGetEventsByEventTypeShouldExecuteCorrectQueryOnECRepository() {
        patientHistoryRepository.getEvent(BASE_ENTITY_ID, EVENT_TYPE, DATE);
        String query = "SELECT json, SUBSTR(dateCreated, 1, 10) AS visit_date FROM event WHERE baseEntityId=? AND eventType=? AND visit_date=?";
        String[] params = new String[]{BASE_ENTITY_ID, EVENT_TYPE, DATE};
        Mockito.verify(eventClientRepository).fetchEventClientsCore(ArgumentMatchers.eq(query), ArgumentMatchers.eq(params));
    }

    @Test
    public void testGetEventsByUniqueDateShouldExecuteCorrectQueryOnECRepository() {
        patientHistoryRepository.getEventsByUniqueDate(BASE_ENTITY_ID);
        String query = "SELECT json, SUBSTR(dateCreated, 1, 10) visit_date FROM event WHERE baseEntityId=? GROUP BY 2";
        String[] params = new String[]{BASE_ENTITY_ID};
        Mockito.verify(eventClientRepository).fetchEventClientsCore(ArgumentMatchers.eq(query), ArgumentMatchers.eq(params));
    }
}
