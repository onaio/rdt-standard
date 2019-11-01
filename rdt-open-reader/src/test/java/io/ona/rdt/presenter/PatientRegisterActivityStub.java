package io.ona.rdt.presenter;

import android.app.Activity;

import io.ona.rdt.contract.PatientRegisterActivityContract;

/**
 * Created by Vincent Karuri on 02/08/2019
 */
public class PatientRegisterActivityStub extends Activity implements PatientRegisterActivityContract.View {

    @Override
    public void openDrawerLayout() {
        // do nothing
    }

    @Override
    public void closeDrawerLayout() {
        // do nothing
    }

    @Override
    public void onFormSaved() {
        // do nothing
    }
}
