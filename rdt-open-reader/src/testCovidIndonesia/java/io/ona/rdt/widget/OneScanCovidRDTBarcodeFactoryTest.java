package io.ona.rdt.widget;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.Button;
import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Date;

import io.ona.rdt.R;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, Bundle.class, RDTApplication.class, LayoutInflater.class})
public class OneScanCovidRDTBarcodeFactoryTest {

    private OneScanCovidRDTBarcodeFactory oneScanCovidRDTBarcodeFactory;

    private WidgetArgs widgetArgs;
    private JsonFormActivity jsonFormActivity;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private StepStateConfig stepStateConfig;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        oneScanCovidRDTBarcodeFactory = new OneScanCovidRDTBarcodeFactory();
        //mockStaticClasses();
        mockStaticMethods();
        setWidgetArgs();
    }

    @After
    public void tearDown() {
        oneScanCovidRDTBarcodeFactory = null;
    }

    @Test
    public void testMoveToNextStepShouldGotoTwoSensorTriggered() {
        oneScanCovidRDTBarcodeFactory.moveToNextStep(mock(Date.class));
    }

    private void setWidgetArgs() throws JSONException {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        widgetArgs.setFormFragment(formFragment);
        widgetArgs.setStepName("step6");

        String step = "{\n" +
                "    \"title\": \"Sample collection\",\n" +
                "    \"display_back_button\": \"true\",\n" +
                "    \"next\": \"step8\",\n" +
                "    \"bottom_navigation\": \"true\",\n" +
                "    \"bottom_navigation_orientation\": \"vertical\",\n" +
                "    \"fields\": [\n" +
                "      {\n" +
                "        \"key\": \"temp_sensor\",\n" +
                "        \"openmrs_entity_parent\": \"\",\n" +
                "        \"openmrs_entity\": \"\",\n" +
                "        \"openmrs_entity_id\": \"\",\n" +
                "        \"type\": \"hidden\",\n" +
                "        \"value\": \"true\"\n" +
                "      }\n" +
                "    ]\n" +
                "  }";

        JSONObject jsonObject = new JSONObject(step);

        widgetArgs.setJsonObject(jsonObject);

        jsonFormActivity = mock(JsonFormActivity.class);
        Resources resources = mock(Resources.class);
        doReturn(resources).when(jsonFormActivity).getResources();
        doReturn(0).when(resources).getColor(anyInt());
        doReturn(jsonObject).when(jsonFormActivity).getStep(Mockito.eq("step6"));
        widgetArgs.setContext(jsonFormActivity);
        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "widgetArgs", widgetArgs);
    }

    private void mockStaticMethods() throws JSONException {

        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step11").when(jsonObject).optString(eq(Constants.Step.SENSOR_TRIGGERED_PAGE), Mockito.eq("step1"));

        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "stepStateConfig", stepStateConfig);
    }

    private void mockStaticClasses() {
        mockStatic(LayoutInflater.class);
        LayoutInflater layoutInflater = mock(LayoutInflater.class);
        mockStatic(LayoutInflater.class);
        RelativeLayout rootLayout = mock(RelativeLayout.class);
        PowerMockito.when(LayoutInflater.from(any(Context.class))).thenReturn(layoutInflater);
        doReturn(rootLayout).when(layoutInflater).inflate(anyInt(), isNull());
        doReturn(mock(MaterialEditText.class)).when(rootLayout).findViewById(R.id.edit_text);
        doReturn(mock(Button.class)).when(rootLayout).findViewById(R.id.scan_button);
    }
}
