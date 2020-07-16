package io.ona.rdt.util;

import org.json.JSONException;
import org.json.JSONObject;

import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static org.junit.Assert.assertEquals;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidRDTJsonFormUtilsTest extends BaseRDTJsonFormUtilsTest {

    public static final String SAMPLE_COLLECTION_JSON_FORM = "{\n" +
            "  \"count\": \"9\",\n" +
            "  \"encounter_type\": \"sample_collection\",\n" +
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
            "    \"title\": \"Sample collection\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step3\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_collect_respiratory_sample\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Collect sample using appropriate methods\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step2\": {\n" +
            "    \"title\": \"Sample collection\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step1\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next_label\": \"OK\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_sample_expired\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"text\": \"Sample is expired!\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_collect_new_sample\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"center_label\": \"true\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"text\": \"Please collect another sample.\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step3\": {\n" +
            "    \"title\": \"Sample collection\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next\": \"step4\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"sampler_name_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Name of sampler\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"sampler_name\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"edit_type\": \"name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"sample_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"label\": \"Where is the assessment taking place?\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"nasopharyngeal\",\n" +
            "            \"text\": \"Nasopharyngeal\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"oropharyngeal\",\n" +
            "            \"text\": \"Oropharyngeal\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"sputum\",\n" +
            "            \"text\": \"Sputum\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"serum\",\n" +
            "            \"text\": \"Serum / Serology Sputum\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"other\",\n" +
            "            \"text\": \"Other\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_sample_type_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Specify sample type\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_sample_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"vtm_brand\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"label\": \"Brand of VTM?\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"copan\",\n" +
            "            \"text\": \"Copan\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"zeesan\",\n" +
            "            \"text\": \"Zeesan\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"nest\",\n" +
            "            \"text\": \"NEST\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"lmk\",\n" +
            "            \"text\": \"LMK\"\n" +
            "          },\n" +
            "      {\n" +
            "        \"key\": \"fkui\",\n" +
            "        \"text\": \"FKUI\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"i3l\",\n" +
            "        \"text\": \"i3L\"\n" +
            "      },\n" +
            "          {\n" +
            "            \"key\": \"other\",\n" +
            "            \"text\": \"Other\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_vtm_brand_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Specify VTM brand\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_vtm_brand\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"sample_expiration_date\",\n" +
            "        \"type\": \"date_picker\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"hint\": \"VTM expiration date\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"sample_lot_number_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"VTM lot number\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"sample_lot_number\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"edit_type\": \"name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"health_facility_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"label\": \"Referral Health Facility?\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"hospital\",\n" +
            "            \"text\": \"Hospital\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"clinic\",\n" +
            "            \"text\": \"Clinic\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"health_facility\",\n" +
            "            \"text\": \"Health Facility\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"other\",\n" +
            "            \"text\": \"Other\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_health_facility_type_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Other facility type\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_health_facility_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"name_of_primary_care_facility\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"label\": \"Name of Primary Care Facility?\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"jenis_fasyankes\",\n" +
            "            \"text\": \"Jenis Fasyankes\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"dinas_kesehatan\",\n" +
            "            \"text\": \"Dinas Kesehatan\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"alamat_fasyankes\",\n" +
            "            \"text\": \"Alamat Fasyankes\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"other\",\n" +
            "            \"text\": \"Other\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_primary_care_facility_type_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Other primary care facility\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"other_primary_care_facility_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"relevance\": {\n" +
            "          \"rules-engine\": {\n" +
            "            \"ex-rules\": {\n" +
            "              \"rules-file\": \"sample-collection-form-relevance-rules.yml\"\n" +
            "            }\n" +
            "          }\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"doctor_name_label\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Doctor's name\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"doctor_name\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"doctor_phone_number_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Doctor's phone number\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"doctor_phone_number\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"false\",\n" +
            "          \"err\": \"\"\n" +
            "        },\n" +
            "        \"v_numeric\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Value must be numeric\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"booking_date\",\n" +
            "        \"type\": \"date_picker\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"hint\": \"Testing booking slot\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"gsi_sample_number_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"GSI sample ID\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"gsi_sample_number\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"edit_type\": \"name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"hospital_sample_number_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Hospital sample ID\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"hospital_sample_number\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"edit_text\",\n" +
            "        \"edit_type\": \"name\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"patient_health_status\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"label\": \"Patient health status?\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"ill\",\n" +
            "            \"text\": \"Ill\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"undergoing_care\",\n" +
            "            \"text\": \"Undergoing care\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"recovered\",\n" +
            "            \"text\": \"Recovered\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"dead\",\n" +
            "            \"text\": \"Dead\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"no_covid_detected\",\n" +
            "            \"text\": \"No Covid-19 Detected\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step4\": {\n" +
            "    \"title\": \"Store specimen\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step5\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_store_respiratory_specimen\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Safely store Respiratory Specimen in provided transport tube.\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step5\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_scan_respiratory_specimen_barcode\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Scan respiratory specimen barcode\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"40dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_affix_respiratory_specimen_label\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Manually affix label\",\n" +
            "        \"top_margin\": \"40dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step6\": {\n" +
            "    \"title\": \"Sample collection\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step8\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"qr_code_reader\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"one_scan_covid_barcode_reader\",\n" +
            "        \"barcode_type\": \"qrcode\",\n" +
            "        \"hint\": \"\",\n" +
            "        \"scanButtonText\": \"\",\n" +
            "        \"value\": \"0\",\n" +
            "        \"v_numeric\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"\"\n" +
            "        },\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"unique_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lot_no\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"exp_date\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"gtin\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"temp_sensor\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step7\": {\n" +
            "    \"title\": \"Sample collection\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next\": \"step8\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_affix_respiratory_sample_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"13sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Affix Label or record ID on transport tube\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_respiratory_sample_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"covid_sample_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step8\": {\n" +
            "    \"title\": \"Store specimen for courier\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step9\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_store_specimen_for_courier\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Store the respiratory sample appropriately until they have been picked up by the courier\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step9\": {\n" +
            "    \"title\": \"Test complete\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_type\": \"submit\",\n" +
            "    \"submit_label\": \"SAVE AND EXIT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_test_complete\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Sample collection for the patient is complete\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    protected void assertAllFieldsArePopulated(int numOfPopulatedFields) {
        assertEquals(2, numOfPopulatedFields);
    }

    @Override
    protected int assertFieldsArePopulated(JSONObject field, Patient patient, int numOfPopulatedFields) throws JSONException {
        if (CovidConstants.FormFields.LBL_RESPIRATORY_SAMPLE_ID.equals(field.getString(KEY))) {
            // pre-populate respiratory sample id labels
            assertEquals(field.getString("text"), "Sample ID: " + UNIQUE_ID);
            numOfPopulatedFields++;
        } else if (COVID_SAMPLE_ID.equals(field.getString(KEY))) {
            // pre-populate respiratory sample id field
            assertEquals(field.getString(VALUE), UNIQUE_ID);
            numOfPopulatedFields++;
        }
        return numOfPopulatedFields;
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return new CovidRDTJsonFormUtils();
    }

    @Override
    protected String getFormToPrepopulate() {
        return SAMPLE_COLLECTION_FORM;
    }

    @Override
    protected String getMockForm() {
        return SAMPLE_COLLECTION_JSON_FORM;
    }
}
