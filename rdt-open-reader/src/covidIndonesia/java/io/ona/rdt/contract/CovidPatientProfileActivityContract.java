package io.ona.rdt.contract;

import io.ona.rdt.callback.OnFormSavedCallback;


/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface CovidPatientProfileActivityContract {
    interface View extends OnFormSavedCallback { }

    interface Presenter {
        void saveForm(String jsonForm, OnFormSavedCallback callback);
    }
}
