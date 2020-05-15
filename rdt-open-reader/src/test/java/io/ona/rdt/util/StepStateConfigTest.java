package io.ona.rdt.util;

import android.content.Context;

import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.smartregister.util.AssetHandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by Vincent Karuri on 13/05/2020
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({AssetHandler.class})
public class StepStateConfigTest {

    private final String STEP_STATE_CONFIG = "{\n" +
            "  \"rdt_expired_page\": \"step6\",\n" +
            "  \"blot_paper_task_page\": \"step9\",\n" +
            "  \"disabled_back_press_pages\" : [\n" +
            "    \"step6\",\n" +
            "    \"step13\",\n" +
            "    \"step14\"\n" +
            "  ],\n" +
            "  \"manual_entry_expiration_page\": \"step20\",\n" +
            "  \"rdt_id_key\": \"rdt_id\",\n" +
            "  \"expiration_date_reader_address\": \"step4:expiration_date_reader\",\n" +
            "  \"rdt_id_lbl_addresses\": [\n" +
            "    \"step7:lbl_rdt_id\",\n" +
            "    \"step8:lbl_rdt_id\",\n" +
            "    \"step9:lbl_rdt_id\",\n" +
            "    \"step18:lbl_rdt_id\",\n" +
            "    \"step19:lbl_rdt_id\"\n" +
            "  ],\n" +
            "  \"scan_qr_page\": \"step5\",\n" +
            "  \"scan_carestart_page\": \"step4\",\n" +
            "  \"twenty_min_countdown_timer_page\": \"step13\",\n" +
            "  \"take_image_of_rdt_page\": \"step15\",\n" +
            "  \"covid_manual_rdt_entry_page\": \"step5\",\n" +
            "  \"covid_scan_barcode_page\": \"step7\",\n" +
            "  \"covid_conduct_rdt_page\": \"step4\",\n" +
            "  \"covid_respiratory_specimen_collection_opt_in_page\": \"step13\",\n" +
            "  \"covid_collect_respiratory_specimen_page\": \"step14\",\n" +
            "  \"covid_test_complete_page\": \"step20\",\n" +
            "  \"covid_one_scan_widget_specimen_page\": \"step18\",\n" +
            "  \"covid_affix_respiratory_sample_id_page\": \"step17\",\n" +
            "  \"covid_rdt_expired_page\": \"step8\"\n" +
            "}";

    @Test
    public void testStepStateConfigInstantiationShouldNotReturnNull() throws JSONException {
        mockStaticMethods();
        assertNotNull(StepStateConfig.getInstance(mock(Context.class)));
        assertNotNull(StepStateConfig.getInstance(mock(Context.class)).getStepStateObj());
        assertEquals("step6", StepStateConfig.getInstance(mock(Context.class)).getStepStateObj().getString("rdt_expired_page"));
    }

    private void mockStaticMethods() {
        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when(AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(STEP_STATE_CONFIG);
    }
}
