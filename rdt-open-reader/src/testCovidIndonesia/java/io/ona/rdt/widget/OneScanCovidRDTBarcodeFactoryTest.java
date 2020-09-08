package io.ona.rdt.widget;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.Date;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.StepStateConfig;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RDTJsonFormFragment.class, Bundle.class, RDTApplication.class, LayoutInflater.class})
public class OneScanCovidRDTBarcodeFactoryTest {

    private static final String STEP_6 = "step6";

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
        mockStaticMethods();
        setWidgetArgs();
    }

    @After
    public void tearDown() {
        oneScanCovidRDTBarcodeFactory = null;
    }

    @Test
    public void testMoveToNextStepShouldGotoTwoSensorTriggered() {
        oneScanCovidRDTBarcodeFactory.moveToNextStep(PowerMockito.mock(Date.class));
    }

    private void setWidgetArgs() throws JSONException {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = PowerMockito.mock(RDTJsonFormFragment.class);
        widgetArgs.setFormFragment(formFragment);
        widgetArgs.setStepName(STEP_6);

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

        jsonFormActivity = PowerMockito.mock(JsonFormActivity.class);
        Resources resources = PowerMockito.mock(Resources.class);
        PowerMockito.doReturn(resources).when(jsonFormActivity).getResources();
        PowerMockito.doReturn(0).when(resources).getColor(ArgumentMatchers.anyInt());
        PowerMockito.doReturn(jsonObject).when(jsonFormActivity).getStep(Mockito.eq(STEP_6));
        widgetArgs.setContext(jsonFormActivity);
        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "widgetArgs", widgetArgs);
    }

    private void mockStaticMethods() {

        PowerMockito.mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = PowerMockito.mock(JSONObject.class);
        PowerMockito.doReturn("step11").when(jsonObject).optString(Mockito.eq(Constants.Step.SENSOR_TRIGGERED_PAGE), Mockito.eq("step1"));

        PowerMockito.doReturn(jsonObject).when(stepStateConfig).getStepStateObj();

        Whitebox.setInternalState(oneScanCovidRDTBarcodeFactory, "stepStateConfig", stepStateConfig);
    }
}
