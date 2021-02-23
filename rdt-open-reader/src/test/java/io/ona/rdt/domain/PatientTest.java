package io.ona.rdt.domain;

import android.os.Parcel;

import org.junit.Assert;
import org.junit.Test;

import io.ona.rdt.robolectric.RobolectricTest;

import static org.junit.Assert.assertEquals;

/**
 * Created by Vincent Karuri on 20/11/2019
 */
public class PatientTest extends RobolectricTest {

    private static final int AGE = 10;

    private Patient patient;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        patient = new Patient("name", "sex", "entity_id");
    }

    @Test
    public void testConstructor() {
        assertEquals("name", patient.getPatientName());
        assertEquals("sex", patient.getPatientSex());
        assertEquals("entity_id", patient.getBaseEntityId());
    }

    @Test
    public void testSetters() {

        assertEquals("name", patient.getPatientName());
        assertEquals("sex", patient.getPatientSex());
        assertEquals("entity_id", patient.getBaseEntityId());

        patient.setBaseEntityId("entity_id_2");
        patient.setPatientSex("sex_2");
        patient.setPatientName("name_2");
        patient.setPatientId("patient_id");
        patient.setAge(AGE);
        patient.setDob("1989-11-08");
        patient.setFemaleTranslatedSex("female");
        assertEquals("name_2", patient.getPatientName());
        assertEquals("sex_2", patient.getPatientSex());
        assertEquals("entity_id_2", patient.getBaseEntityId());
        assertEquals("patient_id", patient.getPatientId());
        assertEquals(AGE, patient.getAge());
        assertEquals("1989-11-08", patient.getDob());
        assertEquals("female", patient.getFemaleTranslatedSex());
    }

    @Test
    public void testParcelable() {
        Parcel parcel = Parcel.obtain();
        patient.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);
        Patient testPatient = Patient.CREATOR.createFromParcel(parcel);
        Assert.assertEquals(patient.getPatientName(), testPatient.getPatientName());
        Assert.assertEquals(patient.getPatientSex(), testPatient.getPatientSex());
        Assert.assertEquals(patient.getBaseEntityId(), testPatient.getBaseEntityId());
    }
}
