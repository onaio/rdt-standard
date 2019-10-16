package io.ona.rdt_app.util;

import io.ona.rdt_app.BuildConfig;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public interface Constants {
    int REQUEST_RDT_PERMISSIONS = 1000;
    int REQUEST_CODE_GET_JSON = 9388;

    String JSON_FORM_PARAM_JSON = "json";
    String ENTITY_ID = "entity_id";
    String METADATA = "metadata";
    String DETAILS = "details";
    String ENCOUNTER_TYPE = "encounter_type";
    String RDT_PATIENTS = "rdt_patients";
    String RDT_TESTS = "rdt_tests";
    String PATIENT_REGISTRATION = "patient_registration";
    String DOB = "dob";
    String PATIENT_NAME = "patient_name";
    String PATIENT_AGE = "patient_age";
    String CONDITIONAL_SAVE = "conditional_save";
    String MULTI_VERSION = "multi_version";
    String EXPIRATION_DATE_RESULT = "expiration_date_result";
    String EXPIRATION_DATE = "expiration_date";
    String ONA_RDT = "experimental";
    String CARESTART_RDT = "carestart";
    String LBL_CARE_START = "lbl_care_start";
    String BULLET_DOT = " \u00B7 ";
    String IS_IMG_SYNC_ENABLED = "is_img_sync_enabled";
    String PROVIDER_ID = "provider_id";
    String EXPIRED_PAGE_ADDRESS = "expired_page_address";

    String PHONE_MODEL = "phone_model";
    String PHONE_OS_VERSION = "phone_os_version";
    String PHONE_MANUFACTURER = "phone_manufacturer";
    String APP_VERSION = "app_version";

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
    }

    interface Form {
        String PATIENT_REGISTRATION_FORM = "json.form-" + BuildConfig.LOCALE + "/patient-registration-form.json";
        String RDT_TEST_FORM = "json.form-" + BuildConfig.LOCALE + "/rdt-capture-form.json";
        String LBL_RDT_ID = "lbl_rdt_id";
        String RDT_ID = "rdt_id";
        String LBL_PATIENT_NAME = "lbl_patient_name";
        String LBL_PATIENT_GENDER_AND_ID = "lbl_patient_gender_and_id";
        String RDT_CAPTURE_CONTROL_RESULT = "rdt_capture_control_result";
        String RDT_CAPTURE_PV_RESULT = "rdt_capture_pv_result";
        String RDT_CAPTURE_PF_RESULT = "rdt_capture_pf_result";
        String RDT_CAPTURE_DURATION = "rdt_capture_duration";
        String RDT_TYPE = "rdt_type";
    }

    interface Test {
        String TEST_CONTROL_RESULT = "test_control_result";
        String TEST_PV_RESULT = "test_pv_result";
        String TEST_PF_RESULT = "test_pf_result";
        String RDT_CAPTURE_DURATION = "rdt_capture_duration";
        String FULL_IMG_ID_AND_TIME_STAMP = "full_img_id_and_time_stamp";
        String FLASH_ON = "flash_on";
        String CASSETTE_BOUNDARY = "cassette_boundary";
        String CROPPED_IMG_ID = "cropped_img_id";
        String FULL_IMAGE = "full_image";
        String CROPPED_IMAGE = "cropped_image";
        String PARCELABLE_IMAGE_METADATA = "parcelable_image_metadata";
    }
}
