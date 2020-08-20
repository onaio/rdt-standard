package io.ona.rdt.interactor;

import com.vijay.jsonwizard.utils.FormUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

import static io.ona.rdt.util.Constants.DBConstants.SEX;
import static io.ona.rdt.util.Constants.FormFields.ENTITY_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.DRIVERS_LICENSE_NUMBER;
import static io.ona.rdt.util.CovidConstants.FormFields.PASSPORT_NO;
import static io.ona.rdt.util.CovidConstants.FormFields.PATIENT_FIRST_NAME;
import static io.ona.rdt.util.CovidConstants.FormFields.PATIENT_LAST_NAME;
import static org.smartregister.util.JsonFormUtils.VALUE;
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
        return createPatient(getString(jsonFormObject, ENTITY_ID), JsonFormUtils.fields(jsonFormObject));
    }

    @Override
    protected Patient createPatient(String baseEntityId, JSONArray formFields) {
        if (baseEntityId == null) { return null; }

        String firstName = FormUtils.getFieldJSONObject(formFields, PATIENT_FIRST_NAME).optString(VALUE);
        String lastName = FormUtils.getFieldJSONObject(formFields, PATIENT_LAST_NAME).optString(VALUE);
        String sex = FormUtils.getFieldJSONObject(formFields, SEX).optString(VALUE);

        JSONObject patientIdObj = FormUtils.getFieldJSONObject(formFields, DRIVERS_LICENSE_NUMBER);
        patientIdObj = patientIdObj == null ? FormUtils.getFieldJSONObject(formFields, PASSPORT_NO) : patientIdObj;
        String patientId = patientIdObj.optString(VALUE);

        return new Patient(StringUtils.join(new String[]{firstName, lastName}, ' '),
                sex, baseEntityId, patientId);
    }
}
