package io.ona.rdt.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import org.smartregister.view.fragment.BaseRegisterFragment;

import io.ona.rdt.R;
import io.ona.rdt.fragment.CovidPatientRegisterFragment;
import io.ona.rdt.presenter.CovidPatientRegisterActivityPresenter;
import io.ona.rdt.presenter.PatientRegisterActivityPresenter;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM;

/**
 * Created by Vincent Karuri on 16/06/2020
 */
public class CovidPatientRegisterActivity extends PatientRegisterActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createFormWidgetKeyToTextMap();
    }

    private void createFormWidgetKeyToTextMap() {
        class CreateFormWidgetKeyToTextMapTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                getCovidPatientRegisterActivityPresenter().createFormWidgetKeyToTextMap();
                return null;
            }
        }
    }

    @Override
    public BaseRegisterFragment getRegisterFragment() {
        return new CovidPatientRegisterFragment();
    }

    @Override
    protected RDTJsonFormUtils initializeFormUtils() {
        return new CovidRDTJsonFormUtils();
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

    @Override
    protected Class getLoginPage() {
        return CovidLoginActivity.class;
    }

    protected PatientRegisterActivityPresenter createPatientRegisterActivityPresenter() {
        return new CovidPatientRegisterActivityPresenter(this);
    }

    private CovidPatientRegisterActivityPresenter getCovidPatientRegisterActivityPresenter() {
        return (CovidPatientRegisterActivityPresenter) getPresenter();
    }
}
