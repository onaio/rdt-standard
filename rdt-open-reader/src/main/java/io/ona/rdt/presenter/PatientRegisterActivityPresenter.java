package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

import io.ona.rdt.callback.OnFormSavedCallback;
import io.ona.rdt.contract.PatientRegisterActivityContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientRegisterActivityInteractor;
import io.ona.rdt.util.RDTJsonFormUtils;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class PatientRegisterActivityPresenter implements PatientRegisterActivityContract.Presenter {

    private final String TAG = PatientRegisterActivityPresenter.class.getName();

    private PatientRegisterActivityContract.View activity;
    private PatientRegisterActivityInteractor interactor;

    public PatientRegisterActivityPresenter(PatientRegisterActivityContract.View activity) {
        this.activity = activity;
        interactor = new PatientRegisterActivityInteractor();
    }

    @Override
    public void registerViewConfigurations(List<String> list) {
        // TODO: implement this
    }

    @Override
    public void unregisterViewConfiguration(List<String> list) {
        // TODO: implement this
    }

    @Override
    public void onDestroy(boolean b) {
        // TODO: implement this
    }

    @Override
    public void updateInitials() {
        // TODO: implement this
    }

    @Override
    public void saveForm(String jsonForm, OnFormSavedCallback callback) {
        try {
            JSONObject jsonFormObj = new JSONObject(jsonForm);
            RDTJsonFormUtils.appendEntityId(jsonFormObj);
            interactor.saveForm(jsonFormObj, callback);
            Patient patient = interactor.getPatientForRDT(jsonFormObj);
            if (patient != null) {
                interactor.launchForm((Activity) activity, RDT_TEST_FORM, patient);
            }
        } catch (JSONException e) {
            Timber.e(TAG, e);
        }
    }

    @Override
    public void launchForm(Activity activity, String formName, Patient patient) {
        try {
            interactor.launchForm(activity, formName, patient);
        } catch (JSONException e) {
            Timber.e(TAG, e);
        }
    }
}
