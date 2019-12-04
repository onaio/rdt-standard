package io.ona.rdt.domain;

/**
 * Created by Vincent Karuri on 08/07/2019
 */
public class Patient {

    private String patientName;
    private String patientSex;
    private String baseEntityId;
    private String patientId;

    public Patient(String patientName, String patientSex, String baseEntityId) {
        this.patientName = patientName;
        this.patientSex = patientSex;
        this.baseEntityId = baseEntityId;
    }

    public Patient(String patientName, String patientSex, String baseEntityId, String patientId) {
        this(patientName, patientSex, baseEntityId);
        setPatientId(patientId);
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getPatientSex() {
        return patientSex;
    }

    public void setPatientSex(String patientSex) {
        this.patientSex = patientSex;
    }

    public String getBaseEntityId() {
        return baseEntityId;
    }

    public void setBaseEntityId(String baseEntityId) {
        this.baseEntityId = baseEntityId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
