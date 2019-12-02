package io.ona.rdt.presenter;

import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import io.ona.rdt.contract.RDTJsonFormFragmentContract;
import io.ona.rdt.stub.JsonApiStub;

import static io.ona.rdt.interactor.PatientRegisterFragmentInteractorTest.PATIENT_REGISTRATION_JSON_FORM;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
public class PatientRegisterFragmentStub extends JsonFormFragment implements RDTJsonFormFragmentContract.View {

    @Override
    public void setNextButtonState(View rootView, boolean buttonEnabled) {
        // do nothing
    }

    @Override
    public void moveToNextStep() {
        // do nothing
    }

    @Override
    public void saveForm() {
        // do nothing
    }

    @Override
    public void transactFragment(JsonFormFragment nextFragment) {
        // do nothing
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
