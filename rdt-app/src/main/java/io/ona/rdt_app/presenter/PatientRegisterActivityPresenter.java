package io.ona.rdt_app.presenter;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.view.contract.BaseRegisterContract;

import java.util.List;

import io.ona.rdt_app.activity.PatientRegisterActivity;
import io.ona.rdt_app.contract.PatientRegisterActivityContract;
import io.ona.rdt_app.fragment.PatientRegisterFragment;
import io.ona.rdt_app.model.Patient;
import io.ona.rdt_app.util.RDTJsonFormUtils;

import static io.ona.rdt_app.util.Constants.Form.RDT_TEST_FORM;

/**
 * Created by Vincent Karuri on 07/06/2019
 */
public class PatientRegisterActivityPresenter implements BaseRegisterContract.Presenter, PatientRegisterActivityContract.Presenter {

    private PatientRegisterActivity activity;

    public PatientRegisterActivityPresenter(PatientRegisterActivity activity) {
        this.activity = activity;
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
    public void saveForm(String jsonForm) throws JSONException {
        PatientRegisterFragmentPresenter patientRegisterFragmentPresenter = ((PatientRegisterFragment) getView().getRegisterFragment()).getPresenter();
        JSONObject jsonFormObject = new JSONObject(jsonForm);
        RDTJsonFormUtils.appendEntityId(jsonFormObject);
        patientRegisterFragmentPresenter.saveForm(jsonFormObject, getView());
        Patient rdtPatient = patientRegisterFragmentPresenter.getRDTPatient(jsonFormObject);
        if (rdtPatient != null) {
            patientRegisterFragmentPresenter.launchForm(getView(), RDT_TEST_FORM, rdtPatient);
        }
    }

    private PatientRegisterActivity getView() {
        return this.activity;
    }
}
