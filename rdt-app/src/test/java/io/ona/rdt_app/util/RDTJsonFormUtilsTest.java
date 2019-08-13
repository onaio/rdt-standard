package io.ona.rdt_app.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.smartregister.exception.JsonFormMissingStepCountException;

import io.ona.rdt_app.model.Patient;

import static io.ona.rdt_app.util.Constants.BULLET_DOT;
import static io.ona.rdt_app.util.Constants.Form.RDT_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class RDTJsonFormUtilsTest {

    private RDTJsonFormUtils formUtils;

    private final String RDT_TEST_JSON_FORM = "{\n" +
            "  \"count\": \"18\",\n" +
            "  \"encounter_type\": \"rdt_test\",\n" +
            "  \"entity_id\": \"\",\n" +
            "  \"metadata\": {\n" +
            "    \"start\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"start\",\n" +
            "      \"openmrs_entity_id\": \"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"end\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"end\",\n" +
            "      \"openmrs_entity_id\": \"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"today\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"encounter\",\n" +
            "      \"openmrs_entity_id\": \"encounter_date\"\n" +
            "    },\n" +
            "    \"deviceid\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"deviceid\",\n" +
            "      \"openmrs_entity_id\": \"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"subscriberid\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"subscriberid\",\n" +
            "      \"openmrs_entity_id\": \"163150AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"simserial\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"simserial\",\n" +
            "      \"openmrs_entity_id\": \"163151AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"phonenumber\": {\n" +
            "      \"openmrs_entity_parent\": \"\",\n" +
            "      \"openmrs_entity\": \"concept\",\n" +
            "      \"openmrs_data_type\": \"phonenumber\",\n" +
            "      \"openmrs_entity_id\": \"163152AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\"\n" +
            "    },\n" +
            "    \"encounter_location\": \"\"\n" +
            "  },\n" +
            "  \"step1\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step2\",\n" +
            "    \"bottom_navigation\" : \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"RDT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_patient_name\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_patient_gender_and_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"8sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step2\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step3\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_which_rdt\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Which RDT?\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_scan_qr_code\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Scan QR Code\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"next\": \"step4\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_care_start\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Scan Carestart Exp. Date\",\n" +
            "        \"top_margin\": \"30dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"next\": \"step3\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step3\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step6\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"expiration_date_reader\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"expiration_date_capture\",\n" +
            "        \"expired_page_address\": \"step5\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please enter the RDT ID\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step4\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step6\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"qr_code_reader\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"barcode\",\n" +
            "        \"barcode_type\": \"qrcode\",\n" +
            "        \"hint\": \"RDT ID *\",\n" +
            "        \"scanButtonText\": \"Scan QR Code\",\n" +
            "        \"value\": \"0\",\n" +
            "        \"rdt_id_lbl_addresses\": \"step6:lbl_rdt_id, step7:lbl_rdt_id, step8:lbl_rdt_id, step17:lbl_rdt_id, step18:lbl_rdt_id\",\n" +
            "        \"rdt_id_address\": \"step16:rdt_id\",\n" +
            "        \"expiration_date_address\": \"step3:expiration_date_reader\",\n" +
            "        \"expired_page_address\": \"step5\",\n" +
            "        \"v_numeric\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please enter a valid ID\"\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please enter the RDT ID\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step5\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step2\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next_label\": \"OK\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_expired\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\",\n" +
            "        \"text\": \"RDT has expired\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_use_new_test\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"8sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\",\n" +
            "        \"text\": \"Use new test and rescan\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step6\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step7\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE TO MICROSCOPY\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_record_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Write this number on back of RDT.\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step7\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step8\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"MICROSCOPY SLIDE COMPLETE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_microscopy_slide_instruction\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Collect Thick and Thin Bloodsmear on Microscopy Slide\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_microscopy_slide\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Label slide with:\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step8\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step9\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"BLOT PAPER COMPLETE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_blot_paper_instructions\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Saturate the Filter Paper Bloodspot Circle\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_blot_paper\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Place Bloodspot in a labeled tube with ID number displayed. Do Not Close top of Bloodspot until dry.\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step9\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step10\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"next\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"key\": \"illustration_text_description\",\n" +
            "        \"type\": \"image_view\",\n" +
            "        \"text\": \"Collect the blood sample (5㎕) using a pipette provided or micropipette.\",\n" +
            "        \"label_text_size\": \"18sp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"image_file\": \"collect_blood_sample.jpg\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step10\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"next\": \"step11\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"next\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"key\": \"add_blood\",\n" +
            "        \"type\": \"image_view\",\n" +
            "        \"text\": \"Add 5 ㎕ of whole blood into the 'S' well.\",\n" +
            "        \"label_text_size\": \"18sp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"image_file\": \"add_blood.jpg\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step11\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"next\": \"step12\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"next\",\n" +
            "    \"next_label\": \"START 20 MINUTE TIMER\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"key\": \"add_buffer_solution\",\n" +
            "        \"type\": \"image_view\",\n" +
            "        \"text\": \"Add 60 ㎕ assay buffer solution (3 drops for vial type or 2 drops for bottle type) into the \\\"A\\\" well.\\nStart a timer.\",\n" +
            "        \"label_text_size\": \"18sp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"image_file\": \"add_buffer_solution.jpg\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step12\": {\n" +
            "    \"title\": \"RDT Timer\",\n" +
            "    \"next\": \"step13\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"next\",\n" +
            "    \"next_label\": \"TAKE IMAGE OF RDT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"countdown_timer\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"timer\",\n" +
            "        \"type\": \"countdown_timer\",\n" +
            "        \"label\": \"Read results in 20 minutes.\",\n" +
            "        \"label_text_size\": \"8sp\",\n" +
            "        \"label_text_color\": \"#525252\",\n" +
            "        \"countdown_time_unit\": \"minutes\",\n" +
            "        \"countdown_time_value\": \"20\",\n" +
            "        \"countdown_interval\": \"1\",\n" +
            "        \"progressbar_background_color\": \"#e2e2e2\",\n" +
            "        \"progressbar_color\": \"#E9D61E\",\n" +
            "        \"progressbar_text_color\": \"#505050\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step13\": {\n" +
            "    \"title\": \"RDT Result Ready\",\n" +
            "    \"next\": \"step15\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"next\",\n" +
            "    \"next_label\": \"TAKE IMAGE OF RDT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"countdown_timer_results_ready\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"timer\",\n" +
            "        \"type\": \"countdown_timer\",\n" +
            "        \"label\": \"Read results within 10 minutes.\",\n" +
            "        \"label_text_size\": \"8sp\",\n" +
            "        \"label_text_color\": \"#4b4b4b\",\n" +
            "        \"countdown_time_unit\": \"minutes\",\n" +
            "        \"countdown_time_value\": \"10\",\n" +
            "        \"countdown_interval\": \"1\",\n" +
            "        \"progressbar_background_color\": \"#e2e2e2\",\n" +
            "        \"progressbar_color\": \"#3EBB22\",\n" +
            "        \"progressbar_text_color\": \"#505050\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step14\": {\n" +
            "    \"title\": \"Carestart RDT\",\n" +
            "    \"next\": \"step15\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"TAKE IMAGE OF RDT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_conduct_carestart\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Conduct Carestart RDT\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_conduct_carestart\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Take image of RDT once test is complete\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step15\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"next\": \"step16\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"rdt_capture\",\n" +
            "        \"carestart_rdt_prev\": \"step14\",\n" +
            "        \"image_id_address\": \"step15:rdt_capture\",\n" +
            "        \"image_timestamp_address\": \"step16:time_img_saved\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step16\": {\n" +
            "    \"title\": \"RDT Test Result\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"SAVE RDT RESULT\",\n" +
            "    \"next\": \"step17\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"rdt_result\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"label\": \"Select RDT reader result\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"positive\",\n" +
            "            \"text\": \"positive\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"negative\",\n" +
            "            \"text\": \"negative\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"invalid\",\n" +
            "            \"text\": \"invalid\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"value\": \"negative\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Please select the RDT reader result\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"time_img_saved\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"timer\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\",\n" +
            "        \"value\": \"834912438\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step17\": {\n" +
            "    \"title\": \"Confirm Labels\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step18\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONFIRM\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_confirm_labels\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Ensure Microscopy slide, Bloodspot and RDT labels are\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step18\": {\n" +
            "    \"title\": \"Sterile Storage\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"submit\",\n" +
            "    \"submit_label\": \"END TEST\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_confirm_labels\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"text\": \"Place RDT, Blood spot and Microscopy slide in a provided Sterile Storage and affix\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    @Before
    public void setUp() {
        formUtils = new RDTJsonFormUtils();
    }

    @Test
    public void testPrePopulateFormFieldsShouldPopulateCorrectValues() throws JSONException, JsonFormMissingStepCountException {
        JSONObject formObject = new JSONObject(RDT_TEST_JSON_FORM);
        Patient patient = new Patient("patient", "female", "entity_id");
        formUtils.prePopulateFormFields(formObject, patient, "rdt_id", 8);

        boolean populatedLblRDTId = false;
        boolean populatedRDTId = false;
        boolean populatedPatientName = false;
        boolean populatedPatientSex = false;
        JSONArray fields = getMultiStepFormFields(formObject);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            // test rdt id labels are populated
            if (Constants.Form.LBL_RDT_ID.equals(field.getString(KEY))) {
               assertEquals(field.getString("text"), "RDT ID: " + "rdt_id");
               populatedLblRDTId = true;
            }
            // test rdt id is populated
            if (RDT_ID.equals(field.getString(KEY))) {
                assertEquals(field.getString(VALUE), "rdt_id");
                populatedRDTId = true;
            }
            // test patient fields are populated
            if (Constants.Form.LBL_PATIENT_NAME.equals(field.getString(KEY))) {
                assertEquals(field.getString("text"), patient.getPatientName());
                populatedPatientName = true;
            } else if (Constants.Form.LBL_PATIENT_GENDER_AND_ID.equals(field.getString(KEY))) {
                assertEquals(field.getString("text"), patient.getPatientSex() + BULLET_DOT + "ID: " + patient.getBaseEntityId());
                populatedPatientSex = true;
            }
        }
        assertTrue(populatedRDTId && populatedPatientName && populatedLblRDTId && populatedPatientSex);
    }
}
