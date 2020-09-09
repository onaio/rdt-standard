package io.ona.rdt.robolectric.widget;

import android.content.Intent;

import com.google.android.gms.vision.barcode.Barcode;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;

import java.util.Date;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.widget.OneScanCovidRDTBarcodeFactory;

public class OneScanCovidRDTBarcodeFactoryTest extends WidgetFactoryRobolectricTest {

    private OneScanCovidRDTBarcodeFactory oneScanCovidRDTBarcodeFactory;

    private WidgetArgs widgetArgs;
    private JsonFormActivity jsonFormActivity;

    @Before
    public void setUp() throws JSONException {
        super.setUp();
        oneScanCovidRDTBarcodeFactory = new OneScanCovidRDTBarcodeFactory();
        setWidgetArgs();
        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "stepStateConfig", RDTApplication.getInstance().getStepStateConfiguration());
    }

    @Test
    public void testMoveToNextStepShouldGotoTwoSensorTriggered() throws Exception {
        Barcode barcode = new Barcode();
        barcode.displayValue = ",,,,true";
        Intent data = new Intent();
        data.putExtra(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_KEY, barcode);

        Whitebox.invokeMethod(oneScanCovidRDTBarcodeFactory, "moveToNextStep", data, Mockito.mock(Date.class));
    }

    private void setWidgetArgs() {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = Mockito.mock(RDTJsonFormFragment.class);
        widgetArgs.setFormFragment(formFragment);

        jsonFormActivity = Mockito.mock(JsonFormActivity.class);
        widgetArgs.setContext(jsonFormActivity);
        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "widgetArgs", widgetArgs);
    }
}
