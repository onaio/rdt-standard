package io.ona.rdt.interactor;

import com.vijay.jsonwizard.utils.NativeFormLangUtils;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.db.EventClient;
import org.smartregister.util.JsonFormUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.util.CovidRDTJsonFormUtils;
import io.ona.rdt.util.RDTJsonFormUtils;

import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.LABEL;
import static com.vijay.jsonwizard.constants.JsonFormConstants.TEXT;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SUPPORT_INVESTIGATION_FORM;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryFragmentInteractor {

    private static final Set<String> FORMS_TO_EXTRACT_TEXT_FROM = new HashSet<>(Arrays.asList(
            PATIENT_DIAGNOSTICS_FORM, SAMPLE_COLLECTION_FORM, SUPPORT_INVESTIGATION_FORM, COVID_RDT_TEST_FORM));
    private static Map<String, String> formWidgetKeyToTextMap;
    private PatientHistoryRepository patientHistoryRepository;

    public CovidPatientHistoryFragmentInteractor() {
        patientHistoryRepository = new PatientHistoryRepository();
    }

    public List<Visit> getVisits(String baseEntityId) {
        List<EventClient> eventClients = patientHistoryRepository.getEventsByUniqueDate(baseEntityId);
        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < visits.size(); i++) {
            visits.add(new Visit(String.format("Visit %d", i + 1), visits.get(i).getDateOfVisit()));
        }
        return visits;
    }

    public static Map<String, String> getFormWidgetKeyToTextMap() throws JSONException {
        if (formWidgetKeyToTextMap == null) {
            formWidgetKeyToTextMap = new HashMap<>();
            for (String formName : FORMS_TO_EXTRACT_TEXT_FROM) {
                JSONObject formJsonObj = getTranslatedForm(formName);
                JSONArray fields = JsonFormUtils.fields(formJsonObj);
                for (int i = 0; i < fields.length(); i++) {
                    JSONObject field = fields.getJSONObject(i);
                    String widgetKey = getWidgetKey(field.optString(KEY));
                    String widgetText = getWidgetText(field);
                    if (StringUtils.isNotBlank(widgetText)) {
                        formWidgetKeyToTextMap.put(widgetKey, widgetText);
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
