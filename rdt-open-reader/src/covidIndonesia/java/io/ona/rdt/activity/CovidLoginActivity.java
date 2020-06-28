package io.ona.rdt.activity;

/**
 * Created by Vincent Karuri on 19/06/2020
 */
public class CovidLoginActivity extends LoginActivity {

    @Override
    protected Class getLoginActivityClass() {
        return CovidPatientRegisterActivity.class;
    }
}
