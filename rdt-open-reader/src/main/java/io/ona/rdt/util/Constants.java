package io.ona.rdt.util;

import io.ona.rdt.BuildConfig;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public interface Constants {

    interface Widget {
        String UW_COVID_RDT_CAPTURE = "uw_covid_rdt_capture";
        String COVID_GOOGLE_BARCODE_READER = "covid_google_barcode_reader";
        String COVID_ONE_SCAN_BARCODE_READER = "covid_one_scan_barcode_reader";
    }

    interface Format {
        String BULLET_DOT = " \u00B7 ";
        String PROFILE_DATE_FORMAT = "dd MMM yyyy";
    }

    interface Config {
        String MULTI_VERSION = "multi_version";
        String IS_IMG_SYNC_ENABLED = "is_img_sync_enabled";
    }

    interface Result {
        String JSON_FORM_PARAM_JSON = "json";
        String EXPIRATION_DATE_RESULT = "expiration_date_result";
        String EXPIRATION_DATE = "expiration_date";
    }

    interface RDTType {
        String ONA_RDT = "malaria-experimental";
        String CARESTART_RDT = "malaria-carestart";
    }

    interface FormFields {
        String PATIENT = "patient";
        String DOB = "dob";
        String PATIENT_NAME = "patient_name";
        String PATIENT_AGE = "patient_age";
        String CONDITIONAL_SAVE = "conditional_save";
        String METADATA = "metadata";
        String DETAILS = "details";
        String ENCOUNTER_TYPE = "encounter_type";
        String ENTITY_ID = "entity_id";
        String MANUAL_EXPIRATION_DATE = "manual_expiration_date";
        String LBL_CARE_START = "lbl_care_start";
        String LBL_SCAN_QR_CODE = "lbl_scan_qr_code";
        String LBL_SCAN_BARCODE = "lbl_scan_barcode";
        String LBL_ENTER_RDT_MANUALLY = "lbl_enter_rdt_manually";
        String LBL_CONDUCT_RDT = "lbl_conduct_rdt";
        String LBL_SKIP_RDT_TEST = "lbl_skip_rdt_test";
        String LBL_COLLECT_RESPIRATORY_SAMPLE = "lbl_collect_respiratory_sample";
        String LBL_SKIP_RESPIRATORY_SAMPLE_COLLECTION = "lbl_skip_respiratory_sample_collection";
        String LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE = "lbl_scan_respiratory_specimen_barcode";
        String LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL = "lbl_affix_respiratory_specimen_label";
        String LBL_RDT_ID = "lbl_rdt_id";
        String LBL_PATIENT_NAME = "lbl_patient_name";
        String LBL_PATIENT_GENDER_AND_ID = "lbl_patient_gender_and_id";
        String LBL_RESPIRATORY_SAMPLE_ID = "lbl_respiratory_sample_id";
        String LBL_ADD_XRAY_RESULTS = "lbl_add_xray_results";
        String LBL_SKIP_XRAY_RESULTS = "lbl_skip_xray_results";
        String LBL_ADD_WBC_RESULTS = "lbl_add_wbc_results";
        String LBL_SKIP_WBC_RESULTS = "lbl_skip_wbc_results";
        String LBL_SCAN_SAMPLE_BARCODE = "lbl_scan_sample_barcode";
        String LBL_ENTER_SAMPLE_DETAILS_MANUALLY = "lbl_enter_sample_details_manually";
        String RDT_ID = "rdt_id";
        String COVID_SAMPLE_ID = "covid_sample_id";
        String RDT_CAPTURE_TOP_LINE_RESULT = "rdt_capture_top_line_result";
        String RDT_CAPTURE_MIDDLE_LINE_RESULT = "rdt_capture_middle_line_result";
        String RDT_CAPTURE_BOTTOM_LINE_RESULT = "rdt_capture_bottom_line_result";
    }

    interface RequestCodes {
        int REQUEST_RDT_PERMISSIONS = 1000;
        int REQUEST_CODE_GET_JSON = 9388;
    }

    interface PhoneProperties {
        String PHONE_MODEL = "phone_model";
        String PHONE_OS_VERSION = "phone_os_version";
        String PHONE_MANUFACTURER = "phone_manufacturer";
        String APP_VERSION = "app_version";
    }

    interface Tags {
        String COUNTRY = "Country";
        String PROVINCE = "Province";
        String DISTRICT = "District";
        String HEALTH_CENTER = "Rural Health Centre";
        String VILLAGE = "Village";
    }

    interface DBConstants {
        String FIRST_NAME = "first_name";
        String LAST_NAME = "last_name";
        String AGE = "age";
        String SEX = "sex";
        String PATIENT_ID = "patient_id";
    }

    interface Form {
        String PATIENT_REGISTRATION_FORM = "json.form-" + BuildConfig.LOCALE + "/patient-registration-form.json";
        String RDT_TEST_FORM = "json.form-" + BuildConfig.LOCALE + "/rdt-capture-form.json";
        String PATIENT_DIAGNOSTICS_FORM = "json.form-" + BuildConfig.LOCALE + "/patient-diagnostics-form.json";
        String SAMPLE_COLLECTION_FORM = "json.form-" + BuildConfig.LOCALE + "/sample-collection-form.json";
        String SAMPLE_DELIVERY_DETAILS_FORM = "json.form-" + BuildConfig.LOCALE + "/sample-delivery-details-form.json";
        String SUPPORT_INVESTIGATION_FORM = "json.form-" + BuildConfig.LOCALE + "/support-investigation-form.json";
        String RDT_TYPE = "rdt_type";
    }

    interface Test {
        String RDT_CAPTURE_DURATION = "rdt_capture_duration";
        String FULL_IMAGE = "full_image";
        String CROPPED_IMAGE = "cropped_image";
        String PARCELABLE_IMAGE_METADATA = "parcelable_image_metadata";
        String CROPPED_IMG_ID = "cropped_img_id";
        String TIME_IMG_SAVED = "time_img_saved";
        String FLASH_ON = "flash_on";
        String CASSETTE_BOUNDARY = "cassette_boundary";
        String IS_MANUAL_CAPTURE = "is_manual_capture";
        String POSITIVE = "positive";
        String NEGATIVE = "negative";
        String INVALID = "invalid";
        String RDT_TEST_DETAILS = "rdt_test_details";
        String RDT_Q_PCR = "rdt";
        String BLOODSPOT_Q_PCR = "bloodspot";
        String MICROSCOPY = "microscopy";
        String Q_PCR = " qPCR";
        String CROPPED_IMG_MD5_HASH = "cropped_img_md5_hash";
        String FULL_IMG_MD5_HASH = "full_img_md5_hash";
    }

    interface Step {
        String RDT_EXPIRED_PAGE = "rdt_expired_page";
        String BLOT_PAPER_TASK_PAGE = "blot_paper_task_page";
        String DISABLED_BACK_PRESS_PAGES = "disabled_back_press_pages";
        String COVID_DISABLED_BACK_PRESS_PAGES = "covid_disabled_back_press_pages";
        String MANUAL_ENTRY_EXPIRATION_PAGE = "manual_entry_expiration_page";
        String RDT_EXPIRED_PAGE_ADDRESS = "rdt_expired_page";
        String EXPIRATION_DATE_READER_ADDRESS = "expiration_date_reader_address";
        String RDT_ID_KEY = "rdt_id_key";
        String RDT_ID_LBL_ADDRESSES = "rdt_id_lbl_addresses";
        String SCAN_QR_PAGE = "scan_qr_page";
        String SCAN_CARESTART_PAGE = "scan_carestart_page";
        String TWENTY_MIN_COUNTDOWN_TIMER_PAGE = "twenty_min_countdown_timer_page";
        String TAKE_IMAGE_OF_RDT_PAGE = "take_image_of_rdt_page";
        String COVID_SCAN_BARCODE_PAGE = "covid_scan_barcode_page";
        String COVID_CONDUCT_RDT_PAGE = "covid_conduct_rdt_page";
        String COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE = "covid_respiratory_specimen_collection_opt_in_page";
        String COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE = "covid_collect_respiratory_specimen_page";
        String COVID_TEST_COMPLETE_PAGE = "covid_test_complete_page";
        String COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE = "covid_affix_respiratory_sample_id_page";
        String COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE  = "covid_one_scan_widget_specimen_page";
        String COVID_MANUAL_RDT_ENTRY_PAGE = "covid_manual_rdt_entry_page";
        String COVID_RDT_EXPIRED_PAGE = "covid_rdt_expired_page";
        String COVID_XRAY_PAGE = "covid_xray_page";
        String COVID_OPT_IN_WBC_PAGE = "covid_opt_in_wbc_page";
        String COVID_WBC_PAGE = "covid_wbc_page";
        String COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE = "covid_support_investigation_complete_page";
        String COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE = "covid_scan_sample_for_delivery_page";
        String COVID_ENTER_DELIVERY_DETAILS_PAGE = "covid_enter_delivery_details_page";
    }

    interface Encounter {
        String PATIENT_REGISTRATION = "patient_registration";
        String COVID_PATIENT_REGISTRATION = "covid_patient_registration";
        String RDT_TEST = "rdt_test";
        String COVID_RDT_TEST = "covid_rdt_test";
        String PCR_RESULT = "pcr_result";
        String PATIENT_DIAGNOSTICS = "patient_diagnostics";
        String SAMPLE_COLLECTION = "sample_collection";
        String SAMPLE_DELIVERY_DETAILS = "sample_delivery_details";
        String SUPPORT_INVESTIGATION = "support_investigation";
    }

    interface Table {
        String RDT_PATIENTS = "rdt_patients";
        String COVID_PATIENTS = "covid_patients";
        String RDT_TESTS = "rdt_tests";
        String COVID_RDT_TESTS = "covid_rdt_tests";
        String PCR_RESULTS = "pcr_results";
        String MICROSCOPY_RESULTS = "microscopy_results";
        String PATIENT_DIAGNOSTIC_RESULTS = "patient_diagnostic_results";
        String SAMPLE_COLLECTIONS = "sample_collections";
        String SAMPLE_DELIVERY_RECORDS = "sample_delivery_records";
        String SUPPORT_INVESTIGATIONS = "support_investigations";
    }
}
