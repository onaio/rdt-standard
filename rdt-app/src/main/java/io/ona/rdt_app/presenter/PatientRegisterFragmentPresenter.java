package io.ona.rdt_app.presenter;

import android.app.Activity;

import org.json.JSONException;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import io.ona.rdt_app.contract.PatientRegisterFragmentContract;
import io.ona.rdt_app.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt_app.model.Patient;
import timber.log.Timber;

import static io.ona.rdt_app.util.Constants.PATIENTS;

/**
 * Created by Vincent Karuri on 11/06/2019
 */
public class PatientRegisterFragmentPresenter implements PatientRegisterFragmentContract.Presenter {

    private final String TAG = PatientRegisterFragmentPresenter.class.getName();

    private PatientRegisterFragmentInteractor interactor = new PatientRegisterFragmentInteractor();
    private PatientRegisterFragmentContract.View patientRegisterFragment;

    public PatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        this.patientRegisterFragment = patientRegisterFragment;
    }

    @Override
    public void processViewConfigurations() {
        // won't implement
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = PATIENTS;
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
        SmartRegisterQueryBuilder countQueryBuilder = new SmartRegisterQueryBuilder();
        countQueryBuilder.SelectInitiateMainTableCounts(tableName);
        return countQueryBuilder.mainCondition(mainCondition);
    }

    public String mainSelect(String tableName, String mainCondition) {
        SmartRegisterQueryBuilder queryBUilder = new SmartRegisterQueryBuilder();
        queryBUilder.SelectInitiateMainTable(tableName, this.mainColumns(tableName));
        return queryBUilder.mainCondition(mainCondition);
    }

    private String[] mainColumns(String tableName) {
        String[] columns = new String[]{tableName + "." + "relationalid", tableName + ".name", tableName + "." + "age", tableName + "." + "sex"};
        return columns;
    }

    @Override
    public String getMainCondition() {
        return String.format(" %s != '%s'", "name", "");
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
