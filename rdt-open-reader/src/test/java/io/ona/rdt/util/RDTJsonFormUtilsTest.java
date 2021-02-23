package io.ona.rdt.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.domain.ProfileImage;
import org.smartregister.domain.UniqueId;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.ImageRepository;
import org.smartregister.util.JsonFormUtils;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import androidx.core.util.Pair;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.callback.OnUniqueIdsFetchedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.Patient;
import io.ona.rdt.robolectric.shadow.ImageUtilShadow;

import static io.ona.rdt.TestUtils.cleanUpFiles;
import static io.ona.rdt.TestUtils.getTestFilePath;
import static io.ona.rdt.util.Constants.Config.MULTI_VERSION;
import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.Constants.Format.BULLET_DOT;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMAGE;
import static io.ona.rdt.util.Constants.Test.FULL_IMAGE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.spy;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class RDTJsonFormUtilsTest extends BaseRDTJsonFormUtilsTest {

    public static final String RDT_TEST_JSON_FORM = "{\n" +
            "  \"count\": \"20\",\n" +
            "  \"encounter_type\": \"covid_rdt_test\",\n" +
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
            "    \"title\": \"Temukan lokasi\",\n" +
            "    \"next\": \"step2\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"gps\",\n" +
            "        \"type\": \"gps\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step2\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step3\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
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
            "  \"step3\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_conduct_rdt\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Conduct RDT\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_skip_rdt_test\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Skip RDT test\",\n" +
            "        \"top_margin\": \"30dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step4\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
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
            "        \"key\": \"lbl_scan_barcode\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Scan barcode\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_enter_rdt_manually\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Enter RDT manually\",\n" +
            "        \"top_margin\": \"30dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step5\": {\n" +
            "    \"title\": \"Record RDT information\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step6\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_manufacturer\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Enter manufacturer name\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"9sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"bottom_margin\": \"50dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_manufacturer\",\n" +
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
            "        \"key\": \"lbl_rdt_test_name\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Enter RDT name\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"9sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"bottom_margin\": \"50dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_test_name\",\n" +
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
            "        \"key\": \"lbl_rdt_batch_id\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Enter RDT batch ID\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"9sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"bottom_margin\": \"50dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_batch_id\",\n" +
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
            "        \"key\": \"lbl_record_expiration_date\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Catat Tanggal Kedaluwarsa\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"9sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"bottom_margin\": \"50dp\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"manual_expiration_date\",\n" +
            "        \"type\": \"date_picker\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"hint\": \"Tanggal kadaluarsa\",\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"Please specify the expiration date\"\n" +
            "        }\n" +
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
            "  \"step6\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step9\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_handle_rdt\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"14sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Record ID on RDT\"\n" +
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
            "  \"step7\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step9\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next_label\": \"CONDUCT RDT TEST\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_one_scan_widget_rdt\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"OneScan widget will go here\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step8\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step3\",\n" +
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
            "        \"text\": \"RDT telah kadaluwarsa\"\n" +
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
            "        \"text\": \"Gunakan tes baru dan pindai ulang\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step9\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step10\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CAPTURE RDT\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_rdt_instructions\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Conduct the RDT according to manufacturing guidelines\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step10\": {\n" +
            "    \"title\": \"RDT\",\n" +
            "    \"next\": \"step11\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"rdt_capture\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_top_line_reading\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_middle_line_reading\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_bottom_line_reading\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_duration\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_type\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"flash_on\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"cassette_boundary\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"cropped_img_id\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"time_img_saved\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"is_manual_capture\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step11\": {\n" +
            "    \"title\": \"Hasil tes RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"SIMPAN HASIL RDT\",\n" +
            "    \"next\": \"step12\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"chw_result\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"native_radio\",\n" +
            "        \"label\": \"Pilih hasil pembaca RDT\",\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"positive\",\n" +
            "            \"text\": \"positif\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"negative\",\n" +
            "            \"text\": \"negatif\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"invalid\",\n" +
            "            \"text\": \"tidak valid\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": \"true\",\n" +
            "          \"err\": \"Silakan pilih hasil pembaca RDT\"\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"parasite_type\",\n" +
            "        \"label\": \"Positif untuk:\",\n" +
            "        \"type\": \"check_box\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"combine_checkbox_option_values\": true,\n" +
            "        \"options\": [\n" +
            "          {\n" +
            "            \"key\": \"igm_positive\",\n" +
            "            \"text\": \"IgM\",\n" +
            "            \"value\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"key\": \"igg_positive\",\n" +
            "            \"text\": \"IgG\",\n" +
            "            \"value\": false\n" +
            "          }\n" +
            "        ],\n" +
            "        \"v_required\": {\n" +
            "          \"value\": true,\n" +
            "          \"err\": \"Please specify if positive for pv, pf or both\"\n" +
            "        },\n" +
            "        \"relevance\": {\n" +
            "          \"step11:chw_result\": {\n" +
            "            \"type\": \"string\",\n" +
            "            \"ex\": \"equalTo(., \\\"positive\\\")\"\n" +
            "          }\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step12\": {\n" +
            "    \"title\": \"Handle RDT\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step13\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"CONTINUE\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_handle_rdt\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"10sp\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"text\": \"Store or dispose of RDT appropriately according to established protocol\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step13\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_collect_respiratory_sample\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Collect respiratory sample\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_skip_respiratory_sample_collection\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Skip respiratory sample collection\",\n" +
            "        \"top_margin\": \"30dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step14\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step15\",\n" +
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
            "        \"text\": \"Collect respiratory specimen using approved methods\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step15\": {\n" +
            "    \"title\": \"Store specimen\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step16\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
            "    \"next_label\": \"KONFIRMASI\",\n" +
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
            "  \"step16\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_scan_respiratory_specimen_barcode\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Scan respiratory specimen barcode\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"lbl_affix_respiratory_specimen_label\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"Manually affix label\",\n" +
            "        \"top_margin\": \"30dp\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"has_drawable_end\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step17\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next\": \"step19\",\n" +
            "    \"bottom_navigation_orientation\": \"vertical\",\n" +
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
            "  \"step18\": {\n" +
            "    \"title\": \"Respiratory sample\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step19\",\n" +
            "    \"bottom_navigation\": \"true\",\n" +
            "    \"next_label\": \"CONDUCT RDT TEST\",\n" +
            "    \"fields\": [\n" +
            "      {\n" +
            "        \"key\": \"lbl_one_scan_widget_respiratory_sample\",\n" +
            "        \"type\": \"label\",\n" +
            "        \"text\": \"OneScan widget will go here\",\n" +
            "        \"text_color\": \"#000000\",\n" +
            "        \"text_size\": \"12sp\",\n" +
            "        \"label_text_style\": \"bold\",\n" +
            "        \"top_margin\": \"15dp\",\n" +
            "        \"has_bg\": true,\n" +
            "        \"bg_color\": \"#ffffff\"\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"step19\": {\n" +
            "    \"title\": \"Store specimen for courier\",\n" +
            "    \"display_back_button\": \"true\",\n" +
            "    \"next\": \"step20\",\n" +
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
            "  \"step20\": {\n" +
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
            "        \"text\": \"Diagnostic test collection for the patient has concluded. Using approved protocols, inform the patient of their results and followup procedures\"\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    private static JSONObject RDT_TEST_JSON_FORM_OBJ;

    @Captor
    private ArgumentCaptor<List<UniqueId>> uniqueIdsArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        RDT_TEST_JSON_FORM_OBJ = new JSONObject(RDT_TEST_JSON_FORM);
    }

    @Test
    public void testGetUniqueIDsShouldGetIDs() {
        OnUniqueIdsFetchedCallback uniqueIdsFetchedCallback = Mockito.mock(OnUniqueIdsFetchedCallback.class);
        FormLaunchArgs formLaunchArgs = Mockito.mock(FormLaunchArgs.class);
        getFormUtils().getNextUniqueIds(formLaunchArgs, uniqueIdsFetchedCallback, 2);

        Mockito.verify(uniqueIdsFetchedCallback).onUniqueIdsFetched(eq(formLaunchArgs), uniqueIdsArgumentCaptor.capture());

        List<UniqueId> uniqueIds = uniqueIdsArgumentCaptor.getValue();
        assertEquals("openmrsID1", uniqueIds.get(0).getOpenmrsId());
        assertEquals("openmrsID1", uniqueIds.get(1).getOpenmrsId());
    }

    @Test
    public void testGetFormJsonObjectShouldGetFormJsonObject() throws Exception {
        JSONObject jsonObject = getFormUtils().getFormJsonObject("form_name.json",
                RuntimeEnvironment.application);
        assertEquals(new JSONObject(getMockForm()).toString(), jsonObject.toString());
    }

    @Test
    public void testWriteImageToDiskShouldSuccessfullyWriteImageToDisk() throws Exception {
        Bitmap image = Mockito.mock(Bitmap.class);
        Pair<Boolean, String> result = Whitebox.invokeMethod(getFormUtils(), "writeImageToDisk", getTestFilePath(), image, Mockito.mock(Context.class));
        final int quality = 100;
        Mockito.verify(image).compress(eq(Bitmap.CompressFormat.JPEG), eq(quality), ArgumentMatchers.any(OutputStream.class));
        cleanUpFiles(result.second);
    }

    @Test
    public void testSaveImgDetailsShouldSaveCorrectImgDetails() throws Exception {
        ProfileImage profileImage = spy(new ProfileImage("image_id", "anm_id", "entity_id", "content_type", "file_path", "sync_status", "file_category"));
        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withProviderId("anm_id")
                .withBaseEntityId("entity_id");

        Whitebox.invokeMethod(getFormUtils(), "saveImgDetails", "file_path", parcelableImageMetadata, profileImage);
        Mockito.verify(profileImage).setAnmId(eq("anm_id"));
        Mockito.verify(profileImage).setEntityID(eq("entity_id"));
        Mockito.verify(profileImage).setFilepath(eq("file_path"));
        Mockito.verify(profileImage).setFilecategory(eq(MULTI_VERSION));
        Mockito.verify(profileImage).setSyncStatus(eq(ImageRepository.TYPE_Unsynced));
        Mockito.verify(RDTApplication.getInstance().getContext().imageRepository()).add(eq(profileImage));
    }

    @Test
    public void testSaveStaticImagesToDiskShouldReturnIfMissingInformation() throws Exception {
        OnImageSavedCallback onImageSavedCallback = Mockito.mock(OnImageSavedCallback.class);
        CompositeImage compositeImage = Mockito.mock(CompositeImage.class);
        Mockito.doReturn(Mockito.mock(ParcelableImageMetadata.class)).when(compositeImage).getParcelableImageMetadata();
        getFormUtils().saveStaticImagesToDisk(Mockito.mock(Context.class), compositeImage, onImageSavedCallback);
        Mockito.verify(onImageSavedCallback).onImageSaved(isNull());
    }

    @Test
    public void testSaveImageShouldSaveImage() {
        assertEquals(0, ImageUtilShadow.getMockCounter().getCount());

        ParcelableImageMetadata parcelableImageMetadata = Mockito.spy(new ParcelableImageMetadata());
        parcelableImageMetadata.withBaseEntityId("entity_id")
                .withProviderId("provider_id");

        CompositeImage compositeImage = new CompositeImage();
        compositeImage.withCroppedImage(Mockito.mock(Bitmap.class))
                .withFullImage(Mockito.mock(Bitmap.class))
                .withParcelableImageMetadata(parcelableImageMetadata);

        OnImageSavedCallback onImageSavedCallback = Mockito.mock(OnImageSavedCallback.class);
        getFormUtils().saveStaticImagesToDisk(RuntimeEnvironment.application, compositeImage, onImageSavedCallback);

        // verify cropped and full image get a turn being saved
        Mockito.verify(parcelableImageMetadata).setImageToSave(eq(FULL_IMAGE));
        Mockito.verify(parcelableImageMetadata).setImageToSave(eq(CROPPED_IMAGE));

        // verify cropped image is saved
        Mockito.verify(parcelableImageMetadata).setCroppedImageId(ArgumentMatchers.any());
        Assert.assertNotNull(compositeImage.getCroppedImageFilePath());

        // verify full image is saved
        Mockito.verify(parcelableImageMetadata).setFullImageId(ArgumentMatchers.any());
        Mockito.verify(parcelableImageMetadata).setImageTimeStamp(anyLong());
        Assert.assertNotNull(compositeImage.getFullImageFilePath());

        // image should be saved to gallery for debug builds
        assertEquals(1, ImageUtilShadow.getMockCounter().getCount());

        Mockito.verify(onImageSavedCallback).onImageSaved(eq(compositeImage));
    }

    @Test
    public void testGetFileMD5HashShouldGetCorrectHash() throws Exception {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Whitebox.invokeMethod(getFormUtils(), "getFileMD5Hash", getTestFilePath() + "5de70b65-9597-4a0c-aac0-8c7b0ecef001.JPEG"));
    }

    @Test
    public void testGetFormTagShouldPopulateTagWithCorrectMetadata() {
        AllSettings settings = RDTApplication.getInstance().getContext().allSettings();
        settings.registerANM("provider");
        settings.getPreferences().saveDefaultLocalityId("provider", "location-id");
        settings.getPreferences().saveDefaultTeam("provider", "team");
        settings.getPreferences().saveDefaultTeamId("provider", "team-id");
        FormTag formTag = getFormUtils().getFormTag();
        assertEquals("provider", formTag.providerId);
        assertEquals("location-id", formTag.locationId);
        assertEquals("team-id", formTag.teamId);
        assertEquals("team", formTag.team);
    }

    @Test
    public void testGetFieldShouldGetAStepFieldIfItExists() throws JSONException {
        JSONObject mJsonObject = new JSONObject();
        JSONObject step = new JSONObject();
        JSONArray stepFields = new JSONArray();
        step.put(JsonFormConstants.FIELDS, stepFields);

        JsonFormActivity jsonFormActivity = Mockito.mock(JsonFormActivity.class);
        Mockito.doReturn(mJsonObject).when(jsonFormActivity).getmJSONObject();
        Mockito.doReturn(step).when(jsonFormActivity).getStep(eq(JsonFormUtils.STEP1));
        Assert.assertNull(getFormUtils().getField(JsonFormUtils.STEP1, JsonFormUtils.KEY, jsonFormActivity));

        JSONObject stepField = new JSONObject();
        stepField.put(JsonFormUtils.KEY, JsonFormUtils.KEY);
        stepFields.put(stepField);
        Assert.assertEquals(stepField, getFormUtils().getField(JsonFormUtils.STEP1, JsonFormUtils.KEY, jsonFormActivity));

        // return null for non-existent step
        Assert.assertNull(getFormUtils().getField("non-existent-step", JsonFormUtils.KEY, jsonFormActivity));
    }

    @Test
    public void testPrePopulateRDTPatientFieldsShouldNotPrepopulateForNullPatient() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonFormConstants.KEY, Constants.FormFields.LBL_PATIENT_NAME);
        ReflectionHelpers.callInstanceMethod(getFormUtils(), "prePopulateRDTPatientFields",
                ReflectionHelpers.ClassParameter.from(Patient.class, null),
                ReflectionHelpers.ClassParameter.from(JSONObject.class, jsonObject),
                ReflectionHelpers.ClassParameter.from(Context.class, null));
        Assert.assertNull(jsonObject.optString(JsonFormConstants.VALUE, null));
        Assert.assertNull(jsonObject.optString(JsonFormConstants.TEXT, null));
    }

    @Test
    public void testConvertBase64StrToBitmapShouldReturnNonNullBitmap() {
        Bitmap bitmap = getFormUtils().convertBase64StrToBitmap("/9j/4AAQSkZJRgABAQAASABIAAD/4QE0RXhpZgAATU0AKgAAAAgABgESAAMAAAABAAEAAAEaAAUAAAABAAAAVgEbAAUAAAABAAAAXgEoAAMAAAABAAIAAAEyAAIAAAAUAAAAZodpAAQAAAABAAAAegAAAAAAAABIAAAAAQAAAEgAAAABMjAyMDowMzoyNCAxMzoyNDo0NgAAC5AAAAcAAAAEMDIyMZADAAIAAAAUAAABBJAEAAIAAAAUAAABGJEBAAcAAAAEAQIDAJKRAAIAAAAEODE1AJKSAAIAAAAEODE1AKAAAAcAAAAEMDEwMKABAAMAAAABAAEAAKACAAQAAAABAAAC86ADAAQAAAABAAAA8KQGAAMAAAABAAAAAAAAAAAyMDIwOjAzOjI0IDEzOjI0OjQ2ADIwMjA6MDM6MjQgMTM6MjQ6NDYA/+EJ/Wh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8APD94cGFja2V0IGJlZ2luPSLvu78iIGlkPSJXNU0wTXBDZWhpSHpyZVN6TlRjemtjOWQiPz4gPHg6eG1wbWV0YSB4bWxuczp4PSJhZG9iZTpuczptZXRhLyIgeDp4bXB0az0iWE1QIENvcmUgNS40LjAiPiA8cmRmOlJERiB4bWxuczpyZGY9Imh0dHA6Ly93d3cudzMub3JnLzE5OTkvMDIvMjItcmRmLXN5bnRheC1ucyMiPiA8cmRmOkRlc2NyaXB0aW9uIHJkZjphYm91dD0iIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtbG5zOnBob3Rvc2hvcD0iaHR0cDovL25zLmFkb2JlLmNvbS9waG90b3Nob3AvMS4wLyIgeG1wOkNyZWF0ZURhdGU9IjIwMjAtMDMtMjRUMTM6MjQ6NDYuODE1IiB4bXA6TW9kaWZ5RGF0ZT0iMjAyMC0wMy0yNFQxMzoyNDo0NiIgcGhvdG9zaG9wOkRhdGVDcmVhdGVkPSIyMDIwLTAzLTI0VDEzOjI0OjQ2LjgxNSIvPiA8L3JkZjpSREY+IDwveDp4bXBtZXRhPiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAg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");
        Assert.assertNotNull(bitmap);
    }

    @Test
    public void testConvertBase64StrToBitmapShouldReturnNullBitmapForEmptyString() {
        Assert.assertNull(getFormUtils().convertBase64StrToBitmap(""));
        Assert.assertNull(getFormUtils().convertBase64StrToBitmap(null));
    }

    @Test
    public void testShowLocationServicesDialogShouldShowDialogForDisabledLocationServices() {
        Activity activity = Mockito.spy(Robolectric.buildActivity(Activity.class).create().get());

        RDTJsonFormUtils.showLocationServicesDialog(activity);

        AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
        Assert.assertNotNull(alertDialog);

        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
        Mockito.verify(activity).startActivity(ArgumentMatchers.any(Intent.class));

        Mockito.reset(activity);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).performClick();
        Mockito.verify(activity).finish();
        Assert.assertNotNull(ShadowToast.getLatestToast());

        activity.finish();
    }

    @Test
    public void testIsLocationServiceDisabledShouldReturnCorrectStatus() {
        Context context = Mockito.mock(Context.class);
        LocationManager locationManager = Mockito.mock(LocationManager.class);
        Mockito.doReturn(locationManager).when(context).getSystemService(Context.LOCATION_SERVICE);

        // if both network and gps disabled
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean isLocationServiceDisabled = RDTJsonFormUtils.isLocationServiceDisabled(context);

        Assert.assertTrue(isLocationServiceDisabled);

        // if gps enabled
        Mockito.doReturn(true).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = RDTJsonFormUtils.isLocationServiceDisabled(context);

        Assert.assertFalse(isLocationServiceDisabled);

        // if network available
        Mockito.doReturn(false).when(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER);
        Mockito.doReturn(true).when(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        isLocationServiceDisabled = RDTJsonFormUtils.isLocationServiceDisabled(context);

        Assert.assertFalse(isLocationServiceDisabled);
    }

    @Override
    protected String getMockForm() {
        return RDT_TEST_JSON_FORM;
    }

    @Override
    protected List<String> getFormsToPrepopulate() {
        return Arrays.asList(RDT_TEST_FORM);
    }

    @Override
    protected void assertAllFieldsArePopulated(int numOfPopulatedFields) {
        assertEquals(8, numOfPopulatedFields);
    }

    @Override
    protected int assertFieldsArePopulated(JSONObject field, Patient patient, int numOfPopulatedFields) throws JSONException {
        if (Constants.FormFields.LBL_PATIENT_NAME.equals(field.getString(JsonFormUtils.KEY))) {
            // test patient fields are populated
            assertEquals(field.getString("text"), patient.getPatientName());
            numOfPopulatedFields++;
        } else if (Constants.FormFields.LBL_PATIENT_GENDER_AND_ID.equals(field.getString(JsonFormUtils.KEY))) {
            // test patient fields are populated
            assertEquals(field.getString("text"), patient.getPatientSex() + BULLET_DOT + "ID: " + patient.getBaseEntityId());
            numOfPopulatedFields++;
        } else if (Constants.FormFields.LBL_RDT_ID.equals(field.getString(JsonFormUtils.KEY))) {
            // test rdt id labels are populated
            assertEquals(field.getString("text"), "RDT ID: " + UNIQUE_ID);
            numOfPopulatedFields++;
        } else if (getFormUtils().isRDTIdField(field)) {
            // test rdt id is populated
            assertEquals(field.getString(JsonFormUtils.VALUE), UNIQUE_ID);
            numOfPopulatedFields++;
        }
        return numOfPopulatedFields;
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return new RDTJsonFormUtils();
    }

    @Override
    protected String getPatientRegistrationEvent() {
        return Constants.Encounter.PATIENT_REGISTRATION;
    }
}
