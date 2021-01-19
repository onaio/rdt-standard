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

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class FormKeyTextExtractionUtilTest extends RobolectricTest {

    private static final Set<String> FORMS_TO_EXTRACT_TEXT_FROM = new HashSet<>(Arrays.asList(
            CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM, CovidConstants.Form.SAMPLE_COLLECTION_FORM,
            CovidConstants.Form.COVID_RDT_TEST_FORM, CovidConstants.Form.XRAY_FORM, CovidConstants.Form.WBC_FORM));

    @Test
    public void testGetFormWidgetKeyToTextMapShouldPopulateNonEmptyMap() throws Exception {

        Map<String, String> formWidgetKeyToTextMap = new HashMap<>();
        for (String formName : FORMS_TO_EXTRACT_TEXT_FROM) {
            JSONObject formJsonObj = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getTranslatedForm", formName);
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
        }

        Map<String, String> data = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();
        Assert.assertFalse(formWidgetKeyToTextMap.isEmpty());
        Assert.assertEquals(formWidgetKeyToTextMap.size(), data.size());

        for (Map.Entry<String, String> entry : formWidgetKeyToTextMap.entrySet()) {
            Assert.assertTrue(data.containsKey(entry.getKey()));
        }
    }
}
