package io.ona.rdt.presenter;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONException;
import org.smartregister.cursoradapter.SmartRegisterQueryBuilder;

import io.ona.rdt.contract.PatientRegisterFragmentContract;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.interactor.PatientRegisterFragmentInteractor;
import io.ona.rdt.util.Constants;
import timber.log.Timber;

import static io.ona.rdt.util.Constants.DBConstants.AGE;
import static io.ona.rdt.util.Constants.DBConstants.FIRST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.LAST_NAME;
import static io.ona.rdt.util.Constants.DBConstants.PATIENT_ID;
import static io.ona.rdt.util.Constants.DBConstants.SEX;

/**
 * Created by Vincent Karuri on 11/06/2019
 */
public class PatientRegisterFragmentPresenter implements PatientRegisterFragmentContract.Presenter {

    protected PatientRegisterFragmentInteractor interactor;
    private PatientRegisterFragmentContract.View patientRegisterFragment;
    private SmartRegisterQueryBuilder smartRegisterQueryBuilder;

    public PatientRegisterFragmentPresenter(PatientRegisterFragmentContract.View patientRegisterFragment) {
        this.patientRegisterFragment = patientRegisterFragment;
        this.interactor = getInteractor();
        this.smartRegisterQueryBuilder = new SmartRegisterQueryBuilder();
    }

    @Override
    public void processViewConfigurations() {
        // won't implement
    }

    @Override
    public void initializeQueries(String mainCondition) {
        String tableName = patientRegisterFragment.getRegisterTableName();
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
        smartRegisterQueryBuilder.selectInitiateMainTableCounts(tableName);
        return smartRegisterQueryBuilder.mainCondition(mainCondition);
    }

    public String mainSelect(String tableName, String mainCondition) {
        smartRegisterQueryBuilder.selectInitiateMainTable(tableName, this.mainColumns(tableName));
        return smartRegisterQueryBuilder.mainCondition(mainCondition);
    }

    private String[] mainColumns(String tableName) {
        String[] columns = new String[] {"relationalid", FIRST_NAME, LAST_NAME, AGE, SEX, PATIENT_ID, Constants.DBConstants.DOB};
        for (int i = 0; i < columns.length; i++) {
            columns[i] = TextUtils.join(".", new String[]{tableName, columns[i]});
        }
        return columns;
    }

    @Override
    public String getMainCondition() {
        return String.format(" (%s != '%s' or %s != '%s' or %s != '%s')", FIRST_NAME, "", LAST_NAME, "", PATIENT_ID, "");
    }

    @Override
    public void launchForm(Activity activity, String formName, Patient patient) {
        try {
            interactor.launchForm(activity, formName, patient);
        } catch (JSONException e) {
            Timber.e(e);
        }
    }

    protected PatientRegisterFragmentInteractor getInteractor() {
        if (interactor == null) {
            interactor = createInteractor();
        }
        return interactor;
    }

    protected PatientRegisterFragmentInteractor createInteractor() {
        return new PatientRegisterFragmentInteractor();
    }
}
