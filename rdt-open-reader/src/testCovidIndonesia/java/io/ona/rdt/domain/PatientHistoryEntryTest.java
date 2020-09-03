package io.ona.rdt.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PatientHistoryEntryTest {

    private static final String PATIENT_KEY = "myKey";
    private static final String PATIENT_VAL = "myVal";

    private PatientHistoryEntry patientHistoryEntry;

    @Before
    public void setUp() {
        patientHistoryEntry = new PatientHistoryEntry(PATIENT_KEY, PATIENT_VAL);
    }

    @After
    public void tearDown() {
        patientHistoryEntry = null;
    }

    @Test
    public void getKeyShouldReturnValidKey() {
        Assert.assertEquals(PATIENT_KEY, patientHistoryEntry.getKey());
    }

    @Test
    public void setKeyShouldVerify() {
        String newKey = "newKey";
        patientHistoryEntry.setKey(newKey);
        Assert.assertEquals(newKey, patientHistoryEntry.getKey());
    }

    @Test
    public void getValueShouldReturnValidValue() {
        Assert.assertEquals(PATIENT_VAL, patientHistoryEntry.getValue());
    }

    @Test
    public void setValueShouldVerify() {
        String newVal = "newVal";
        patientHistoryEntry.setValue(newVal);
        Assert.assertEquals(newVal, patientHistoryEntry.getValue());
    }
}
