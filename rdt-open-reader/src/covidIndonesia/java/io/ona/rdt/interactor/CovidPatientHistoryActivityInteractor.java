package io.ona.rdt.interactor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.util.FormKeyTextExtractionUtil;
import timber.log.Timber;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryActivityInteractor {

    private PatientHistoryRepository patientHistoryRepository;

    public CovidPatientHistoryActivityInteractor() {
        patientHistoryRepository = new PatientHistoryRepository();
    }

    public List<PatientHistoryEntry> getPatientHistoryEntries(String baseEntityId, String eventType, String date) throws JSONException {
        Map<String, String> formWidgetKeyToTextMap = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();
        List<PatientHistoryEntry> patientHistoryEntries = new ArrayList<>();
        EventClient eventClient = patientHistoryRepository.getEvent(baseEntityId, eventType, date);
        if (eventClient != null) {
            for (Obs obs : eventClient.getEvent().getObs()) {
                String text = formWidgetKeyToTextMap.get(obs.getFormSubmissionField());
                if (StringUtils.isNotBlank(text)) {
                    PatientHistoryEntry patientHistoryEntry = new PatientHistoryEntry(text,
                            getValues(obs, formWidgetKeyToTextMap));
                    patientHistoryEntries.add(patientHistoryEntry);
                }
            }
        }
        return patientHistoryEntries;
    }

    private String getValues(Obs obs, Map<String, String> formWidgetKeyToTextMap) {
        List<String> values = new ArrayList<>();
        // for checkboxes, get keys from key-value map
        Map<String, Object> keyValPairs = obs.getKeyValPairs();
        List<Object> obsValues = Utils.isEmptyMap(keyValPairs) ? obs.getValues()
                : new ArrayList<>(keyValPairs.keySet());
        for (Object value : obsValues) {
            values.add(getValue(value.toString(), formWidgetKeyToTextMap));
        }
        return StringUtils.join(values, ", ");
    }

    public JSONArray getJSONArr(String str) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(str);
        } catch (JSONException ex) {
            Timber.i("Invalid JSON array obs value.");
        }
        return jsonArray;
    }

    /**
     *
     * Takes care of the special case in native radio button and checkbox where the key should be used
     *
     * @param value
     * @param formWidgetKeyToTextMap
     * @return
     */
    private String getValue(String value, Map<String, String> formWidgetKeyToTextMap) {
        // if value is a json string, return the length of json array (occurs in repeating groups)
        JSONArray jsonArray = getJSONArr(value);
        if (jsonArray != null) {
            return String.valueOf(jsonArray.length());
        }
        return formWidgetKeyToTextMap.containsKey(value) ? formWidgetKeyToTextMap.get(value) : value;
    }
}
