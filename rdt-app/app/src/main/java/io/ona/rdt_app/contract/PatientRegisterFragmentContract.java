package io.ona.rdt_app.contract;

import org.json.JSONException;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter {
        void saveForm(String jsonForm) throws JSONException;

        String getMainCondition();
    }

    interface View {
        void initializeAdapter();
    }
}


