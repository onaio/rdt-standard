package io.ona.rdt.interactor;

import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.json.JSONObject;

import io.ona.rdt.widget.CustomRDTCaptureFactory;
import io.ona.rdt.widget.RDTBarcodeFactory;
import io.ona.rdt.widget.RDTCountdownTimerFactory;
import io.ona.rdt.widget.RDTExpirationDateReaderFactory;
import io.ona.rdt.widget.RDTGpsFactory;
import io.ona.rdt.widget.RDTLabelFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.COUNTDOWN_TIMER;
import static com.vijay.jsonwizard.constants.JsonFormConstants.GPS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE;
import static io.ona.rdt.widget.RDTExpirationDateReaderFactory.EXPIRATION_DATE_CAPTURE;

/**
 * Created by Vincent Karuri on 19/06/2019
 */
public class RDTJsonFormInteractor extends JsonFormInteractor {

    private static final RDTJsonFormInteractor INSTANCE = new RDTJsonFormInteractor();

    private PatientRegisterFragmentInteractor patientRegisterFragmentInteractor;

    public static JsonFormInteractor getInstance() {
        return INSTANCE;
    }

    @Override
    protected void registerWidgets() {
        super.registerWidgets();
        map.put(BARCODE, new RDTBarcodeFactory());
        map.put(LABEL, new RDTLabelFactory());
        map.put(EXPIRATION_DATE_CAPTURE, new RDTExpirationDateReaderFactory());
        map.put(RDT_CAPTURE, new CustomRDTCaptureFactory());
        map.put(COUNTDOWN_TIMER, new RDTCountdownTimerFactory());
        map.put(GPS, new RDTGpsFactory());
    }

    public void saveForm(JSONObject jsonForm) {
        getPatientRegisterFragmentInteractor().saveForm(jsonForm, null);
    }

    public PatientRegisterFragmentInteractor getPatientRegisterFragmentInteractor() {
        if (patientRegisterFragmentInteractor == null) {
            patientRegisterFragmentInteractor = new PatientRegisterFragmentInteractor();
        }
        return patientRegisterFragmentInteractor;
    }
}
