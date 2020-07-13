package io.ona.rdt.util;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.domain.db.Obs;

import io.ona.rdt.application.RDTApplication;

import static io.ona.rdt.util.Constants.Encounter.PATIENT_REGISTRATION;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_PATIENT_REGISTRATION;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.PATIENT_DIAGNOSTICS;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_COLLECTION;
import static io.ona.rdt.util.CovidConstants.Encounter.SAMPLE_DELIVERY_DETAILS;
import static io.ona.rdt.util.CovidConstants.Encounter.SUPPORT_INVESTIGATION;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.CovidConstants.Table.COVID_PATIENTS;
import static io.ona.rdt.util.CovidConstants.Table.COVID_RDT_TESTS;
import static io.ona.rdt.util.CovidConstants.Table.PATIENT_DIAGNOSTIC_RESULTS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_COLLECTIONS;
import static io.ona.rdt.util.CovidConstants.Table.SAMPLE_DELIVERY_RECORDS;
import static io.ona.rdt.util.CovidConstants.Table.SUPPORT_INVESTIGATIONS;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidFormSaver extends FormSaver {

    @Override
    protected boolean isPatientRegistrationEvent(String encounterType) {
        return PATIENT_REGISTRATION.equals(encounterType) || COVID_PATIENT_REGISTRATION.equals(encounterType);
    }

    @Override
    protected void closeIDs(org.smartregister.domain.db.Event dbEvent) {
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
        String bindType = getBindType(encounterType);
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
            case SUPPORT_INVESTIGATION:
                bindType = SUPPORT_INVESTIGATIONS;
                break;
        }
        return bindType;
    }
}
