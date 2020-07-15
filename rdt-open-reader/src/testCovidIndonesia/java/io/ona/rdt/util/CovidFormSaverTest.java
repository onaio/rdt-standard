package io.ona.rdt.util;

import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;

import static io.ona.rdt.util.CovidConstants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.PATIENT_DIAGNOSTICS;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS;
import static io.ona.rdt.util.CovidConstants.Encounter.SUPPORT_INVESTIGATION;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;
import static io.ona.rdt.util.CovidConstants.Table.SUPPORT_INVESTIGATIONS;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidFormSaverTest extends FormSaverTest {

    @Override
    @Test
    public void testGetBindTypeShouldGetCorrectBindType() throws Exception {
        assertEquals(COVID_PATIENTS, Whitebox.invokeMethod(formSaver, "getBindType", COVID_PATIENT_REGISTRATION));
        assertEquals(COVID_RDT_TESTS, Whitebox.invokeMethod(formSaver, "getBindType", COVID_RDT_TEST));
        assertEquals(PATIENT_DIAGNOSTIC_RESULTS, Whitebox.invokeMethod(formSaver, "getBindType", PATIENT_DIAGNOSTICS));
        assertEquals(SAMPLE_COLLECTIONS, Whitebox.invokeMethod(formSaver, "getBindType", SAMPLE_COLLECTION));
        assertEquals(SAMPLE_DELIVERY_RECORDS, Whitebox.invokeMethod(formSaver, "getBindType", SAMPLE_DELIVERY_DETAILS));
        assertEquals(SUPPORT_INVESTIGATIONS, Whitebox.invokeMethod(formSaver, "getBindType", SUPPORT_INVESTIGATION));
    }

    protected FormSaver getFormSaver() {
        return new CovidFormSaver();
    }

    @Override
    protected String getEventType() {
        return COVID_PATIENT_REGISTRATION;
    }

    @Override
    protected String getBindType() {
        return COVID_PATIENTS;
    }

    @Override
    protected int getNumOfIDsToClose() {
        return 2;
    }

    @Override
    protected String getEncounterType() {
        return COVID_RDT_TEST;
    }

    @Override
    protected org.smartregister.domain.db.Event getDBEvent() {
        org.smartregister.domain.db.Event event = super.getDBEvent();
        org.smartregister.domain.db.Obs obs = new org.smartregister.domain.db.Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(COVID_SAMPLE_ID);
        obs.setFieldCode(COVID_SAMPLE_ID);
        event.addObs(obs);
        return event;
    }

    @Override
    protected Event getEvent() {
        Event event = super.getEvent();;
        Obs obs = new Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(COVID_SAMPLE_ID);
        event.getObs().add(obs);
        return event;
    }
}
