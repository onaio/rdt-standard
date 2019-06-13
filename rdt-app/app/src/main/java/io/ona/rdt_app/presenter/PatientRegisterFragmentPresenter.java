package io.ona.rdt_app.presenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import io.ona.rdt_app.contract.PatientRegisterFragmentContract;
import io.ona.rdt_app.interactor.PatientRegisterFragmentInteractor;

/**
 * Created by Vincent Karuri on 11/06/2019
 */
public class PatientRegisterFragmentPresenter implements BaseRegisterFragmentContract.Presenter, PatientRegisterFragmentContract.Presenter {

    private PatientRegisterFragmentInteractor interactor = new PatientRegisterFragmentInteractor();

    @Override
    public void processViewConfigurations() {

    }

    @Override
    public void initializeQueries(String mainCondition) {

    }

    @Override
    public void startSync() {

    }

    @Override
    public void searchGlobally(String uniqueId) {

    }

    @Override
    public void saveForm(String jsonForm) throws JSONException {
        interactor.saveRegistrationForm(new JSONObject(jsonForm));
    }
}
