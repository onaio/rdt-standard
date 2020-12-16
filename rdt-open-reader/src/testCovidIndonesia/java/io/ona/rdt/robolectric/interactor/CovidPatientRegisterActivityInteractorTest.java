package io.ona.rdt.robolectric.interactor;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.CovidPatientRegisterActivityInteractor;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

public class CovidPatientRegisterActivityInteractorTest extends RobolectricTest {

    private static final String PATIENT_FORM = "{\"entity_id\":\"3sldfkl3-2-sf-3-sdf\",\"step1\":{\"fields\":[{\"key\":\"patient_first_name\",\"value\":\"New\"},{\"key\":\"patient_last_name\",\"value\":\"Moon\"},{\"key\":\"sex\",\"value\":\"Male\"},{\"key\":\"age\",\"value\":\"31\"},{\"key\":\"patient_dob\",\"value\":\"16-12-2020\"},{\"key\":\"drivers_license_number\",\"value\":\"242424234\"}]}}";
    private CovidPatientRegisterActivityInteractor interactor;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new CovidPatientRegisterActivityInteractor();
    }

    @Test
    public void testCreateFormLauncher() throws Exception {
        FormLauncher formLauncher = Whitebox.invokeMethod(interactor, "createFormLauncher");
        Assert.assertEquals(CovidFormLauncher.class.getName(), formLauncher.getClass().getName());
    }

    @Test
    public void testCreateFormSaver() throws Exception {
        FormSaver formSaver = Whitebox.invokeMethod(interactor, "createFormSaver");
        Assert.assertEquals(CovidFormSaver.class.getName(), formSaver.getClass().getName());
    }

    @Test
    public void testGetPatientForRDT() throws JSONException {
        int age = 31;
        Patient patient = interactor.getPatientForRDT(new JSONObject(PATIENT_FORM));

        Assert.assertNotNull(patient);
        Assert.assertEquals("New Moon", patient.getPatientName());
        Assert.assertEquals("Male", patient.getPatientSex());
        Assert.assertEquals("3sldfkl3-2-sf-3-sdf", patient.getBaseEntityId());
        Assert.assertEquals("242424234", patient.getPatientId());
        Assert.assertEquals(age, patient.getAge());
        Assert.assertEquals("16-12-2020", patient.getDob());
    }
}
