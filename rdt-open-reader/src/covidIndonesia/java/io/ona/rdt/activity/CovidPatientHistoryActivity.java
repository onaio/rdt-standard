package io.ona.rdt.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import io.ona.rdt.R;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.util.Constants;

import static io.ona.rdt.util.Utils.updateLocale;

public class CovidPatientHistoryActivity extends AppCompatActivity {

    private Patient currPatient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_patient_history);
        updateLocale(this);
        currPatient = getIntent().getParcelableExtra(Constants.FormFields.PATIENT);
    }
}