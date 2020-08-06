package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.domain.Patient;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class FormLauncherAndSaver {

    protected FormSaver formSaver = getFormSaver();
    protected FormLauncher formLauncher = getFormLauncher();

    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        formSaver.saveForm(jsonForm, callback);
    }

    public void launchForm(Activity activity, String formName, Patient patient) throws JSONException {
        formLauncher.launchForm(activity, formName, patient);
    }

    protected FormLauncher getFormLauncher() {
        if (formLauncher == null) {
            formLauncher = createFormLauncher();
        }
        return formLauncher;
    }

    protected FormLauncher createFormLauncher() {
        return new FormLauncher();
    }

    protected FormSaver getFormSaver() {
        if (formSaver == null) {
           formSaver = createFormSaver();
        }
        return formSaver;
    }

    protected FormSaver createFormSaver() {
        return new FormSaver();
    }
}
