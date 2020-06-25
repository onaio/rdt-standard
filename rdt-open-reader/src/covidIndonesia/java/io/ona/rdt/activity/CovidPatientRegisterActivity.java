package io.ona.rdt.activity;

import android.view.MenuItem;

import org.smartregister.view.fragment.BaseRegisterFragment;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.util.Utils;

import static io.ona.rdt.util.Constants.Form.SAMPLE_DELIVERY_DETAILS_FORM;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterActivity extends PatientRegisterActivity {

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new CovidPatientRegisterFragment();
    }

    @Override
    public boolean selectDrawerItem(MenuItem menuItem) {
        if (super.selectDrawerItem(menuItem)) {
            return true;
        }
        switch(menuItem.getItemId()) {
            case R.id.menu_item_create_shipment:
                getPresenter().launchForm(this, SAMPLE_DELIVERY_DETAILS_FORM, null);
                return true;
        }
        return false;
    }
}
