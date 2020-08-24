package io.ona.rdt.interactor;

import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.widget.GoogleCovidRDTBarcodeFactory;
import io.ona.rdt.widget.MalariaRDTBarcodeFactory;
import io.ona.rdt.widget.OneScanCovidRDTBarcodeFactory;
import io.ona.rdt.widget.RDTCountdownTimerFactory;
import io.ona.rdt.widget.RDTExpirationDateReaderFactory;
import io.ona.rdt.widget.RDTGpsFactory;
import io.ona.rdt.widget.RDTLabelFactory;
import io.ona.rdt.widget.UWCovidRDTCaptureFactory;
import io.ona.rdt.widget.UWMalariaRDTCaptureFactory;

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
        map.put(CovidConstants.Widget.UW_COVID_RDT_CAPTURE, new UWCovidRDTCaptureFactory());
        map.put(CovidConstants.Widget.GOOGLE_COVID_BARCODE_READER, new GoogleCovidRDTBarcodeFactory());
        map.put(CovidConstants.Widget.ONE_SCAN_COVID_BARCODE_READER, new OneScanCovidRDTBarcodeFactory());
    }
}
