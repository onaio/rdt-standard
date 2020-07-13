package io.ona.rdt.activity;

import android.os.Bundle;

import com.vijay.jsonwizard.interfaces.FormWidgetFactory;

import java.util.Map;

import io.ona.rdt.interactor.RDTJsonFormInteractor;
import io.ona.rdt.widget.CovidRDTLabelFactory;
import io.ona.rdt.widget.GoogleCovidRDTBarcodeFactory;
import io.ona.rdt.widget.OneScanCovidRDTBarcodeFactory;
import io.ona.rdt.widget.UWCovidRDTCaptureFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static io.ona.rdt.util.CovidConstants.Widget.GOOGLE_COVID_BARCODE_READER;
import static io.ona.rdt.util.CovidConstants.Widget.ONE_SCAN_COVID_BARCODE_READER;
import static io.ona.rdt.util.CovidConstants.Widget.UW_COVID_RDT_CAPTURE;

/**
 * Created by Vincent Karuri on 19/06/2020
 */
public class CovidLoginActivity extends LoginActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerWidgets();
    }

    private void registerWidgets() {
        Map<String, FormWidgetFactory> widgetFactoryMap = RDTJsonFormInteractor.getInstance().map;
        widgetFactoryMap.put(UW_COVID_RDT_CAPTURE, new UWCovidRDTCaptureFactory());
        widgetFactoryMap.put(GOOGLE_COVID_BARCODE_READER, new GoogleCovidRDTBarcodeFactory());
        widgetFactoryMap.put(ONE_SCAN_COVID_BARCODE_READER, new OneScanCovidRDTBarcodeFactory());
        widgetFactoryMap.put(LABEL, new CovidRDTLabelFactory());
    }

    @Override
    protected Class getLoginActivityClass() {
        return CovidPatientRegisterActivity.class;
    }
}
