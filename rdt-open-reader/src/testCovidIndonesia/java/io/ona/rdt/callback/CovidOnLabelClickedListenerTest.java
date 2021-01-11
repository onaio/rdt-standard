package io.ona.rdt.callback;

import com.vijay.jsonwizard.domain.WidgetArgs;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.util.ReflectionHelpers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

public class CovidOnLabelClickedListenerTest extends RobolectricTest {

    private static final String STEP_JSON = "{\"covid_scan_barcode_page\":\"step8\",\"manual_expiration_date_entry_page\":\"step6\",\"covid_one_scan_widget_specimen_page\":\"step6\",\"covid_affix_respiratory_sample_id_page\":\"step7\",\"covid_scan_sample_for_delivery_page\":\"step3\",\"covid_enter_delivery_details_page\":\"step4\"}";

    private Map<String, String> stepMap;
    private CovidOnLabelClickedListener labelClickedListener;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        buildSteps();
        labelClickedListener = new CovidOnLabelClickedListener(getWidgetArgs());
    }

    @Test
    public void testGetNextStepShouldPopulateRelevantSteps() {

        Set<Map.Entry<String, String>> entrySet = stepMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            ReflectionHelpers.setField(labelClickedListener, "key", entry.getKey());
            String nextStep = labelClickedListener.getNextStep();
            Assert.assertEquals(entry.getValue(), nextStep);
        }
    }

    private WidgetArgs getWidgetArgs() {
        WidgetArgs widgetArgs = new WidgetArgs();
        widgetArgs.setFormFragment(new RDTJsonFormFragment());
        widgetArgs.setJsonObject(new JSONObject());
        return widgetArgs;
    }

    private void buildSteps() throws JSONException {
        JSONObject stepJSON = new JSONObject(STEP_JSON);

        stepMap = new HashMap<>();
        stepMap.put(CovidConstants.FormFields.LBL_SCAN_BARCODE, stepJSON.getString(CovidConstants.Step.COVID_SCAN_BARCODE_PAGE));
        stepMap.put(CovidConstants.FormFields.LBL_ENTER_RDT_MANUALLY, stepJSON.getString(Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE));
        stepMap.put(CovidConstants.FormFields.LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE, stepJSON.getString(CovidConstants.Step.COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE));
        stepMap.put(CovidConstants.FormFields.LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL, stepJSON.getString(CovidConstants.Step.COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE));
        stepMap.put(CovidConstants.FormFields.LBL_SCAN_SAMPLE_BARCODE, stepJSON.getString(CovidConstants.Step.COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE));
        stepMap.put(CovidConstants.FormFields.LBL_ENTER_SAMPLE_DETAILS_MANUALLY, stepJSON.getString(CovidConstants.Step.COVID_ENTER_DELIVERY_DETAILS_PAGE));
    }
}
