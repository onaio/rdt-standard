package io.ona.rdt.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.powermock.api.mockito.PowerMockito;
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
import static io.ona.rdt.util.CovidConstants.FormFields.LAST_KNOWN_LOCATION;
import static io.ona.rdt.util.CovidConstants.FormFields.LAT;
import static io.ona.rdt.util.CovidConstants.FormFields.LNG;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;
import static io.ona.rdt.util.CovidConstants.Table.SUPPORT_INVESTIGATIONS;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidFormSaverTest extends BaseFormSaverTest {


    @Test
    public void testGetBindTypeShouldGetCorrectBindType() throws Exception {
        assertEquals(COVID_PATIENTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", COVID_PATIENT_REGISTRATION));
        assertEquals(COVID_RDT_TESTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", COVID_RDT_TEST));
        assertEquals(PATIENT_DIAGNOSTIC_RESULTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", PATIENT_DIAGNOSTICS));
        assertEquals(SAMPLE_COLLECTIONS, Whitebox.invokeMethod(getFormSaver(), "getBindType", SAMPLE_COLLECTION));
        assertEquals(SAMPLE_DELIVERY_RECORDS, Whitebox.invokeMethod(getFormSaver(), "getBindType", SAMPLE_DELIVERY_DETAILS));
        assertEquals(SUPPORT_INVESTIGATIONS, Whitebox.invokeMethod(getFormSaver(), "getBindType", SUPPORT_INVESTIGATION));
    }

    @Override
    protected FormSaver getFormSaver() {
        return new CovidFormSaver();
    }

    @Override
    protected String getPatientRegistrationEventType() {
        return COVID_PATIENT_REGISTRATION;
    }

    @Override
    protected String getRDTPatientBindType() {
        return COVID_PATIENTS;
    }

    @Override
    protected int getNumOfIDsToClose() {
        return 2;
    }

    @Override
    protected String getRDTTestEncounterType() {
        return COVID_RDT_TEST;
    }

    @Override
    protected org.smartregister.domain.Event getDBEvent() {
        org.smartregister.domain.Event event = super.getDBEvent();
        org.smartregister.domain.Obs obs = new org.smartregister.domain.Obs();
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

    protected void mockStaticMethods() throws JSONException {
        super.mockStaticMethods();

        PowerMockito.when(drishtiContext.allSettings()).thenReturn(settings);
        JSONObject coordinates = new JSONObject();
        coordinates.put(LNG, "1");
        coordinates.put(LAT, "2");
        doReturn(coordinates.toString()).when(settings).get(eq(LAST_KNOWN_LOCATION));
    }
}
