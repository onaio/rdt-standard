package io.ona.rdt.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Vincent Karuri on 08/07/2019
 */
public class Patient implements Parcelable {

    private String patientName;
    private String patientSex;
    private String baseEntityId;
    private String patientId;
    private int age;
    private String dob;

    public Patient(String patientName, String patientSex, String baseEntityId) {
        this.patientName = patientName;
        this.patientSex = patientSex;
        this.baseEntityId = baseEntityId;
    }

    public Patient(String patientName, String patientSex, String baseEntityId, String patientId) {
        this(patientName, patientSex, baseEntityId);
        setPatientId(patientId);
    }

    public Patient(String patientName, String patientSex, String baseEntityId, String patientId, int age, String dob) {
        this(patientName, patientSex, baseEntityId, patientId);
        setAge(age);
        setDob(dob);
    }

    protected Patient(Parcel in) {
        patientName = in.readString();
        patientSex = in.readString();
        baseEntityId = in.readString();
        patientId = in.readString();
        age = in.readInt();
        dob = in.readString();
    }

    public static final Creator<Patient> CREATOR = new Creator<Patient>() {
        @Override
        public Patient createFromParcel(Parcel in) {
            return new Patient(in);
        }

        @Override
        public Patient[] newArray(int size) {
            return new Patient[size];
        }
    };

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.patientName);
        dest.writeString(this.patientSex);
        dest.writeString(this.baseEntityId);
        dest.writeString(this.patientSex);
        dest.writeInt(this.age);
        dest.writeString(this.dob);
    }
}
