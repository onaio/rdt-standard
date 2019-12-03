package io.ona.rdt.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.RDT_PATIENTS;

/**
 * Created by Vincent Karuri on 11/06/2019
 */
public class PatientRegisterFragmentPresenter implements PatientRegisterFragmentContract.Presenter {

    private final String TAG = PatientRegisterFragmentPresenter.class.getName();

    private PatientRegisterFragmentInteractor interactor;
    private PatientRegisterFragmentContract.View patientRegisterFragment;
    private SmartRegisterQueryBuilder smartRegisterQueryBuilder;

    public PatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        this.patientRegisterFragment = patientRegisterFragment;
        this.interactor = new PatientRegisterFragmentInteractor();
        this.smartRegisterQueryBuilder = new SmartRegisterQueryBuilder();
    }

    @Override
    public void processViewConfigurations() {
        // won't implement
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = RDT_PATIENTS;
        String countSelect = countSelect(tableName, mainCondition);
        String mainSelect = mainSelect(tableName, mainCondition);
        patientRegisterFragment.initializeQueryParams(tableName, countSelect, mainSelect);
        patientRegisterFragment.initializeAdapter();
        patientRegisterFragment.countExecute();
        patientRegisterFragment.filterandSortInInitializeQueries();
    }

    @Override
    public void startSync() {
        // todo: implement this
    }

    @Override
    public void searchGlobally(String uniqueId) {
        // todo: implement this
    }


    public String countSelect(String tableName, String mainCondition) {
        smartRegisterQueryBuilder.SelectInitiateMainTableCounts(tableName);
        return smartRegisterQueryBuilder.mainCondition(mainCondition);
    }

    public String mainSelect(String tableName, String mainCondition) {
        smartRegisterQueryBuilder.SelectInitiateMainTable(tableName, this.mainColumns(tableName));
        return smartRegisterQueryBuilder.mainCondition(mainCondition);
    }

    private String[] mainColumns(String tableName) {
        String[] columns = new String[]{tableName + "." + "relationalid", tableName + ".name", tableName + "." + "age", tableName + "." + "sex", tableName + "." + "patient_id"};
        return columns;
    }

    @Override
    public String getMainCondition() {
        return String.format(" %s != '%s' or %s != '%s'", "name", "", "patient_id", "");
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
