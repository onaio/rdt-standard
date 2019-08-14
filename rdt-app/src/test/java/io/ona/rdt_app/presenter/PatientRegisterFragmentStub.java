package io.ona.rdt_app.presenter;

import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import io.ona.rdt_app.contract.RDTJsonFormFragmentContract;

import static io.ona.rdt_app.interactor.PatientRegisterFragmentInteractorTest.PATIENT_REGISTRATION_JSON_FORM;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
public class PatientRegisterFragmentStub extends JsonFormFragment implements RDTJsonFormFragmentContract.View {

    @Override
    public void setNextButtonState(View rootView, boolean buttonEnabled) {

    }

    @Override
    public void moveToNextStep() {

    }

    @Override
    public void saveForm() {

    }

    @Override
    public void transactFragment(JsonFormFragment nextFragment) {

    }

    @Override
    public String getRDTType() {
        return null;
    }

    @Override
    public JsonApi getJsonApi() {
        return new JsonApiStub();
    }

    @Override
    public String getCurrentJsonState() {
        return PATIENT_REGISTRATION_JSON_FORM ;
    }
}
