package io.ona.rdt.util;

import io.ona.rdt.BuildConfig;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public interface Constants {

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
        String ONA_RDT = "experimental";
        String CARESTART_RDT = "carestart";
    }

    interface FormFields {
        String PATIENT = "patient";
        String DOB = "dob";
        String PATIENT_NAME = "patient_name";
        String RESIDENTIAL_ADDRESS = "residential_address";
        String PATIENT_AGE = "patient_age";
        String CONDITIONAL_SAVE = "conditional_save";
        String METADATA = "metadata";
        String DETAILS = "details";
        String ENCOUNTER_TYPE = "encounter_type";
        String ENTITY_ID = "entity_id";
        String LBL_CARE_START = "lbl_care_start";
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
        String NAME = "name";
        String AGE = "age";
        String SEX = "sex";
        String RESIDENTIAL_ADDRESS = "residential_address";
        String PATIENT_ID = "patient_id";
    }

    interface Form {
        String PATIENT_REGISTRATION_FORM = "json.form-" + BuildConfig.LOCALE + "/patient-registration-form.json";
        String RDT_TEST_FORM = "json.form-" + BuildConfig.LOCALE + "/rdt-capture-form.json";
        String LBL_RDT_ID = "lbl_rdt_id";
        String LBL_PATIENT_NAME = "lbl_patient_name";
        String LBL_PATIENT_GENDER_AND_ID = "lbl_patient_gender_and_id";
        String RDT_CAPTURE_CONTROL_RESULT = "rdt_capture_control_result";
        String RDT_CAPTURE_PV_RESULT = "rdt_capture_pv_result";
        String RDT_CAPTURE_PF_RESULT = "rdt_capture_pf_result";
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
    }

    interface Step {
        String RDT_EXPIRED_PAGE = "rdt_expired_page";
        String BLOT_PAPER_TASK_PAGE = "blot_paper_task_page";
        String DISABLED_BACK_PRESS_PAGES = "disabled_back_press_pages";
        String MANUAL_ENTRY_EXPIRATION_PAGE = "manual_entry_expiration_page";
        String RDT_EXPIRED_PAGE_ADDRESS = "rdt_expired_page";
        String EXPIRATION_DATE_READER_ADDRESS = "expiration_date_reader_address";
        String RDT_ID_KEY = "rdt_id_key";
        String RDT_ID_LBL_ADDRESSES = "rdt_id_lbl_addresses";
        String SCAN_QR_PAGE = "scan_qr_page";
        String SCAN_CARESTART_PAGE = "scan_carestart_page";
        String TWENTY_MIN_COUNTDOWN_TIMER_PAGE = "twenty_min_countdown_timer_page";
        String TAKE_IMAGE_OF_RDT_PAGE = "take_image_of_rdt_page";
    }

    interface Encounter {
        String PATIENT_REGISTRATION = "patient_registration";
        String RDT_TEST = "rdt_test";
        String PCR_RESULT = "pcr_result";
    }

    interface Table {
        String RDT_PATIENTS = "rdt_patients";
        String RDT_TESTS = "rdt_tests";
        String PCR_RESULTS = "pcr_results";
        String MICROSCOPY_RESULTS = "microscopy_results";
    }
}
