package io.ona.rdt.presenter;

import android.view.View;

import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.JsonApi;

import io.ona.rdt.TestUtils;
import io.ona.rdt.contract.RDTJsonFormFragmentContract;

import static org.mockito.Mockito.mock;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
public class PatientRegisterFragmentStub extends JsonFormFragment implements RDTJsonFormFragmentContract.View {

    @Override
    public void setNextButtonState(View rootView, boolean buttonEnabled) {
        // do nothing
    }

    @Override
    public void navigateToNextStep() {
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
        return mock(JsonApi.class);
    }

    @Override
    public String getCurrentJsonState() {
        return TestUtils.PATIENT_REGISTRATION_JSON_FORM ;
    }
}
