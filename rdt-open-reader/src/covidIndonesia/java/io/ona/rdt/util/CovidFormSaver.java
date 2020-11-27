package io.ona.rdt.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Obs;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.widget.CovidRDTBarcodeFactory;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidFormSaver extends FormSaver {

    @Override
    protected boolean isPatientRegistrationEvent(String encounterType) {
        return CovidConstants.Encounter.COVID_PATIENT_REGISTRATION.equals(encounterType);
    }

    @Override
    protected void closeIDs(org.smartregister.domain.Event dbEvent) {
        super.closeIDs(dbEvent);
        // close respiratory sample id
        Obs idObs = dbEvent.findObs(null, false, CovidConstants.FormFields.COVID_SAMPLE_ID, CovidRDTBarcodeFactory.BATCH_ID);
        if (idObs != null) {
            String rdtId = idObs.getValue() == null ? "" : idObs.getValue().toString();
            RDTApplication.getInstance().getContext().getUniqueIdRepository().close(rdtId);
        }
    }

    @Override
    protected boolean formHasUniqueIDs(String bindType) {
        return CovidConstants.Table.COVID_RDT_TESTS.equals(bindType)
                || CovidConstants.Table.SAMPLE_COLLECTIONS.equals(bindType)
                || CovidConstants.Table.SAMPLE_DELIVERY_RECORDS.equals(bindType);
    }

    @Override
    protected String getBindType(String encounterType) {
        String bindType = super.getBindType(encounterType);
        if (StringUtils.isNotBlank(bindType)) {
            return bindType;
        }
        switch (encounterType) {
            case CovidConstants.Encounter.COVID_PATIENT_REGISTRATION:
                bindType = CovidConstants.Table.COVID_PATIENTS;
                break;
            case CovidConstants.Encounter.COVID_RDT_TEST:
                bindType = CovidConstants.Table.COVID_RDT_TESTS;
                break;
            case CovidConstants.Encounter.PATIENT_DIAGNOSTICS:
                bindType = CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
                break;
            case CovidConstants.Encounter.SAMPLE_COLLECTION:
                bindType = CovidConstants.Table.SAMPLE_COLLECTIONS;
                break;
            case CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS:
                bindType = CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;
                break;
            case CovidConstants.Encounter.COVID_WBC:
                bindType = CovidConstants.Table.COVID_WBC_RECORDS;
                break;
            case CovidConstants.Encounter.COVID_XRAY:
                bindType = CovidConstants.Table.COVID_XRAY_RECORDS;
        }
        return bindType;
    }

    @Override
    protected void populatePhoneMetadata(Event event) {
        super.populatePhoneMetadata(event);
        // add location information
        org.smartregister.clientandeventmodel.Obs obs = new org.smartregister.clientandeventmodel.Obs();
        obs.setFieldCode(CovidConstants.FormFields.LAST_KNOWN_LOCATION);
        obs.setValue(RDTApplication.getInstance().getContext().allSettings().get(CovidConstants.FormFields.LAST_KNOWN_LOCATION,
                new JSONObject().toString()));
        obs.setFormSubmissionField(CovidConstants.FormFields.LAST_KNOWN_LOCATION);
        event.addObs(obs);
    }
}
