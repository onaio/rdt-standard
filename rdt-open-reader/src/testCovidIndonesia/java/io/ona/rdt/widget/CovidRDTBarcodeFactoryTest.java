package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.smartregister.domain.UniqueId;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import io.ona.rdt.callback.OnUniqueIdsFetchedCallback;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import io.ona.rdt.util.FormLaunchArgs;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.Utils;

@RunWith(PowerMockRunner.class)
@PrepareForTest({DeviceDefinitionProcessor.class})
public class CovidRDTBarcodeFactoryTest {

    private CovidRDTBarcodeFactory covidRdtBarcodeFactory;
    private JsonApiTestContext jsonApiTestContext;
    private ArrayList<UniqueId> idList;
    private RDTJsonFormFragment formFragment;

    @Before
    public void setUp() throws JSONException, IOException, FHIRParserException {
        covidRdtBarcodeFactory = Mockito.mock(CovidRDTBarcodeFactory.class, Mockito.CALLS_REAL_METHODS);
        Mockito.doNothing().when(covidRdtBarcodeFactory).moveToNextStep();
        Mockito.doNothing().when(covidRdtBarcodeFactory).navigateToUnusableProductPage();
        Mockito.when(covidRdtBarcodeFactory.getBarcodeValsAsCSV(Mockito.any(Intent.class)))
                .thenReturn("val-0,2020-08-08,val-2,val-3,true");
        Mockito.when(covidRdtBarcodeFactory.splitCSV(Mockito.anyString()))
                .thenReturn(new String[]{"val-0", "2020-08-08", "val-2", "val-3", "true"});

        PowerMockito.mockStatic(DeviceDefinitionProcessor.class);
        DeviceDefinitionProcessor deviceDefinitionProcessor = Mockito.mock(DeviceDefinitionProcessor.class);
        Mockito.when(deviceDefinitionProcessor.getDeviceId(Mockito.anyString())).thenReturn(null);
        PowerMockito.when(DeviceDefinitionProcessor.getInstance(Mockito.any(Context.class))).thenReturn(deviceDefinitionProcessor);

        mockInternalState();
    }

    @Test
    public void testOnActivityResultShouldPopulateCorrectData() throws Exception {
        Intent intent = Mockito.mock(Intent.class);
        Mockito.when(intent.getBooleanExtra("enable_batch_scan", false)).thenReturn(true);
        Mockito.when(intent.getStringExtra("data")).thenReturn("{}");
        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);
        verifyPopulateBarcodeDataShouldPopulateCorrectData();

        Mockito.when(intent.getBooleanExtra("enable_batch_scan", false)).thenReturn(false);
        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_OK, intent);
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "", "val-0,2020-08-08,val-2,val-3,true", "", "", "", false);
        verifyPopulateRelevantFieldsShouldPopulateCorrectData();
        Mockito.verify(covidRdtBarcodeFactory).navigateToUnusableProductPage();

        covidRdtBarcodeFactory.onActivityResult(JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE, Activity.RESULT_CANCELED, intent);
        Mockito.verify(formFragment).setMoveBackOneStep(true);
    }

    public void verifyPopulateBarcodeDataShouldPopulateCorrectData() throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("batch_id", Utils.getUniqueId(idList));
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "qr_code_reader", jsonObject.toString(), "", "", "", false);
        Mockito.verify(jsonApiTestContext).writeValue("test-page", "batch_id", Utils.getUniqueId(idList), "", "", "", false);
        Mockito.verify(covidRdtBarcodeFactory).moveToNextStep();
    }

    public void verifyPopulateRelevantFieldsShouldPopulateCorrectData() throws JSONException {
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "unique_id", "val-0", "", "", "", false);
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "exp_date", "2020-08-08", "", "", "", false);
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "lot_no", "val-2", "", "", "", false);
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "gtin", "val-3", "", "", "", false);
        Mockito.verify(jsonApiTestContext).writeValue("test-step", "temp_sensor", "true", "", "", "", false);
    }

    private void mockInternalState() throws JSONException {
        UniqueId uniqueId = new UniqueId("test-id", "test-openmrsid", "test-status", "test-user", new Date());
        idList = new ArrayList<>();
        idList.add(uniqueId);
        RDTJsonFormUtils formUtils = new RDTJsonFormUtils() {
            @Override
            public synchronized void getNextUniqueIds(FormLaunchArgs args, OnUniqueIdsFetchedCallback callBack, int numOfIDs) {
                callBack.onUniqueIdsFetched(args, idList);
            }
        };
        Whitebox.setInternalState(covidRdtBarcodeFactory, "formUtils", formUtils);

        jsonApiTestContext = Mockito.mock(JsonApiTestContext.class);
        formFragment = Mockito.mock(RDTJsonFormFragment.class);
        WidgetArgs widgetArgs = Mockito.mock(WidgetArgs.class);
        Mockito.when(widgetArgs.getContext()).thenReturn((Context) jsonApiTestContext);
        Mockito.when(widgetArgs.getStepName()).thenReturn("test-step");
        Mockito.when(widgetArgs.getJsonObject()).thenReturn(new JSONObject());
        Mockito.when(widgetArgs.getFormFragment()).thenReturn(formFragment);
        covidRdtBarcodeFactory.widgetArgs = widgetArgs;

        JSONObject stepStateConfig = new JSONObject();
        stepStateConfig.put(CovidConstants.Step.UNIQUE_BATCH_ID_PAGE, "test-page");
        covidRdtBarcodeFactory.stepStateConfig = stepStateConfig;
    }

    private static abstract class JsonApiTestContext extends Context implements JsonApi {
    }

}
