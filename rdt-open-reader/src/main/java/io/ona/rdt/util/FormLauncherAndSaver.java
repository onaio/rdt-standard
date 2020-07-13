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

    private final FormLauncher formLauncher = getFormLauncher();
    private final FormSaver formSaver = getFormSaver();

    public void saveForm(JSONObject jsonForm, OnFormSavedCallback callback) {
        formSaver.saveForm(jsonForm, callback);
    }

    public void launchForm(Activity activity, String formName, Patient patient) throws JSONException {
        formLauncher.launchForm(activity, formName, patient);
    }

    protected FormLauncher getFormLauncher() {
        return new FormLauncher();
    }

    protected FormSaver getFormSaver() {
        return new FormSaver();
    }
}
