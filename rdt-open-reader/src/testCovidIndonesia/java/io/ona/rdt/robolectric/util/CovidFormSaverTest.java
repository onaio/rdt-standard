package io.ona.rdt.robolectric.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormSaver;
import io.ona.rdt.widget.CovidRDTBarcodeFactory;

import static io.ona.rdt.util.CovidConstants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.PATIENT_DIAGNOSTICS;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.LAST_KNOWN_LOCATION;
import static io.ona.rdt.util.CovidConstants.FormFields.LAT;
import static io.ona.rdt.util.CovidConstants.FormFields.LNG;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;
import static org.mockito.ArgumentMatchers.eq;
import static org.powermock.api.mockito.PowerMockito.doReturn;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidFormSaverTest extends BaseFormSaverTest {

    @Test
    public void testGetBindTypeShouldGetCorrectBindType() throws Exception {
        String getBindType = "getBindType";
        Assert.assertEquals(COVID_PATIENTS, Whitebox.invokeMethod(getFormSaver(), getBindType, COVID_PATIENT_REGISTRATION));
        Assert.assertEquals(COVID_RDT_TESTS, Whitebox.invokeMethod(getFormSaver(), getBindType, COVID_RDT_TEST));
        Assert.assertEquals(PATIENT_DIAGNOSTIC_RESULTS, Whitebox.invokeMethod(getFormSaver(), getBindType, PATIENT_DIAGNOSTICS));
        Assert.assertEquals(SAMPLE_COLLECTIONS, Whitebox.invokeMethod(getFormSaver(), getBindType, SAMPLE_COLLECTION));
        Assert.assertEquals(SAMPLE_DELIVERY_RECORDS, Whitebox.invokeMethod(getFormSaver(), getBindType, SAMPLE_DELIVERY_DETAILS));
        Assert.assertEquals(CovidConstants.Table.COVID_WBC_RECORDS, Whitebox.invokeMethod(getFormSaver(), getBindType, CovidConstants.Encounter.COVID_WBC));
        Assert.assertEquals(CovidConstants.Table.COVID_XRAY_RECORDS, Whitebox.invokeMethod(getFormSaver(), getBindType, CovidConstants.Encounter.COVID_XRAY));
    }

    @Test
    public void testProcessAndSaveFormShouldCloseBatchIdForDeliveryDetailsForm() throws Exception {
        final String valBatchID = "test-batch-id";
        final String sampleDeliveryDetailsForm = "{\n" +
                "   \"count\":\"2\",\n" +
                "   \"encounter_type\":\"sample_delivery_details\",\n" +
                "   \"metadata\":{},\n" +
                "   \"step1\":{\n" +
                "      \"fields\":[]\n" +
                "   },\n" +
                "   \"step2\":{\n" +
                "      \"fields\":[\n" +
                "         {\n" +
                "            \"key\":\"" + CovidRDTBarcodeFactory.BATCH_ID + "\",\n" +
                "            \"type\":\"hidden\",\n" +
                "            \"value\":\"" + valBatchID + "\"\n" +
                "         }\n" +
                "      ]\n" +
                "   }\n" +
                "}";

        FormSaver formSaver = getFormSaver();
        JSONObject formJson = new JSONObject(sampleDeliveryDetailsForm);
        formJson.put(Constants.FormFields.ENCOUNTER_TYPE, SAMPLE_DELIVERY_DETAILS);
        Whitebox.invokeMethod(formSaver, "processAndSaveForm", formJson);

        Mockito.verify(RDTApplication.getInstance().getContext().getUniqueIdRepository(),
                Mockito.times(1)).close(eq(valBatchID));
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
        Event event = super.getEvent();
        Obs obs = new Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(COVID_SAMPLE_ID);
        event.getObs().add(obs);
        return event;
    }

    protected void mockStaticMethods() throws JSONException {
        JSONObject coordinates = new JSONObject();
        coordinates.put(LNG, "1");
        coordinates.put(LAT, "2");
        doReturn(coordinates.toString()).when(settings).get(eq(LAST_KNOWN_LOCATION));
    }
}
