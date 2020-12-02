package io.ona.rdt.interactor;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.smartregister.domain.Event;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryActivityInteractor {

    private PatientHistoryRepository patientHistoryRepository;
    private final Set<String> UNIQUE_MANUAL_FORM_IDS = new HashSet<>(Arrays.asList(Constants.FormFields.RDT_ID,
            CovidConstants.FormFields.COVID_SAMPLE_ID));

    public CovidPatientHistoryActivityInteractor() {
        patientHistoryRepository = new PatientHistoryRepository();
    }

    public List<PatientHistoryEntry> getPatientHistoryEntries(String baseEntityId, String eventType, String date) throws JSONException {
        Map<String, String> formWidgetKeyToTextMap = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();
        List<PatientHistoryEntry> patientHistoryEntries = new ArrayList<>();
        EventClient eventClient = patientHistoryRepository.getEvent(baseEntityId, eventType, date);
        if (eventClient != null) {
            Event event = eventClient.getEvent();
            for (Obs obs : event.getObs()) {
                if (!shouldAddObs(obs, event)) {
                    continue;
                }
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

    private boolean shouldAddObs(Obs obs, Event event) {
        // do not process unique manual id if unique barcode id exists
        boolean shouldIncludeManualIDField = isUniqueManualFormIDField(obs.getFormSubmissionField())
                &&  event.findObs("", true, CovidConstants.FormFields.UNIQUE_ID) == null;

        // do not process repeating group field, process it's corresponding _count field instead
        boolean isNotRepeatingGroupField = !isUniqueManualFormIDField(obs.getFormSubmissionField())
                && event.findObs("", true, obs.getFormSubmissionField() + "_count") == null;

        return shouldIncludeManualIDField || isNotRepeatingGroupField;
    }

    private String getValues(Obs obs, Map<String, String> formWidgetKeyToTextMap) {
        List<String> values = new ArrayList<>();
        // for checkboxes, get keys from key-value map
        Map<String, Object> keyValPairs = obs.getKeyValPairs();
        List<Object> obsValues = Utils.isEmptyMap(keyValPairs) ? obs.getValues()
                : new ArrayList<>(keyValPairs.keySet());
        for (Object value : obsValues) {
            values.add(getValue(value.toString(), formWidgetKeyToTextMap, obs));
        }
        return StringUtils.join(values, ", ");
    }

    /**
     * Takes care of the special case in native radio button and checkbox where the key should be used
     *
     * @param value
     * @param formWidgetKeyToTextMap
     * @return
     */
    private String getValue(String value, Map<String, String> formWidgetKeyToTextMap, Obs obs) {

        if (formWidgetKeyToTextMap.containsKey(value)) {
            if (StringUtils.isNotBlank(formWidgetKeyToTextMap.get(value))) {
                return formWidgetKeyToTextMap.get(value);
            }
            else {
                return obs.getValue().toString();
            }
        } else {
            return value;
        }
    }

    private boolean isUniqueManualFormIDField(String key) {
        return UNIQUE_MANUAL_FORM_IDS.contains(key);
    }
}
