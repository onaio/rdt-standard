package io.ona.rdt.robolectric.util;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.smartregister.AllConstants;
import org.smartregister.util.JsonFormUtils;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class FormKeyTextExtractionUtilTest extends RobolectricTest {

    @Test
    public void testGetFormWidgetKeyToTextMapShouldPopulateNonEmptyMap() throws Exception {

        Map<String, String> expectedData = getTestFormWidgetKeyToTextMap();
        Map<String, String> actualData = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String expectedValue = entry.getValue();
            String actualValue = actualData.get(entry.getKey());
            System.out.println("expected key: " + entry.getKey());
            System.out.println("expected val: " + expectedValue + " - actual val: " + actualValue);
        }

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String expectedValue = entry.getValue();
            String actualValue = actualData.get(entry.getKey());
            System.out.println("expected: " + expectedValue + " - actual: " + actualValue);
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    private Map<String, String> getTestFormWidgetKeyToTextMap() throws Exception {
        Map<String, String> formWidgetKeyToTextMap = new HashMap<>();
        JSONObject formJsonObj = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getTranslatedForm", "patient-diagnostics-form.json");
        JSONArray fields = JsonFormUtils.fields(formJsonObj);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String fieldType = field.getString(JsonFormConstants.TYPE);
            // add main widget text
            String widgetKey = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getWidgetKey", field.optString(JsonFormConstants.KEY));
            String widgetText = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getWidgetText", field);
            if (StringUtils.isNotBlank(widgetText)) {
                formWidgetKeyToTextMap.put(widgetKey, widgetText);
            }
            // for repeating group, use text hint to populate its _count field
            if (JsonFormConstants.REPEATING_GROUP.equals(fieldType)) {
                formWidgetKeyToTextMap.put(widgetKey + "_count",
                        field.getString(RepeatingGroupFactory.REFERENCE_EDIT_TEXT_HINT));
            }
            // add options text
            if (JsonFormConstants.CHECK_BOX.equals(fieldType)
                    || JsonFormConstants.NATIVE_RADIO_BUTTON.equals(fieldType)) {
                JSONArray options = field.getJSONArray(AllConstants.OPTIONS);
                for (int j = 0; j < options.length(); j++) {
                    JSONObject option = options.getJSONObject(j);
                    formWidgetKeyToTextMap.put(option.getString(JsonFormConstants.KEY),
                            option.getString(JsonFormConstants.TEXT));
                }
            }
        }
        return formWidgetKeyToTextMap;
    }
}
