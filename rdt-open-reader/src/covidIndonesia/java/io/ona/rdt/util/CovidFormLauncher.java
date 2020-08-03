package io.ona.rdt.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidFormLauncher extends FormLauncher {

    private Set<String> formsThatRequireUniqueIDs = new HashSet<>(Arrays.asList(SAMPLE_COLLECTION_FORM,
            COVID_RDT_TEST_FORM, PATIENT_DIAGNOSTICS_FORM));

    @Override
    protected boolean formRequiresUniqueId(String formName) {
        return formsThatRequireUniqueIDs.contains(formName);
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return new CovidRDTJsonFormUtils();
    }
}
