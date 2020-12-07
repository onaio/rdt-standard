package io.ona.rdt.util;

import android.content.Context;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.JsonFormUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.ona.rdt.R;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.robolectric.widget.WidgetFactoryRobolectricTest;
import io.ona.rdt.shadow.DeviceDefinitionProcessorShadow;
import io.ona.rdt.widget.validator.CovidImageViewFactory;

import static io.ona.rdt.util.CovidConstants.FormFields.COVID_SAMPLE_ID;
import static org.junit.Assert.assertEquals;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidRDTJsonFormUtilsTest extends BaseRDTJsonFormUtilsTest {

    private static final String ANM_PREFERRED_NAME = "indtester1";

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
            "        \"text\": \"Obtain specimen using approved methods.\"\n" +
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
            "        \"text\": \"Specify VTM brand?\",\n" +
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
            "        \"key\": \"manufacturing_lot_number_lbl\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"VTM lot number\",\n" +
            "        \"text_color\": \"#000000\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"manufacturing_lot_number\",\n" +
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
            "        \"text\": \"Doctor's phone number?\",\n" +
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
            "        \"text\": \"GSI sample ID?\",\n" +
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
            "        \"text\": \"Hospital sample ID?\",\n" +
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
            "\"step8\": {\n" +
            "    \"title\": \"Patient Information\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step9\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_patient_confirmation\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Please confirm the information\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"patient_info_unique_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        },\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"unique_id\",\n" +
            "            \"text\": \"Unique Id: \"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"patient_info_name\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        },\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"patient_name\",\n" +
            "            \"text\": \"Patient Name: \"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"patient_info_dob\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"This field is required\"\n" +
            "        },\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"patient_dob\",\n" +
            "            \"text\": \"Patient Dob: \"\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }," +
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

    private static final String MOCK_LOCATION_TREE_JSON = "{\"locationsHierarchy\":{\"map\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"children\":{\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 1\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 2\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 2\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"},\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\":{\"id\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Location 3\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Location 3\",\"parentLocation\":{\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"},\"parent\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\"}},\"id\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"label\":\"Indonesia Division 1\",\"node\":{\"attributes\":{\"geographicLevel\":0.0},\"locationId\":\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"name\":\"Indonesia Division 1\",\"serverVersion\":0,\"voided\":false,\"type\":\"Location\"}}},\"parentChildren\":{\"1c7ba751-35e8-4b46-9e53-3cb8fd193697\":[\"2c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"3c7ba751-35e8-4b46-9e53-3cb8fd193697\",\"4c7ba751-35e8-4b46-9e53-3cb8fd193697\"]}}}";

    @Override
    public void setUp() throws Exception {
        super.setUp();
        AllSharedPreferences allSharedPreferences = RDTApplication.getInstance().getContext().allSharedPreferences();
        allSharedPreferences.updateANMUserName("anm_id");
        allSharedPreferences.updateANMPreferredName(allSharedPreferences.fetchRegisteredANM(), ANM_PREFERRED_NAME);
        allSharedPreferences.savePreference(CovidConstants.Preference.LOCATION_TREE, MOCK_LOCATION_TREE_JSON);
    }

    @After
    public void tearDown() {
        RDTApplication.getInstance().getStepStateConfiguration().setStepStateObj(null);
        DeviceDefinitionProcessorShadow.setJSONObject(null);
        RDTJsonFormUtilsShadow.setJsonObject(null);
    }

    @Config(shadows = {DeviceDefinitionProcessorShadow.class})
    @Test
    public void testPrePopulateRDTFormFieldsShouldPopulateAvailableRDTs() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormUtils.KEY, Constants.RDTType.RDT_TYPE);
        getFormUtils().prePopulateRDTFormFields(RuntimeEnvironment.application, jsonObject, "");
        Assert.assertEquals(Utils.createOptionsBlock(CovidRDTJsonFormUtils.appendOtherOption(DeviceDefinitionProcessorShadow.getDeviceIdToNameMap()), "", "", "").toString(),
                jsonObject.get(JsonFormConstants.OPTIONS_FIELD_NAME).toString());
    }

    @Config(shadows = {DeviceDefinitionProcessorShadow.class, RDTJsonFormUtilsShadow.class})
    @Test
    public void testPopulateRDTDetailsConfirmationPageShouldCorrectlyPopulateDetails() throws Exception {
        JSONObject jsonObject = new JSONObject();
        RDTJsonFormActivity jsonFormActivity = WidgetFactoryRobolectricTest.getRDTJsonFormActivity();
        Mockito.doNothing().when(jsonFormActivity).writeValue(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());

        JsonFormFragment formFragment = Mockito.mock(JsonFormFragment.class);
        final String TEST_STEP = "test_step";
        final String DEVICE_ID = "device_id";


        JSONObject deviceDetailsWidget = new JSONObject(new HashMap<String, JSONArray>() {
            {
                put(JsonFormConstants.OPTIONS_FIELD_NAME, new JSONArray("[{}]"));
            }
        });
        RDTJsonFormUtilsShadow.setJsonObject(deviceDetailsWidget);

        JSONObject deviceConfig = new JSONObject(new HashMap<String, String>() {
            {
                put(CovidConstants.FHIRResource.REF_IMG, CovidConstants.FHIRResource.REF_IMG);
            }
        });
        DeviceDefinitionProcessorShadow.setJSONObject(deviceConfig);

        JSONObject stepStateConfig = new JSONObject();
        stepStateConfig.put(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE, CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE);
        RDTApplication.getInstance().getStepStateConfiguration().setStepStateObj(stepStateConfig);

        WidgetArgs widgetArgs = new WidgetArgs().withJsonObject(jsonObject).withStepName(TEST_STEP)
                .withContext(jsonFormActivity).withFormFragment(formFragment);

        getCovidFormUtils().populateRDTDetailsConfirmationPage(widgetArgs, DEVICE_ID);

        verifyRDTDetailsConfirmationPageIsPopulated(jsonFormActivity, deviceDetailsWidget);
        Mockito.verify(jsonFormActivity).writeValue(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE, CovidConstants.FormFields.RDT_CONFIG,
                deviceConfig.toString(), "", "", "", false);

        jsonFormActivity.finish();
    }

    private void verifyRDTDetailsConfirmationPageIsPopulated(RDTJsonFormActivity jsonFormActivity, JSONObject deviceDetailsWidget) throws JSONException {

        String deviceDetails = ReflectionHelpers.callInstanceMethod(new CovidRDTJsonFormUtils(), "getFormattedRDTDetails",
                ReflectionHelpers.ClassParameter.from(Context.class, RuntimeEnvironment.application),
                ReflectionHelpers.ClassParameter.from(String.class, DeviceDefinitionProcessorShadow.MANUFACTURER),
                ReflectionHelpers.ClassParameter.from(String.class, DeviceDefinitionProcessorShadow.DEVICE_NAME));

        Assert.assertEquals(deviceDetails, deviceDetailsWidget.getString(JsonFormConstants.TEXT));
        Assert.assertEquals(CovidConstants.FHIRResource.REF_IMG, deviceDetailsWidget.getString(CovidImageViewFactory.BASE64_ENCODED_IMG));

        Mockito.verify(jsonFormActivity).writeValue(ArgumentMatchers.eq(CovidConstants.Step.COVID_DEVICE_DETAILS_CONFIRMATION_PAGE),
                ArgumentMatchers.eq(CovidConstants.FormFields.RDT_CONFIG), ArgumentMatchers.eq(DeviceDefinitionProcessorShadow.getJsonObject().toString()),
                ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyBoolean());
    }

    protected void assertAllFieldsArePopulated(int numOfPopulatedFields) {
        assertEquals(10, numOfPopulatedFields);
    }

    @Override
    protected int assertFieldsArePopulated(JSONObject field, Patient patient, int numOfPopulatedFields) throws JSONException {
        if (CovidConstants.FormFields.LBL_RESPIRATORY_SAMPLE_ID.equals(field.getString(KEY))) {
            // pre-populate respiratory sample id labels
            Utils.updateLocale(RuntimeEnvironment.application);
            assertEquals(RuntimeEnvironment.application.getString(R.string.sample_id_prompt) + UNIQUE_ID, field.getString("text"));
            numOfPopulatedFields++;
        } else if (COVID_SAMPLE_ID.equals(field.getString(KEY))) {
            // pre-populate respiratory sample id field
            assertEquals(field.getString(VALUE), UNIQUE_ID);
            numOfPopulatedFields++;
        } else if (CovidConstants.FormFields.PATIENT_INFO_UNIQUE_ID.equals(field.getString(JsonFormConstants.KEY))) {
            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
            if (options.length() > 0) {
                JSONObject childObject = options.getJSONObject(0);
                assertEquals(childObject.getString(JsonFormConstants.TEXT), "unique_id");
                numOfPopulatedFields++;
            }
        } else if (CovidConstants.FormFields.PATIENT_INFO_NAME.equals(field.getString(JsonFormConstants.KEY))) {
            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
            if (options.length() > 0) {
                JSONObject childObject = options.getJSONObject(0);
                assertEquals(childObject.getString(JsonFormConstants.TEXT), "patient");
                numOfPopulatedFields++;
            }
        } else if (CovidConstants.FormFields.PATIENT_INFO_DOB.equals(field.getString(JsonFormConstants.KEY))) {
            JSONArray options = field.getJSONArray(JsonFormConstants.OPTIONS_FIELD_NAME);
            if (options.length() > 0) {
                JSONObject childObject = options.getJSONObject(0);
                assertEquals(childObject.getString(JsonFormConstants.TEXT), "01-09-2020");
                numOfPopulatedFields++;
            }
        } else if (CovidConstants.FormFields.SAMPLER_NAME.equals(field.getString(JsonFormConstants.KEY))) {
            assertEquals(field.getString(VALUE), ANM_PREFERRED_NAME);
            numOfPopulatedFields++;
        } else if (CovidConstants.FormFields.SENDER_NAME.equals(field.getString(JsonFormConstants.KEY))) {
            assertEquals(field.getString(VALUE), ANM_PREFERRED_NAME);
            numOfPopulatedFields++;
        } else if (CovidRDTJsonFormUtils.FACILITY_SET.contains(field.getString(JsonFormConstants.KEY))) {
            JSONArray jsonArray = new JSONArray(field.getString(JsonFormConstants.OPTIONS_FIELD_NAME));
            final int four = 4;
            assertEquals(four, jsonArray.length());
            for (int i = 1; i < jsonArray.length(); i++) {
                assertEquals("Indonesia Location " + i, jsonArray.getJSONObject(i - 1).getString(JsonFormConstants.TEXT));
            }
            assertEquals("Other", jsonArray.getJSONObject(jsonArray.length() - 1).getString(JsonFormConstants.TEXT));
            numOfPopulatedFields++;
        }

        return numOfPopulatedFields;
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return new CovidRDTJsonFormUtils();
    }

    private CovidRDTJsonFormUtils getCovidFormUtils() {
        return (CovidRDTJsonFormUtils) getFormUtils();
    }

    @Override
    protected List<String> getFormsToPrepopulate() {
        return Arrays.asList(CovidConstants.Form.SAMPLE_COLLECTION_FORM,
                CovidConstants.Form.SAMPLE_DELIVERY_DETAILS_FORM,
                CovidConstants.Form.COVID_RDT_TEST_FORM,
                CovidConstants.Form.PATIENT_DIAGNOSTICS_FORM);
    }

    @Override
    protected String getMockForm() {
        return SAMPLE_COLLECTION_JSON_FORM;
    }

    @Override
    protected String getPatientRegistrationEvent() {
        return CovidConstants.Encounter.COVID_PATIENT_REGISTRATION;
    }
}
