package io.ona.rdt_app.interactor;

import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import io.ona.rdt_app.widget.RDTBarcodeFactory;
import io.ona.rdt_app.widget.RDTLabelFactory;

import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;

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
        map.put(BARCODE, new RDTBarcodeFactory());
        map.put(LABEL, new RDTLabelFactory());
    }
}
