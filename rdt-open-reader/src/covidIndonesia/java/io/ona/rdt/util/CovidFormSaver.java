package io.ona.rdt.util;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.smartregister.clientandeventmodel.Event;
import org.smartregister.domain.Obs;

import io.ona.rdt.application.RDTApplication;

import static io.ona.rdt.util.CovidConstants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_WBC;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_XRAY;
import static io.ona.rdt.util.CovidConstants.Encounter.PATIENT_DIAGNOSTICS;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.FormFields.LAST_KNOWN_LOCATION;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_WBC_RECORDS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_XRAY_RECORDS;
import static io.ona.rdt.util.CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidFormSaver extends FormSaver {

    @Override
    protected boolean isPatientRegistrationEvent(String encounterType) {
        return COVID_PATIENT_REGISTRATION.equals(encounterType);
    }

    @Override
    protected void closeIDs(org.smartregister.domain.Event dbEvent) {
        super.closeIDs(dbEvent);
        // close respiratory sample id
        Obs idObs = dbEvent.findObs(null, false, COVID_SAMPLE_ID);
        if (idObs != null) {
            String rdtId = idObs.getValue() == null ? "" : idObs.getValue().toString();
            RDTApplication.getInstance().getContext().getUniqueIdRepository().close(rdtId);
        }
    }

    @Override
    protected boolean formHasUniqueIDs(String bindType) {
        return COVID_RDT_TESTS.equals(bindType) || SAMPLE_COLLECTIONS.equals(bindType);
    }

    @Override
    protected String getBindType(String encounterType) {
        String bindType = super.getBindType(encounterType);
        if (StringUtils.isNotBlank(bindType)) { return bindType; }
        switch (encounterType) {
            case COVID_PATIENT_REGISTRATION:
                bindType = COVID_PATIENTS;
                break;
            case COVID_RDT_TEST:
                bindType = COVID_RDT_TESTS;
                break;
            case PATIENT_DIAGNOSTICS:
                bindType = PATIENT_DIAGNOSTIC_RESULTS;
                break;
            case SAMPLE_COLLECTION:
                bindType = SAMPLE_COLLECTIONS;
                break;
            case SAMPLE_DELIVERY_DETAILS:
                bindType = SAMPLE_DELIVERY_RECORDS;
                break;
            case COVID_WBC:
                bindType = COVID_WBC_RECORDS;
                break;
            case COVID_XRAY:
                bindType = COVID_XRAY_RECORDS;
        }
        return bindType;
    }

    @Override
    protected void populatePhoneMetadata(Event event) {
        super.populatePhoneMetadata(event);
        // add location information
        org.smartregister.clientandeventmodel.Obs obs = new org.smartregister.clientandeventmodel.Obs();
        obs.setFieldCode(LAST_KNOWN_LOCATION);
        obs.setValue(RDTApplication.getInstance().getContext().allSettings().get(LAST_KNOWN_LOCATION,
                new JSONObject().toString()));
        obs.setFormSubmissionField(LAST_KNOWN_LOCATION);
        event.addObs(obs);
    }
}
