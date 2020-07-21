package io.ona.rdt.util;

import io.ona.rdt.BuildConfig;

/**
 * Created by Vincent Karuri on 13/07/2020
 */
public interface CovidConstants extends Constants {

    interface Widget {
        String UW_COVID_RDT_CAPTURE = "uw_covid_rdt_capture";
        String GOOGLE_COVID_BARCODE_READER = "google_covid_barcode_reader";
        String ONE_SCAN_COVID_BARCODE_READER = "one_scan_covid_barcode_reader";
    }

    interface Table {
        String COVID_PATIENTS = "covid_patients";
        String COVID_RDT_TESTS = "covid_rdt_tests";
        String PATIENT_DIAGNOSTIC_RESULTS = "patient_diagnostic_results";
        String SAMPLE_COLLECTIONS = "sample_collections";
        String SAMPLE_DELIVERY_RECORDS = "sample_delivery_records";
        String SUPPORT_INVESTIGATIONS = "support_investigations";
    }

    interface Encounter {
        String PATIENT_DIAGNOSTICS = "patient_diagnostics";
        String SAMPLE_COLLECTION = "sample_collection";
        String SAMPLE_DELIVERY_DETAILS = "sample_delivery_details";
        String SUPPORT_INVESTIGATION = "support_investigation";
        String COVID_RDT_TEST = "covid_rdt_test";
        String COVID_PATIENT_REGISTRATION = "covid_patient_registration";
    }

    interface Form {
        String PATIENT_DIAGNOSTICS_FORM = "json.form-" + BuildConfig.LOCALE + "/patient-diagnostics-form.json";
        String SAMPLE_COLLECTION_FORM = "json.form-" + BuildConfig.LOCALE + "/sample-collection-form.json";
        String SAMPLE_DELIVERY_DETAILS_FORM = "json.form-" + BuildConfig.LOCALE + "/sample-delivery-details-form.json";
        String SUPPORT_INVESTIGATION_FORM = "json.form-" + BuildConfig.LOCALE + "/support-investigation-form.json";
    }

    interface Step {
        String COVID_SCAN_BARCODE_PAGE = "covid_scan_barcode_page";
        String COVID_CONDUCT_RDT_PAGE = "covid_conduct_rdt_page";
        String COVID_RESPIRATORY_SPECIMEN_COLLECTION_OPT_IN_PAGE = "covid_respiratory_specimen_collection_opt_in_page";
        String COVID_COLLECT_RESPIRATORY_SPECIMEN_PAGE = "covid_collect_respiratory_specimen_page";
        String COVID_TEST_COMPLETE_PAGE = "covid_test_complete_page";
        String COVID_AFFIX_RESPIRATORY_SAMPLE_ID_PAGE = "covid_affix_respiratory_sample_id_page";
        String COVID_ONE_SCAN_WIDGET_SPECIMEN_PAGE  = "covid_one_scan_widget_specimen_page";
        String COVID_XRAY_PAGE = "covid_xray_page";
        String COVID_OPT_IN_WBC_PAGE = "covid_opt_in_wbc_page";
        String COVID_WBC_PAGE = "covid_wbc_page";
        String COVID_SUPPORT_INVESTIGATION_COMPLETE_PAGE = "covid_support_investigation_complete_page";
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
    }

    interface RDTType {
        String COVID_WONDFO = "covid19-wondfo";
        String COVID_ALLTEST = "covid19-jalmedical";
    }
}
