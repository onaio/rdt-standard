package io.ona.rdt.interactor;

import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import org.json.JSONObject;

import io.ona.rdt.widget.GoogleCovidRDTBarcodeFactory;
import io.ona.rdt.widget.OneScanCovidRDTBarcodeFactory;
import io.ona.rdt.widget.MalariaRDTBarcodeFactory;
import io.ona.rdt.widget.UWCovidRDTCaptureFactory;
import io.ona.rdt.widget.UWMalariaRDTCaptureFactory;
import io.ona.rdt.widget.RDTCountdownTimerFactory;
import io.ona.rdt.widget.RDTExpirationDateReaderFactory;
import io.ona.rdt.widget.RDTGpsFactory;
import io.ona.rdt.widget.RDTLabelFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.COUNTDOWN_TIMER;
import static com.vijay.jsonwizard.constants.JsonFormConstants.GPS;
import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE;
import static io.ona.rdt.util.Constants.Widget.COVID_GOOGLE_BARCODE_READER;
import static io.ona.rdt.util.Constants.Widget.COVID_ONE_SCAN_BARCODE_READER;
import static io.ona.rdt.util.Constants.Widget.UW_COVID_RDT_CAPTURE;
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
        map.put(BARCODE, new MalariaRDTBarcodeFactory());
        map.put(LABEL, new RDTLabelFactory());
        map.put(EXPIRATION_DATE_CAPTURE, new RDTExpirationDateReaderFactory());
        map.put(RDT_CAPTURE, new UWMalariaRDTCaptureFactory());
        map.put(COUNTDOWN_TIMER, new RDTCountdownTimerFactory());
        map.put(GPS, new RDTGpsFactory());
        map.put(UW_COVID_RDT_CAPTURE, new UWCovidRDTCaptureFactory());
        map.put(COVID_GOOGLE_BARCODE_READER, new GoogleCovidRDTBarcodeFactory());
        map.put(COVID_ONE_SCAN_BARCODE_READER, new OneScanCovidRDTBarcodeFactory());
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
