package io.ona.rdt.contract;

import io.ona.rdt.callback.OnFormSavedCallback;


/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View extends OnFormSavedCallback {
        void openDrawerLayout();

        void closeDrawerLayout();
    }

    interface Presenter {

        void saveForm(String jsonForm, OnFormSavedCallback callback);
    }
}
