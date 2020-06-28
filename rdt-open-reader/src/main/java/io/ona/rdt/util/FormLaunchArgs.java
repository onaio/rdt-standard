package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONObject;

import io.ona.rdt.domain.Patient;

/**
 * Created by Vincent Karuri on 01/08/2019
 */
public class FormLaunchArgs {
    private Activity activity;
    private Patient patient;
    private JSONObject formJsonObject;
    private String formName;

    public FormLaunchArgs withActivity(Activity activity) {
        this.activity = activity;
        return this;
    }

    public FormLaunchArgs withPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public FormLaunchArgs withFormJsonObj(JSONObject formJsonObj) {
        this.formJsonObject = formJsonObj;
        return this;
    }

    public FormLaunchArgs withFormName(String formName) {
        this.formName = formName;
        return this;
    }

    public Activity getActivity() {
        return activity;
    }

    public Patient getPatient() {
        return patient;
    }

    public JSONObject getFormJsonObject() {
        return formJsonObject;
    }

    public String getFormName() {
        return formName;
    }
}
