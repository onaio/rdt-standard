package io.ona.rdt.domain;

import java.util.List;

/**
 * Created by Vincent Karuri on 16/01/2020
 */
public class RDTTestDetails {

    private String rdtId;
    private String date;
    private String rdtType;
    private String testResult;
    private List<String> parasiteTypes;

    public String getRdtId() {
        return rdtId;
    }

    public void setRdtId(String rdtId) {
        this.rdtId = rdtId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRdtType() {
        return rdtType;
    }

    public void setRdtType(String rdtType) {
        this.rdtType = rdtType;
    }

    public List<String> getParasiteTypes() {
        return parasiteTypes;
    }

    public void setParasiteTypes(List<String> parasiteTypes) {
        this.parasiteTypes = parasiteTypes;
    }

    public String getTestResult() {
        return testResult;
    }

    public void setTestResult(String testResult) {
        this.testResult = testResult;
    }
}
