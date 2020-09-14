package io.ona.rdt.util;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.utils.NativeFormLangUtils;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.AllConstants;
import org.smartregister.util.JsonFormUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.application.RDTApplication;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class FormKeyTextExtractionUtil {

    private static final Set<String> FORMS_TO_EXTRACT_TEXT_FROM = new HashSet<>(Arrays.asList(
            CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM, CovidConstants.Form.SAMPLE_COLLECTION_FORM,
            CovidConstants.Form.SUPPORT_INVESTIGATION_FORM, CovidConstants.Form.COVID_RDT_TEST_FORM));
    private static Map<String, String> formWidgetKeyToTextMap;

    public static Map<String, String> getFormWidgetKeyToTextMap() throws JSONException {
        if (formWidgetKeyToTextMap == null) {
            formWidgetKeyToTextMap = new HashMap<>();
            for (String formName : FORMS_TO_EXTRACT_TEXT_FROM) {
                JSONObject formJsonObj = getTranslatedForm(formName);
                JSONArray fields = JsonFormUtils.fields(formJsonObj);
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = fields.getJSONObject(i);
                    String fieldType = field.getString(JsonFormConstants.TYPE);
                    // add main widget text
                    String widgetKey = getWidgetKey(field.optString(JsonFormConstants.KEY));
                    String widgetText = getWidgetText(field);
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
        }
        return formWidgetKeyToTextMap;
    }

    public static void destroyFormWidgetKeyToTextMap() {
        formWidgetKeyToTextMap = null;
    }

    private static JSONObject getTranslatedForm(String formName) throws JSONException {
        String placeholderInjectedForm = new CovidRDTJsonFormUtils()
                .getFormJsonObject(formName, RDTApplication.getInstance()).toString();
        String translatedForm = NativeFormLangUtils
                .getTranslatedString(placeholderInjectedForm, RDTApplication.getInstance());

        return new JSONObject(translatedForm);
    }

    private static String getWidgetKey(String key) {
        return key.replace("_lbl", "");
    }

    private static String getWidgetText(JSONObject field) {
        return StringUtils.isBlank(field.optString(JsonFormConstants.TEXT))
                ? field.optString(JsonFormConstants.LABEL) : field.optString(JsonFormConstants.TEXT);
    }
}
