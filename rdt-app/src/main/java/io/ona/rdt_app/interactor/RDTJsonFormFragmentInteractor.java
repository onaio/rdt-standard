package io.ona.rdt_app.interactor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vincent Karuri on 05/08/2019
 */
public class RDTJsonFormFragmentInteractor {

    public void saveForm(JSONObject jsonForm) throws JSONException {
        PatientRegisterFragmentInteractor interactor = new PatientRegisterFragmentInteractor();
        interactor.saveForm(jsonForm, null);
    }
}
