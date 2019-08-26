package io.ona.rdt_app.contract;

import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONException;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public interface RDTJsonFormFragmentContract {

    interface View {
        void setNextButtonState(android.view.View rootView, boolean buttonEnabled);

        void moveToNextStep();

        void saveForm();

        void transactFragment(JsonFormFragment nextFragment);

        String getRDTType();
    }

    interface Presenter{
        void saveForm() throws JSONException;

        void performNextButtonAction(String currentStep, Object isSubmit);
    }
}
