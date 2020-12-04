package io.ona.rdt.widget;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.robolectric.util.ReflectionHelpers;

import java.util.HashMap;

import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.Utils;

/**
 * Created by Vincent Karuri on 04/12/2020
 */
public class RDTDeviceSelectorSpinnerFactoryTest extends WidgetFactoryRobolectricTest {

    private RDTDeviceSelectorSpinnerFactory deviceSelectorSpinnerFactory;
    private JSONObject deviceSelectorSpinnerWidget;

    @Mock
    private CommonListener listener;
    @Mock
    private JsonFormFragment jsonFormFragment;
    @Mock
    private CovidRDTJsonFormUtils formUtils;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deviceSelectorSpinnerFactory = new RDTDeviceSelectorSpinnerFactory();
        mockMethods();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetViewsFromJsonShouldCorrectlyBootStrapWidget() throws Exception {
        deviceSelectorSpinnerFactory.getViewsFromJson(JsonFormConstants.STEP1, jsonFormActivity, jsonFormFragment,
                deviceSelectorSpinnerWidget, listener, false);
    }

    private void mockMethods() throws JSONException {
        deviceSelectorSpinnerWidget = new JSONObject();
        deviceSelectorSpinnerWidget.put(JsonFormConstants.KEY, JsonFormConstants.KEY);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.TYPE, CovidConstants.Widget.RDT_DEVICE_SELECTOR_SPINNER);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.HINT, JsonFormConstants.HINT);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPTIONS_FIELD_NAME, Utils.createOptionsBlock(new HashMap<String, String>() {
            {
                put("device1", "Device1");
                put("device2", "Device2");
                put("device3", "Device3");
            }
        }, "", "", ""));
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, JsonFormConstants.OPENMRS_ENTITY_PARENT);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY, JsonFormConstants.OPENMRS_ENTITY);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY_ID, JsonFormConstants.OPENMRS_ENTITY_ID);

        ReflectionHelpers.setField(deviceSelectorSpinnerFactory, "formUtils", formUtils);
    }
}