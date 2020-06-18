package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import androidx.core.util.Pair;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.AdditionalMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.smartregister.domain.ProfileImage;
import org.smartregister.domain.UniqueId;
import org.smartregister.domain.tag.FormTag;
import org.smartregister.repository.AllSettings;
import org.smartregister.repository.ImageRepository;
import org.smartregister.repository.UniqueIdRepository;
import org.smartregister.util.AssetHandler;
import org.smartregister.view.activity.DrishtiApplication;

import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import edu.washington.cs.ubicomplab.rdt_reader.utils.ImageUtil;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.Patient;

import static io.ona.rdt.TestUtils.getTestFilePath;
import static io.ona.rdt.util.Constants.Config.MULTI_VERSION;
import static io.ona.rdt.util.Constants.FormFields.RDT_ID;
import static io.ona.rdt.util.Constants.FormFields.COVID_SAMPLE_ID;
import static io.ona.rdt.util.Constants.Format.BULLET_DOT;
import static io.ona.rdt.util.Constants.Step.RDT_ID_KEY;
import static io.ona.rdt.util.Constants.Step.SCAN_CARESTART_PAGE;
import static io.ona.rdt.util.Constants.Step.SCAN_QR_PAGE;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMAGE;
import static io.ona.rdt.util.Constants.Test.FULL_IMAGE;
import static io.ona.rdt.util.Utils.isCovidApp;
import static io.ona.rdt.util.Utils.isMalariaApp;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;
import static org.smartregister.util.JsonFormUtils.KEY;
import static org.smartregister.util.JsonFormUtils.VALUE;
import static org.smartregister.util.JsonFormUtils.getMultiStepFormFields;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
@PrepareForTest({AssetHandler.class, ImageUtil.class, RDTApplication.class, org.smartregister.Context.class, DrishtiApplication.class})
public class RDTJsonFormUtilsTest extends PowerMockTest {

    private static RDTJsonFormUtils formUtils;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private UniqueIdRepository uniqueIdRepository;

    @Mock
    private AllSettings allSettings;

    @Mock
    private StepStateConfig stepStateConfig;

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
            "        \"key\": \"rdt_capture_top_line_result\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_middle_line_result\",\n" +
            "        \"openmrs_entity_parent\": \"\",\n" +
            "        \"openmrs_entity\": \"\",\n" +
            "        \"openmrs_entity_id\": \"\",\n" +
            "        \"type\": \"hidden\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"key\": \"rdt_capture_bottom_line_result\",\n" +
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
            "        \"key\": \"respiratory_sample_id\",\n" +
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

    @Before
    public void setUp() throws JSONException {
        MockitoAnnotations.initMocks(this);
        RDT_TEST_JSON_FORM_OBJ = new JSONObject(RDT_TEST_JSON_FORM);
        formUtils = new RDTJsonFormUtils();
    }

    @Test
    public void testPrePopulateFormFieldsShouldPopulateCorrectValues() throws JSONException {
        mockStaticMethods();

        JSONObject formObject = new JSONObject(RDT_TEST_JSON_FORM);
        Patient patient = new Patient("patient", "female", "entity_id");

        formUtils.prePopulateFormFields(formObject, patient, getIDs());

        boolean populatedLblRDTId = false;
        boolean populatedRDTId = false;
        boolean populatedPatientName = false;
        boolean populatedPatientSex = false;
        boolean populatedLblRespiratorySampleID = false;
        boolean populatedRespiratorySampleID = false;
        JSONArray fields = getMultiStepFormFields(formObject);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            // test rdt id labels are populated
            if (Constants.FormFields.LBL_RDT_ID.equals(field.getString(KEY))) {
               assertEquals(field.getString("text"), "RDT ID: " + RDT_ID);
               populatedLblRDTId = true;
            }
            // test rdt id is populated
            if (formUtils.isRDTIdField(field)) {
                assertEquals(field.getString(VALUE), RDT_ID);
                populatedRDTId = true;
            }
            // test patient fields are populated
            if (Constants.FormFields.LBL_PATIENT_NAME.equals(field.getString(KEY))) {
                assertEquals(field.getString("text"), patient.getPatientName());
                populatedPatientName = true;
            } else if (Constants.FormFields.LBL_PATIENT_GENDER_AND_ID.equals(field.getString(KEY))) {
                assertEquals(field.getString("text"), patient.getPatientSex() + BULLET_DOT + "ID: " + patient.getBaseEntityId());
                populatedPatientSex = true;
            }

            // test respiratory sample IDs
            if (isCovidApp()) {
                // pre-populate respiratory sample id labels
                if (Constants.FormFields.LBL_RESPIRATORY_SAMPLE_ID.equals(field.getString(KEY))) {
                    assertEquals(field.getString("text"), "Respiratory sample ID: " + COVID_SAMPLE_ID);
                    populatedLblRespiratorySampleID = true;
                }
                // pre-populate respiratory sample id field
                if (COVID_SAMPLE_ID.equals(field.getString(KEY))) {
                    assertEquals(field.getString(VALUE), COVID_SAMPLE_ID);
                    populatedRespiratorySampleID = true;
                }
            }
        }

        if (isMalariaApp()) {
            assertTrue(populatedRDTId && populatedPatientName && populatedLblRDTId
                    && populatedPatientSex);
        } else if (isCovidApp()) {
            assertTrue(populatedRDTId && populatedPatientName && populatedLblRDTId
                    && populatedPatientSex && populatedLblRespiratorySampleID && populatedRespiratorySampleID);
        }
    }

    @Test
    public void testGetUniqueIDsShouldGetIDs() throws Exception {
        mockStaticMethods();

        UniqueId uniqueId = new UniqueId();
        uniqueId.setOpenmrsId("openmrsID1");
        doReturn(uniqueId).when(uniqueIdRepository).getNextUniqueId();

        List<UniqueId> uniqueIds = Whitebox.invokeMethod(formUtils, "getUniqueIDs", 2);
        assertEquals("openmrsID1", uniqueIds.get(0).getOpenmrsId());
        assertEquals("openmrsID1", uniqueIds.get(1).getOpenmrsId());

        uniqueId = new UniqueId();
        uniqueId.setOpenmrsId("openmrsID2");
        doReturn(uniqueId).when(uniqueIdRepository).getNextUniqueId();

        uniqueIds = Whitebox.invokeMethod(formUtils, "getUniqueIDs", 2);
        assertEquals("openmrsID2", uniqueIds.get(0).getOpenmrsId());
        assertEquals("openmrsID2", uniqueIds.get(1).getOpenmrsId());
    }

    @Test
    public void testAppendEntityIdShouldAppendCorrectNonEmptyId() throws JSONException {
        String entityId = formUtils.appendEntityId(RDT_TEST_JSON_FORM_OBJ);
        assertNotNull(entityId);
        assertFalse(entityId.isEmpty());
        assertEquals(RDT_TEST_JSON_FORM_OBJ.get(Constants.FormFields.ENTITY_ID), entityId);
    }

    @Test
    public void testLaunchFormShouldntThrowException() throws JSONException {
        mockStaticMethods();
        formUtils.launchForm(mock(Activity.class), "form_name", mock(Patient.class), getIDs());
    }

    @Test
    public void testGetFormJsonObjectShouldGetFormJsonObject() throws Exception {
        mockStaticMethods();
        JSONObject jsonObject = formUtils.getFormJsonObject("form_name", mock(Context.class));
        assertEquals(RDT_TEST_JSON_FORM_OBJ.toString(), jsonObject.toString());
    }

    @Test
    public void testSaveImageToGalleryShouldSaveImageToGallery() throws Exception {
        mockStaticMethods();
        Context context = mock(Context.class);
        Whitebox.invokeMethod(formUtils, "saveImageToGallery", context, mock(Bitmap.class));
        verifyStatic(ImageUtil.class, times(1));
        ImageUtil.saveImage(eq(context), any(), eq(0L), eq(false), any(OnImageSavedCallBack.class));
    }

    @Test
    public void testWriteImageToDiskShouldSuccessfullyWriteImageToDisk() throws Exception {
        mockStaticMethods();
        Bitmap image = mock(Bitmap.class);
        Pair<Boolean, String> result = Whitebox.invokeMethod(formUtils, "writeImageToDisk", getTestFilePath(), image, mock(Context.class));
        verify(image).compress(eq(Bitmap.CompressFormat.JPEG), eq(100), any(OutputStream.class));
        cleanUpFiles(result.second);
    }

    @Test
    public void testSaveImgDetailsShouldSaveCorrectImgDetails() throws Exception {
        mockStaticMethods();

        ProfileImage profileImage = spy(new ProfileImage("image_id", "anm_id", "entity_id", "content_type", "file_path", "sync_status", "file_category"));
        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withProviderId("anm_id")
                .withBaseEntityId("entity_id");

        Whitebox.invokeMethod(formUtils, "saveImgDetails", "file_path", parcelableImageMetadata, profileImage);
        verify(profileImage).setAnmId(eq("anm_id"));
        verify(profileImage).setEntityID(eq("entity_id"));
        verify(profileImage).setFilepath(eq("file_path"));
        verify(profileImage).setFilecategory(eq(MULTI_VERSION));
        verify(profileImage).setSyncStatus(eq(ImageRepository.TYPE_Unsynced));
        verify(imageRepository).add(eq(profileImage));
    }

    @Test
    public void testSaveImageShouldSaveImage() throws Exception {
        mockStaticMethods();

        ParcelableImageMetadata parcelableImageMetadata = spy(new ParcelableImageMetadata());
        parcelableImageMetadata.withProviderId("anm_id")
                .withBaseEntityId("entity_id");
        Whitebox.invokeMethod(formUtils, "saveImage", getTestFilePath(), mock(Bitmap.class), mock(Context.class), parcelableImageMetadata);
        verify(parcelableImageMetadata).setCroppedImageId(any());

        parcelableImageMetadata.setImageToSave(FULL_IMAGE);
        Whitebox.invokeMethod(formUtils, "saveImage", getTestFilePath(), mock(Bitmap.class), mock(Context.class), parcelableImageMetadata);
        verify(parcelableImageMetadata).setFullImageId(any());
        verify(parcelableImageMetadata).setImageTimeStamp(anyLong());
    }

    @Test
    public void testSaveStaticImagesToDiskShouldReturnIfMissingInformation() throws Exception {
        OnImageSavedCallback onImageSavedCallback = mock(OnImageSavedCallback.class);
        CompositeImage compositeImage = mock(CompositeImage.class);
        doReturn(mock(ParcelableImageMetadata.class)).when(compositeImage).getParcelableImageMetadata();
        Whitebox.invokeMethod(formUtils, "saveStaticImagesToDisk", mock(Context.class), compositeImage, onImageSavedCallback);
        verify(onImageSavedCallback).onImageSaved(isNull());
    }

    @Test
    public void testSaveImage() throws Exception {
        mockStaticMethods();
        ParcelableImageMetadata parcelableImageMetadata = mock(ParcelableImageMetadata.class);

        CompositeImage compositeImage = mock(CompositeImage.class);
        doReturn(mock(Bitmap.class)).when(compositeImage).getFullImage();
        doReturn(mock(Bitmap.class)).when(compositeImage).getCroppedImage();
        doReturn(mock(ParcelableImageMetadata.class)).when(compositeImage).getParcelableImageMetadata();
        Whitebox.invokeMethod(formUtils, "saveImage", "entity_id", parcelableImageMetadata, compositeImage, mock(Context.class));
        verify(parcelableImageMetadata).setImageToSave(eq(FULL_IMAGE));
        verify(parcelableImageMetadata).setImageToSave(eq(CROPPED_IMAGE));
    }


    @Test
    public void testGetFileMD5HashShouldGetCorrectHash() throws Exception {
        assertEquals("d41d8cd98f00b204e9800998ecf8427e", Whitebox.invokeMethod(formUtils, "getFileMD5Hash", getTestFilePath() + "5de70b65-9597-4a0c-aac0-8c7b0ecef001.JPEG"));
    }

    @Test
    public void testGetFormTagShouldPopulateTagWithCorrectMetadata() {
        mockStaticMethods();
        doReturn("provider").when(allSettings).fetchRegisteredANM();
        doReturn("location-id").when(allSettings).fetchDefaultLocalityId(anyString());
        doReturn("team-id").when(allSettings).fetchDefaultTeamId(anyString());
        doReturn("team").when(allSettings).fetchDefaultTeam(anyString());
        FormTag formTag = formUtils.getFormTag();
        assertEquals("provider", formTag.providerId);
        assertEquals("location-id", formTag.locationId);
        assertEquals("team-id", formTag.teamId);
        assertEquals("team", formTag.team);
    }

    private void cleanUpFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private void mockStaticMethods() {
        // mock AssetHandler
        mockStatic(AssetHandler.class);
        PowerMockito.when(AssetHandler.readFileFromAssetsFolder(any(), any())).thenReturn(RDT_TEST_JSON_FORM);

        mockStatic(ImageUtil.class);

        // mock RDTApplication and Drishti context
        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        // mock repositories
        PowerMockito.when(drishtiContext.imageRepository()).thenReturn(imageRepository);
        PowerMockito.when(drishtiContext.getUniqueIdRepository()).thenReturn(uniqueIdRepository);
        PowerMockito.when(drishtiContext.allSettings()).thenReturn(allSettings);

        // Drishti
        mockStatic(DrishtiApplication.class);
        PowerMockito.when(DrishtiApplication.getAppDir()).thenReturn(getTestFilePath());

        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getStepStateConfiguration()).thenReturn(stepStateConfig);

        JSONObject jsonObject = mock(JSONObject.class);
        doReturn("step1").when(jsonObject).optString(AdditionalMatchers.or(eq(SCAN_CARESTART_PAGE), eq(SCAN_QR_PAGE)));
        doReturn(jsonObject).when(stepStateConfig).getStepStateObj();
        doReturn("rdt_id").when(jsonObject).optString(eq(RDT_ID_KEY));
    }

    private List<String> getIDs() {
        List<String> rdtIds = new ArrayList<>();
        rdtIds.add(RDT_ID);
        rdtIds.add(COVID_SAMPLE_ID);
        return rdtIds;
    }
}
