package io.ona.rdt.util;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public interface CovidConstants extends Constants {

    interface Widget {
        String GOOGLE_COVID_BARCODE_READER = "google_covid_barcode_reader";
        String ONE_SCAN_COVID_BARCODE_READER = "one_scan_covid_barcode_reader";
    }

    interface Table {
        String COVID_PATIENTS = "covid_patients";
        String COVID_RDT_TESTS = "covid_rdt_tests";
        String PATIENT_DIAGNOSTIC_RESULTS = "patient_diagnostic_results";
        String SAMPLE_COLLECTIONS = "sample_collections";
        String SAMPLE_DELIVERY_RECORDS = "sample_delivery_records";
        String COVID_XRAY_RECORDS = "covid_xray_records";
        String COVID_WBC_RECORDS = "covid_wbc_records";
    }

    interface Encounter {
        String PATIENT_DIAGNOSTICS = "patient_diagnostics";
        String SAMPLE_COLLECTION = "sample_collection";
        String SAMPLE_DELIVERY_DETAILS = "sample_delivery_details";
        String COVID_RDT_TEST = "covid_rdt_test";
        String COVID_PATIENT_REGISTRATION = "covid_patient_registration";
        String COVID_XRAY = "covid_xray";
        String COVID_WBC = "covid_wbc";
    }

    interface Form {
        String JSON_FORM_FOLDER = "json.form/";
        String PATIENT_DIAGNOSTICS_FORM = JSON_FORM_FOLDER + "patient-diagnostics-form.json";
        String SAMPLE_COLLECTION_FORM = JSON_FORM_FOLDER + "sample-collection-form.json";
        String SAMPLE_DELIVERY_DETAILS_FORM = JSON_FORM_FOLDER + "sample-delivery-details-form.json";
        String XRAY_FORM = JSON_FORM_FOLDER + "xray-form.json";
        String WBC_FORM = JSON_FORM_FOLDER + "wbc-form.json";
        String COVID_PATIENT_REGISTRATION_FORM = JSON_FORM_FOLDER + "patient-registration-form.json";
        String COVID_RDT_TEST_FORM = JSON_FORM_FOLDER + "rdt-capture-form.json";
    }

    interface Step {
        String COVID_SCAN_BARCODE_PAGE = "covid_scan_barcode_page";
        String COVID_CONDUCT_RDT_PAGE = "covid_conduct_rdt_page";
        String COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE = "covid_respiratory_specimen_collection_opt_in_page";
        String COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE = "covid_collect_respiratory_specimen_page";
        String COVID_TEST_COMPLETE_PAGE = "covid_test_complete_page";
        String COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE = "covid_affix_respiratory_sample_id_page";
        String COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE  = "covid_one_scan_widget_specimen_page";
        String COVID_SCAN_SAMPLE_FOR_DELIVERY_PAGE = "covid_scan_sample_for_delivery_page";
        String COVID_ENTER_DELIVERY_DETAILS_PAGE = "covid_enter_delivery_details_page";
    }

    interface FormFields {
        String LBL_SCAN_BARCODE = "lbl_scan_barcode";
        String LBL_ENTER_RDT_MANUALLY = "lbl_enter_rdt_manually";
        String LBL_SCAN_RESPIRATORY_SPECIMEN_BARCODE = "lbl_scan_respiratory_specimen_barcode";
        String LBL_AFFIX_RESPIRATORY_SPECIMEN_LABEL = "lbl_affix_respiratory_specimen_label";
        String LBL_RESPIRATORY_SAMPLE_ID = "lbl_respiratory_sample_id";
        String LBL_ADD_XRAY_RESULTS = "lbl_add_xray_results";
        String LBL_SKIP_XRAY_RESULTS = "lbl_skip_xray_results";
        String LBL_ADD_WBC_RESULTS = "lbl_add_wbc_results";
        String LBL_SKIP_WBC_RESULTS = "lbl_skip_wbc_results";
        String LBL_SCAN_SAMPLE_BARCODE = "lbl_scan_sample_barcode";
        String LBL_ENTER_SAMPLE_DETAILS_MANUALLY = "lbl_enter_sample_details_manually";
        String COVID_SAMPLE_ID = "covid_sample_id";
        String PATIENT_FIRST_NAME = "patient_first_name";
        String PATIENT_LAST_NAME = "patient_last_name";
        String PATIENT_SEX = "patient_sex";
        String LAST_KNOWN_LOCATION = "last_known_location";
        String DRIVERS_LICENSE_NUMBER = "drivers_license_number";
        String PASSPORT_NO = "passport_no";
        String LAT = "lat";
        String LNG = "lng";
        String UNIQUE_ID = "unique_id";
        String PATIENT_INFO_UNIQUE_ID = "patient_info_unique_id";
        String PATIENT_INFO_NAME = "patient_info_name";
        String PATIENT_INFO_DOB = "patient_info_dob";
    }

    interface RDTType {
        String COVID_WONDFO = "covid19-wondfo";
        String COVID_ALLTEST = "covid19-jalmedical";
    }

    interface RequestCodes {
        int LOCATION_PERMISSIONS = 8732;
    }

    interface ScannerType {
        String SCANNER  = "scanner";
    }
}
