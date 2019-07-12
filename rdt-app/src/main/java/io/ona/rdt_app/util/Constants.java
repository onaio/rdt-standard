package io.ona.rdt_app.util;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public interface Constants {
    int REQUEST_RDT_PERMISSIONS = 1000;
    int REQUEST_CODE_GET_JSON = 9388;

    String JSON_FORM_PARAM_JSON = "json";
    String METADATA = "metadata";
    String DETAILS = "details";
    String ENCOUNTER_TYPE = "encounter_type";
    String PATIENTS = "patients";
    String RDT_TESTS = "rdt_tests";
    String PATIENT_REGISTRATION = "patient_registration";
    String DOB = "dob";
    String PATIENT_AGE = "patient_age";
    String PROFILE_PIC = "profilepic";
    String EXPIRATION_DATE_RESULT = "expiration_date_result";
    String ONA_RDT = "ona_rdt";
    String CARESTART_RDT = "carestart_rdt";
    String LBL_CARE_START = "lbl_care_start";

    interface DBConstants {
        String NAME = "name";
        String AGE = "age";
        String SEX = "sex";
        String ID = "id";
        String BASE_ENTITY_ID = "base_entity_id";
    }

    interface Form {
        String PATIENT_REGISTRATION_FORM = "json.form/patient-registration-form.json";
        String RDT_TEST_FORM = "json.form/rdt-capture-form.json";
        String LBL_RDT_ID = "lbl_rdt_id";
        String EXPIRATION_DATE_READER = "expiration_date_reader";
        String RDT_CAPTURE = "rdt_capture";
        String TIME_IMG_SAVED = "time_img_saved";
        String LBL_PATIENT_NAME = "lbl_patient_name";
        String LBL_PATIENT_GENDER_AND_ID = "lbl_patient_gender_and_id";
    }
}
