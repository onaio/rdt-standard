package io.ona.rdt_app.contract;

import org.json.JSONException;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View {
        void openDrawerLayout();

        void closeDrawerLayout();
    }

    interface Presenter {
        void saveForm(String jsonForm) throws JSONException;
    }
}
