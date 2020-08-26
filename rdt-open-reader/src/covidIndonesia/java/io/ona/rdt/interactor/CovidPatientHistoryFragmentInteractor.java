package io.ona.rdt.interactor;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.util.Utils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.util.FormKeyTextExtractionUtil;
import timber.log.Timber;

import static io.ona.rdt.util.Utils.convertDate;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryFragmentInteractor {

    private PatientHistoryRepository patientHistoryRepository;

    public CovidPatientHistoryFragmentInteractor() {
        patientHistoryRepository = new PatientHistoryRepository();
    }

    public List<Visit> getVisits(String baseEntityId) {
        List<EventClient> eventClients = patientHistoryRepository.getEventsByUniqueDate(baseEntityId);
        List<Visit> visits = new ArrayList<>();
        for (int i = 0; i < eventClients.size(); i++) {
            visits.add(new Visit(String.format("Visit %d", i + 1),
                    formatDate(eventClients.get(i).getEvent().getEventDate())));
        }
        return visits;
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
        return StringUtils.join(values, ",");
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
        return formWidgetKeyToTextMap.containsKey(value) ? formWidgetKeyToTextMap.get(value) : value;
    }

    private String formatDate(DateTime eventDate) {
        String date = null;
        try {
            date = convertDate(eventDate.toString(), "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd");
        } catch (ParseException e) {
            Timber.e(e);
        }
        return date;
    }
}
