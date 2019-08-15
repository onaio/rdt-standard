package io.ona.rdt_app.contract;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterFragmentContract;

import io.ona.rdt_app.callback.OnFormSavedCallback;
import io.ona.rdt_app.model.Patient;

/**
 * Created by Vincent Karuri on 13/06/2019
 */
public interface PatientRegisterFragmentContract {

    interface Presenter extends BaseRegisterFragmentContract.Presenter {
        String getMainCondition();

        void launchForm(Activity activity, String formName, Patient patient) throws JSONException;
    }

    interface View extends BaseRegisterFragmentContract.View {
        void initializeAdapter();
    }
}


