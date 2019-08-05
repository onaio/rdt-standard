package io.ona.rdt_app.contract;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.presenter.PatientRegisterFragmentPresenter;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public interface PatientRegisterActivityContract {
    interface View extends OnFormSavedCallback {
        void openDrawerLayout();

        void closeDrawerLayout();

        PatientRegisterFragmentPresenter getRegisterFragmentPresenter();
    }

    interface Presenter {
        void saveForm(String jsonForm) throws JSONException;
    }
}
