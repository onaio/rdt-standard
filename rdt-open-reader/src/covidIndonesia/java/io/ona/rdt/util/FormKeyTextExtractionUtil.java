package io.ona.rdt.util;

import com.vijay.jsonwizard.utils.NativeFormLangUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.application.RDTApplication;

import static com.vijay.jsonwizard.constants.JsonFormConstants.CHECK_BOX;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.NATIVE_RADIO_BUTTON;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TEXT;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TYPE;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SUPPORT_INVESTIGATION_FORM;
import static org.smartregister.AllConstants.OPTIONS;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class FormKeyTextExtractionUtil {

    private static final Set<String> FORMS_TO_EXTRACT_TEXT_FROM = new HashSet<>(Arrays.asList(
            PATIENT_DIAGNOSTICS_FORM, SAMPLE_COLLECTION_FORM, SUPPORT_INVESTIGATION_FORM, COVID_RDT_TEST_FORM));
    private static Map<String, String> formWidgetKeyToTextMap;

    public static Map<String, String> getFormWidgetKeyToTextMap() throws JSONException {
        if (formWidgetKeyToTextMap == null) {
            formWidgetKeyToTextMap = new HashMap<>();
            for (String formName : FORMS_TO_EXTRACT_TEXT_FROM) {
                JSONObject formJsonObj = getTranslatedForm(formName);
                JSONArray fields = JsonFormUtils.fields(formJsonObj);
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = fields.getJSONObject(i);
                    String fieldType = field.getString(TYPE);
                    // add main widget text
                    String widgetKey = getWidgetKey(field.optString(KEY));
                    String widgetText = getWidgetText(field);
                    if (StringUtils.isNotBlank(widgetText)) {
                        formWidgetKeyToTextMap.put(widgetKey, widgetText);
                    }
                    // add options text
                    if (CHECK_BOX.equals(fieldType) || NATIVE_RADIO_BUTTON.equals(fieldType)) {
                        JSONArray options = field.getJSONArray(OPTIONS);
                        for (int j = 0; j < options.length(); j++) {
                            JSONObject option = options.getJSONObject(j);
                            formWidgetKeyToTextMap.put(option.getString(KEY), option.getString(TEXT));
                        }
                    }
                }
            }
        }
        return formWidgetKeyToTextMap;
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
        return StringUtils.isBlank(field.optString(TEXT)) ? field.optString(LABEL)
                : field.optString(TEXT);
    }
}
