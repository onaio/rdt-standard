package io.ona.rdt.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.activity.CovidJsonFormActivity;
import io.ona.rdt.domain.Patient;

import static com.vijay.jsonwizard.constants.JsonFormConstants.ENCOUNTER_TYPE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.VALUE;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.LBL_RESPIRATORY_SAMPLE_ID;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidRDTJsonFormUtils extends RDTJsonFormUtils {

    @Override
    public void prePopulateFormFields(JSONObject jsonForm, Patient patient, String uniqueID) throws JSONException {
        JSONArray fields = getMultiStepFormFields(jsonForm);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            switch (jsonForm.getString(ENCOUNTER_TYPE)) {
                case COVID_RDT_TEST:
                    prePopulateRDTFormFields(field, uniqueID);
                    break;
                case SAMPLE_COLLECTION:
                    prePopulateSampleCollectionFormFields(field, uniqueID);
                    break;
            }
            prePopulateRDTPatientFields(patient, field);
        }
    }

    private void  prePopulateSampleCollectionFormFields(JSONObject field, String uniqueID) throws JSONException {
        // pre-populate respiratory sample id labels
        if (LBL_RESPIRATORY_SAMPLE_ID.equals(field.getString(KEY))) {
            field.put("text", "Sample ID: " + uniqueID);
        }
        // pre-populate respiratory sample id field
        if (COVID_SAMPLE_ID.equals(field.getString(KEY))) {
            field.put(VALUE, uniqueID);
        }
    }

    @Override
    protected boolean formShouldBePrePopulated(String formName) {
        return RDT_TEST_FORM.equals(formName) || SAMPLE_COLLECTION_FORM.equals(formName);
    }

    @Override
    protected Class getJsonFormActivityClass() {
        return CovidJsonFormActivity.class;
    }
}