package io.ona.rdt_app.contract;

import org.json.JSONException;

import io.ona.rdt_app.activity.PatientRegisterActivity;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View {
        void openDrawerLayout();

        void closeDrawerLayout();

        PatientRegisterFragmentPresenter getRegisterFragmentPresenter();

        PatientRegisterActivity getView();
    }

    interface Presenter {
        void saveForm(String jsonForm) throws JSONException;
    }
}
