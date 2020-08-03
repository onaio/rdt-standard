package io.ona.rdt.util;

import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidFormLauncher extends FormLauncher {

    @Override
    protected boolean formRequiresUniqueId(String formName) {
        return COVID_RDT_TEST_FORM.equals(formName) || SAMPLE_COLLECTION_FORM.equals(formName);
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return new CovidRDTJsonFormUtils();
    }
}
