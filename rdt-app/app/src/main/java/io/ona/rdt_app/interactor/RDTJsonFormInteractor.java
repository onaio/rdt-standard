package io.ona.rdt_app.interactor;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interactors.JsonFormInteractor;

import io.ona.rdt_app.widget.RDTBarcodeFactory;

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
        map.put(JsonFormConstants.BARCODE, new RDTBarcodeFactory());
    }
}
