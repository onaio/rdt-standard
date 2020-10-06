package io.ona.rdt.robolectric.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.clientandeventmodel.Obs;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.EventClientRepository;
import org.smartregister.sync.ClientProcessorForJava;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.ona.rdt.TestUtils;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.presenter.RDTApplicationPresenter;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormSaver;
import io.ona.rdt.util.StepStateConfig;

import static io.ona.rdt.util.Constants.FormFields.ENCOUNTER_TYPE;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Vincent Karuri on 16/07/2020
 */
public abstract class BaseFormSaverTest extends RobolectricTest {

    protected final String UNIQUE_ID = "unique_id";

    private RDTApplication rdtApplication = RDTApplication.getInstance();

    @Mock
    protected AllSettings settings;
    @Mock
    private StepStateConfig stepStateConfig;


    public static final String PATIENT_NAME = "Mr. Patient";
    public static final String PATIENT_GENDER = "Male";
    public static final String PATIENT_BASE_ENTITY_ID = "2b66831d";
    public static final String PATIENT_ID = "patient id";
    public static final int PATIENT_AGE = 20;

    private Map<String, String > properties = new HashMap();

    public static final Patient expectedPatient = new Patient(PATIENT_NAME, PATIENT_GENDER,
            PATIENT_BASE_ENTITY_ID, PATIENT_ID);

    protected static JSONArray formFields;
    protected static JSONObject formJsonObj;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockStaticMethods();
        formJsonObj = new JSONObject(TestUtils.getPatientRegistrationJsonForm(getPatientRegistrationEventType()));
        formFields = getFormFields(formJsonObj);
    }

    @Test
    public void testSaveEventClientShouldSaveEventAndClient() throws Exception {
        Whitebox.invokeMethod(getFormSaver(), "saveEventClient", formJsonObj,
                getPatientRegistrationEventType(), getRDTPatientBindType());
        EventClientRepository eventClientRepository = RDTApplication.getInstance().getContext()
                .getEventClientRepository();
        verify(eventClientRepository).addorUpdateClient(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
        verify(eventClientRepository).addEvent(eq(PATIENT_BASE_ENTITY_ID), any(JSONObject.class));
    }

    @Test
    public void testCloseRDTIdShouldCloseRDTId() throws Exception {
        Whitebox.invokeMethod(getFormSaver(), "closeIDs", getDBEvent());
        verifyIDsAreClosed(getNumOfIDsToClose());
    }

    @Test
    public void testProcessAndSaveFormShouldProcessClient() throws Exception {
        processFormAndVerifyIDsAreClosed();
        verify(ClientProcessorForJava.getInstance(mock(Context.class)),
                times(1)).processClient(any(List.class));
    }

    private void processFormAndVerifyIDsAreClosed() throws Exception {
        formJsonObj.put(ENCOUNTER_TYPE, getRDTTestEncounterType());
        getFormSaver().saveForm(formJsonObj, mock(OnFormSavedCallback.class));
        verifyIDsAreClosed(getNumOfIDsToClose());
    }

    protected void mockStaticMethods() throws JSONException {
        RDTApplicationPresenter applicationPresenter = mock(RDTApplicationPresenter.class);

        properties = new HashMap();
        properties.put("property1", "property1");
        properties.put("property2", "property2");
        properties.put("property3", "property3");
        doReturn(properties).when(applicationPresenter).getPhoneProperties();

        ReflectionHelpers.setField(rdtApplication, "presenter", applicationPresenter);

        JSONObject jsonObject = Mockito.mock(JSONObject.class);
        Mockito.doReturn(RDT_ID).when(jsonObject).optString(eq(RDT_ID_KEY));
        Mockito.doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
    }

    private static JSONArray getFormFields(JSONObject formJsonObj) throws JSONException {
        return formJsonObj.getJSONObject("step1").getJSONArray("fields");
    }

    private void verifyIDsAreClosed(int times) {
        verify(RDTApplication.getInstance().getContext().getUniqueIdRepository(),
                times(times)).close(eq(UNIQUE_ID));
    }

    protected org.smartregister.domain.Event getDBEvent() {
        org.smartregister.domain.Event dbEvent = new org.smartregister.domain.Event();
        org.smartregister.domain.Obs obs = new org.smartregister.domain.Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(RDT_ID);
        dbEvent.addObs(obs);
        return dbEvent;
    }

    protected Event getEvent() {
        Event event = new Event();
        event.setObs(new ArrayList<>());
        Obs obs = new Obs();
        obs.setValue(UNIQUE_ID);
        obs.setFieldCode(RDT_ID);
        event.getObs().add(obs);
        return event;
    }


    protected abstract String getPatientRegistrationEventType();

    protected abstract FormSaver getFormSaver();

    protected abstract String getRDTPatientBindType();

    protected abstract int getNumOfIDsToClose();

    protected abstract String getRDTTestEncounterType();
}
