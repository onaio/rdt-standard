package io.ona.rdt.fragment;

import static io.ona.rdt.util.Constants.Encounter.RDT_TEST;
import static io.ona.rdt.util.CovidConstants.Encounter.COVID_RDT_TEST;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public class CovidJsonFormFragment extends RDTJsonFormFragment {

    @Override
    protected boolean formHasSpecialNavigationRules(String eventType) {
        return RDT_TEST.equals(eventType) || COVID_RDT_TEST.equals(eventType);
    }
}
