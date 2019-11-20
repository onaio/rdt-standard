package io.ona.rdt.domain;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
public class PatientTest {

    private Patient patient;

    @Before
    public void setUp() {
        patient = new Patient("name", "sex", "entity_id");
    }

    @Test
    public void testConstructor() {
        assertEquals("name", patient.getPatientName());
        assertEquals("sex", patient.getPatientSex());
        assertEquals("entity_id", patient.getBaseEntityId());
    }
}
