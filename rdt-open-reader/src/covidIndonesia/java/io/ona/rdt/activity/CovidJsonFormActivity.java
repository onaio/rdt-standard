package io.ona.rdt.activity;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import io.ona.rdt.fragment.CovidJsonFormFragment;
import io.ona.rdt.fragment.RDTJsonFormFragment;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormActivity extends RDTJsonFormActivity {

    @Override
    public void initializeFormFragment() {
        CovidJsonFormFragment jsonFormFragment = (CovidJsonFormFragment) CovidJsonFormFragment.getFormFragment(JsonFormConstants.FIRST_STEP_NAME);
        getSupportFragmentManager().beginTransaction().add(com.vijay.jsonwizard.R.id.container, jsonFormFragment).commitAllowingStateLoss();
    }
}
