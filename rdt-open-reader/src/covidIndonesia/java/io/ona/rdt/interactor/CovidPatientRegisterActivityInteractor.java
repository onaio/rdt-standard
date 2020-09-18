package io.ona.rdt.interactor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

import static org.smartregister.util.JsonFormUtils.getString;

/**
 * Created by Vincent Karuri on 17/07/2020
 */
public class CovidPatientRegisterActivityInteractor extends PatientRegisterActivityInteractor {

    @Override
    protected FormLauncher createFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected FormSaver createFormSaver() {
        return new CovidFormSaver();
    }

    @Override
    public Patient getPatientForRDT(JSONObject jsonFormObject) {
        return createPatient(getString(jsonFormObject, Constants.FormFields.ENTITY_ID),
                JsonFormUtils.fields(jsonFormObject));
    }

    @Override
    protected Patient createPatient(String baseEntityId, JSONArray formFields) {
        if (baseEntityId == null) {
            return null;
        }

        String firstName = getStrField(formFields, CovidConstants.FormFields.PATIENT_FIRST_NAME);
        String lastName = getStrField(formFields, CovidConstants.FormFields.PATIENT_LAST_NAME);
        String sex = getStrField(formFields, Constants.DBConstants.SEX);

        String patientId = getStrField(formFields, CovidConstants.FormFields.DRIVERS_LICENSE_NUMBER);
        patientId = patientId == null ? getStrField(formFields, CovidConstants.FormFields.PASSPORT_NO)
                : patientId;

        Patient patient = new Patient(StringUtils.join(new String[]{firstName, lastName}, ' '),
                sex, baseEntityId, patientId);
        populateCommonPatientFields(patient, formFields);

        return isValidPatient(patient) ? patient : null;
    }
}
