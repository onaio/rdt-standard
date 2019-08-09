package io.ona.rdt_app.presenter;

import io.ona.rdt_app.contract.PatientRegisterActivityContract;

/**
 * Created by Vincent Karuri on 02/08/2019
 */
public class PatientRegisterActivityStub implements PatientRegisterActivityContract.View {

    private PatientRegisterFragmentPresenter fragmentPresenter;

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
