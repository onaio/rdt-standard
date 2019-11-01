package io.ona.rdt.presenter;

import io.ona.rdt.contract.RDTJsonFormActivityContract;
import io.ona.rdt.fragment.RDTJsonFormFragment;

/**
 * Created by Vincent Karuri on 16/08/2019
 */
public class RDTJsonFormActivityPresenter implements RDTJsonFormActivityContract.Presenter {

    private RDTJsonFormActivityContract.View activity;

    public RDTJsonFormActivityPresenter(RDTJsonFormActivityContract.View activity) {
        this.activity = activity;
    }

    @Override
    public void onBackPress() {
        int currStep = RDTJsonFormFragment.getCurrentStep();
        // disable backpress for timer, rdt capture and expiration date screens
        if (currStep != 6 && currStep != 13 && currStep != 14) {
            activity.onBackPress();
        }
    }
}
