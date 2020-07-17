package io.ona.rdt.activity;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.fragment.RDTJsonFormFragment;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormActivity extends RDTJsonFormActivity {

    @Override
    protected JsonFormFragment getFirstStep() {
        return CovidJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
    }
}
