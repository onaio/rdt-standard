package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt.activity.PatientRegisterActivity;
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

    private PatientRegisterActivityContract.View activity;
    protected PatientRegisterActivityInteractor interactor;
    private RDTJsonFormUtils formUtils;

    public PatientRegisterActivityPresenter(PatientRegisterActivityContract.View activity) {
        this.activity = activity;
        interactor = getInteractor();
        formUtils = getFormUtils();
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
            formUtils.appendEntityId(jsonFormObj);
            interactor.saveForm(jsonFormObj, callback);
            Patient patient = interactor.getPatientForRDT(jsonFormObj);
            if (patient != null) {
                launchPostRegistrationView(patient);
            }
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    protected void launchPostRegistrationView(Patient patient) throws JSONException {
        interactor.launchForm((Activity) activity, RDT_TEST_FORM, patient);
    }

    @Override
    public void launchForm(Activity activity, String formName, Patient patient) {
        try {
            interactor.launchForm(activity, formName, patient);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    protected PatientRegisterActivityInteractor getInteractor() {
        if (interactor == null) {
            interactor = initializeInteractor();
        }
        return interactor;
    }

    protected PatientRegisterActivityInteractor initializeInteractor() {
        return new PatientRegisterActivityInteractor();
    }

    protected PatientRegisterActivity getActivity() {
        return (PatientRegisterActivity) activity;
    }

    public RDTJsonFormUtils getFormUtils() {
        if (formUtils == null) {
            formUtils = initializeFormUtils();
        }
        return formUtils;
    }

    protected RDTJsonFormUtils initializeFormUtils() {
        return new RDTJsonFormUtils();
    }
}
