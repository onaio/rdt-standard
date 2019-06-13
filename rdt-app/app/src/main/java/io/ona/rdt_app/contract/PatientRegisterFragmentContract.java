package io.ona.rdt_app.contract;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter {
        void saveForm(String jsonForm);
    }
}
