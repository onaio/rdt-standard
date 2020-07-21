package io.ona.rdt.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;

import java.util.Calendar;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt.util.Constants.Encounter.PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Encounter.PCR_RESULT;
import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.Constants.Table.PCR_RESULTS;
import static io.ona.rdt.util.Constants.Table.RDT_PATIENTS;
import static io.ona.rdt.util.Constants.Table.RDT_TESTS;
import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class FormSaverTest extends BaseFormSaverTest {

    @Test
    public void testPopulateApproxDOBShouldPopulateCorrectDate() throws Exception {
        Whitebox.invokeMethod(getFormSaver(), "populateApproxDOB", formFields);
        for (int i = 0; i < formFields.length(); i++) {
            JSONObject field = formFields.getJSONObject(i);
            if (Constants.FormFields.DOB.equals(field.get(KEY))) {
                Calendar today = Calendar.getInstance();
                int year = today.get(Calendar.YEAR) - PATIENT_AGE;
                assertEquals(StringUtils.join(new int[]{year, today.get(Calendar.MONTH),
                        today.get(Calendar.DAY_OF_MONTH)}, '-'), field.get(VALUE));
            }
        }
    }

    @Test
    public void testPopulatePhoneMetadataShouldPopulateCorrectMetadata() throws Exception {
        Event event = new Event();
        Whitebox.invokeMethod(getFormSaver(), "populatePhoneMetadata", event);
        for (Obs obs : event.getObs()) {
            assertEquals(obs.getFieldCode(), obs.getValue());
        }
    }

    @Test
    public void testGetBindTypeShouldGetCorrectBindType() throws Exception {
        assertEquals(RDT_PATIENTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", PATIENT_REGISTRATION));
        assertEquals(RDT_TESTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", RDT_TEST));
        assertEquals(PCR_RESULTS, Whitebox.invokeMethod(getFormSaver(), "getBindType", PCR_RESULT));
    }

    @Override
    protected FormSaver getFormSaver() {
        return new FormSaver();
    }

    @Override
    protected String getPatientRegistrationEventType() {
        return PATIENT_REGISTRATION;
    }

    @Override
    protected String getRDTPatientBindType() {
        return RDT_PATIENTS;
    }

    @Override
    protected int getNumOfIDsToClose() {
        return 1;
    }

    @Override
    protected String getRDTTestEncounterType() {
        return RDT_TEST;
    }
}
