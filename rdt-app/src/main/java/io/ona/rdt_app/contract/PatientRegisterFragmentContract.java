package io.ona.rdt_app.contract;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt_app.callback.OnFormSavedCallback;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter {
        String getMainCondition();
    }

    interface View {
        void initializeAdapter();
    }
}


