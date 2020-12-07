package io.ona.rdt.widget;

import android.view.View;
import android.widget.RelativeLayout;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.customviews.MaterialSpinner;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;

import java.util.HashMap;
import java.util.List;

import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.Utils;

/**
 * Created by Vincent Karuri on 04/12/2020
 */

@Config(shadows = {RDTJsonFormUtilsShadow.class})
public class RDTDeviceSelectorSpinnerFactoryTest extends WidgetFactoryRobolectricTest {

    private RDTDeviceSelectorSpinnerFactory deviceSelectorSpinnerFactory;
    private JSONObject deviceSelectorSpinnerWidget;
    private WidgetArgs widgetArgs;

    @Captor
    private ArgumentCaptor<WidgetArgs> widgetArgsArgumentCaptor;
    @Mock
    private CommonListener listener;
    @Mock
    private JsonFormFragment jsonFormFragment;
    @Mock
    private CovidRDTJsonFormUtils formUtils;

    private final String DEVICE_1 = "device_1";
    private final String DEVICE_2 = "device_2";
    private final String DEVICE_3 = "device_3";

    @Before
    public void setUp() throws Exception {
        super.setUp();
        deviceSelectorSpinnerFactory = new RDTDeviceSelectorSpinnerFactory();
        mockMethods();
    }

    @After
    public void tearDown() {
        RDTJsonFormUtilsShadow.setJsonObject(null);
    }

    @Test
    public void testGetViewsFromJsonShouldCorrectlyBootStrapWidget() throws Exception {
        List<View> views = deviceSelectorSpinnerFactory.getViewsFromJson(JsonFormConstants.STEP1, jsonFormActivity, jsonFormFragment,
                deviceSelectorSpinnerWidget, listener, false);

        MaterialSpinner materialSpinner = ReflectionHelpers.callInstanceMethod(deviceSelectorSpinnerFactory, "getSpinner",
                ReflectionHelpers.ClassParameter.from(RelativeLayout.class, views.get(0)));

        deviceSelectorSpinnerWidget.put(JsonFormConstants.VALUE, DEVICE_1);
        materialSpinner.setSelection(1);
        Mockito.verify(formUtils).populateRDTDetailsConfirmationPage(widgetArgsArgumentCaptor.capture(), ArgumentMatchers.eq(DEVICE_1));
        verifyWidgetArgsMatch(widgetArgsArgumentCaptor.getValue());

        deviceSelectorSpinnerWidget.put(JsonFormConstants.VALUE, DEVICE_2);
        materialSpinner.setSelection(2);
        Mockito.verify(formUtils).populateRDTDetailsConfirmationPage(widgetArgsArgumentCaptor.capture(), ArgumentMatchers.eq(DEVICE_2));
        verifyWidgetArgsMatch(widgetArgsArgumentCaptor.getValue());
    }

    private void verifyWidgetArgsMatch(WidgetArgs capturedWidgetArgs) {
        Assert.assertEquals(widgetArgs.getContext(), capturedWidgetArgs.getContext());
        Assert.assertEquals(widgetArgs.getStepName(), capturedWidgetArgs.getStepName());
        Assert.assertEquals(widgetArgs.getFormFragment(), capturedWidgetArgs.getFormFragment());
        Assert.assertEquals(widgetArgs.getJsonObject(), capturedWidgetArgs.getJsonObject());
        Assert.assertEquals(widgetArgs.getListener(), capturedWidgetArgs.getListener());
        Assert.assertEquals(widgetArgs.isPopup(), capturedWidgetArgs.isPopup());
    }

    private void mockMethods() throws JSONException {
        deviceSelectorSpinnerWidget = new JSONObject();
        deviceSelectorSpinnerWidget.put(JsonFormConstants.KEY, JsonFormConstants.KEY);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.TYPE, CovidConstants.Widget.RDT_DEVICE_SELECTOR_SPINNER);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.HINT, JsonFormConstants.HINT);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPTIONS_FIELD_NAME, Utils.createOptionsBlock(new HashMap<String, String>() {
            {
                put(DEVICE_1, "Device1");
                put(DEVICE_2, "Device2");
                put(DEVICE_3, "Device3");
            }
        }, "", "", ""));
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, JsonFormConstants.OPENMRS_ENTITY_PARENT);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY, JsonFormConstants.OPENMRS_ENTITY);
        deviceSelectorSpinnerWidget.put(JsonFormConstants.OPENMRS_ENTITY_ID, JsonFormConstants.OPENMRS_ENTITY_ID);

        RDTJsonFormUtilsShadow.setJsonObject(deviceSelectorSpinnerWidget);

        ReflectionHelpers.setField(deviceSelectorSpinnerFactory, "formUtils", formUtils);

        widgetArgs = new WidgetArgs().withJsonObject(deviceSelectorSpinnerWidget).withListener(listener)
                .withFormFragment(jsonFormFragment).withPopup(false).withStepName(JsonFormConstants.STEP1).withContext(jsonFormActivity);
    }
}
