package io.ona.rdt.interactor;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.util.ReflectionHelpers;
import org.smartregister.domain.Event;
import org.smartregister.domain.Obs;
import org.smartregister.domain.db.EventClient;
import org.smartregister.repository.EventClientRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.emory.mathcs.backport.java.util.Collections;
import io.ona.rdt.domain.PatientHistoryEntry;
import io.ona.rdt.repository.PatientHistoryRepository;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class CovidPatientHistoryActivityInteractorTest extends RobolectricTest {

    private static final String EVENT_JSON = "{\"type\":\"Event\",\"dateCreated\":\"2020-09-03T09:37:31.385Z\",\"serverVersion\":1599125920026,\"identifiers\":{},\"baseEntityId\":\"06891a4f-2625-4d12-a15e-634179df3813\",\"locationId\":\"ae5e8535-6f3f-48cd-b7a3-7f86bba093ea\",\"eventDate\":\"2020-09-02T21:00:00.000Z\",\"eventType\":\"patient_diagnostics\",\"formSubmissionId\":\"004f519a-57ea-409b-ab00-fc7ee5412f07\",\"providerId\":\"indtester1\",\"duration\":0,\"obs\":[{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"facility_type\",\"parentCode\":\"\",\"values\":[\"home\"],\"set\":[],\"formSubmissionField\":\"facility_type\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"patient_age\",\"parentCode\":\"\",\"values\":[\"9\"],\"set\":[],\"formSubmissionField\":\"patient_age\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"patient_sex\",\"parentCode\":\"\",\"values\":[\"male\"],\"set\":[],\"formSubmissionField\":\"patient_sex\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"using_ventilator\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"using_ventilator\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"patient_case_category\",\"parentCode\":\"\",\"values\":[\"without_symptoms\"],\"set\":[],\"formSubmissionField\":\"patient_case_category\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_chronic_heart_disease_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_chronic_heart_disease_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_chronic_lung_disease_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_chronic_lung_disease_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_chronic_kidney_disease_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_chronic_kidney_disease_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_urinary_or_kidney_disease_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_urinary_or_kidney_disease_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_diabetes\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_diabetes\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_gout_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_gout_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_autoimmune_disease_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_autoimmune_disease_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_neurological_disease\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_neurological_disease\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_hiv\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_hiv\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_hypertension\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_hypertension\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_other_health_issues\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_other_health_issues\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"date_of_onset_of_symptoms\",\"parentCode\":\"\",\"values\":[\"03-09-2019\"],\"set\":[],\"formSubmissionField\":\"date_of_onset_of_symptoms\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_fever\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_fever\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_nausea\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_nausea\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_diarrhoea\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_diarrhoea\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_sore_throat\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_sore_throat\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_cough\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_cough\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_cold\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_cold\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_lost_smell\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_lost_smell\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_shortness_of_breath\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_shortness_of_breath\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_decreased_taste_sensitivity\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_decreased_taste_sensitivity\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_abnormal_headaches\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_abnormal_headaches\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_chest_pains\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_chest_pains\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_muscle_pains\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_muscle_pains\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_fatigue\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_fatigue\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_travel_history\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_travel_history\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_close_contact_with_sick_traveller\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_close_contact_with_sick_traveller\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"covid_patients_in_compound\",\"parentCode\":\"\",\"values\":[\"[{\\\"key\\\":\\\"name_label\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts name\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"name\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"phone_number_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts phone number\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"phone_number\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"},\\\"v_numeric\\\":{\\\"value\\\":\\\"true\\\",\\\"err\\\":\\\"Value must be numeric\\\"},\\\"v_max_length\\\":{\\\"value\\\":\\\"13\\\",\\\"err\\\":\\\"Phone number cannot be more than 13 digits\\\"},\\\"v_min_length\\\":{\\\"value\\\":\\\"8\\\",\\\"err\\\":\\\"Phone number cannot be less than 8 digits\\\"}},{\\\"key\\\":\\\"relationship_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Relationship with contact\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"relationship\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"date_of_first_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of first contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of first contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"},{\\\"key\\\":\\\"date_of_last_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of last contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of last contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"}]\"],\"set\":[],\"formSubmissionField\":\"covid_patients_in_compound\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_close_contact_with_covid_patient\",\"parentCode\":\"\",\"values\":[\"[{\\\"key\\\":\\\"name_label\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts name\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"name\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"phone_number_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts phone number\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"phone_number\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"},\\\"v_numeric\\\":{\\\"value\\\":\\\"true\\\",\\\"err\\\":\\\"Value must be numeric\\\"},\\\"v_max_length\\\":{\\\"value\\\":\\\"13\\\",\\\"err\\\":\\\"Phone number cannot be more than 13 digits\\\"},\\\"v_min_length\\\":{\\\"value\\\":\\\"8\\\",\\\"err\\\":\\\"Phone number cannot be less than 8 digits\\\"}},{\\\"key\\\":\\\"relationship_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Relationship with contact\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"relationship\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"date_of_first_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of first contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of first contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"},{\\\"key\\\":\\\"date_of_last_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of last contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of last contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"}]\"],\"set\":[],\"formSubmissionField\":\"has_close_contact_with_covid_patient\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_close_contact_with_someone_outside_household\",\"parentCode\":\"\",\"values\":[\"[{\\\"key\\\":\\\"name_label\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts name\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"name\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"phone_number_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Contacts phone number\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"phone_number\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"},\\\"v_numeric\\\":{\\\"value\\\":\\\"true\\\",\\\"err\\\":\\\"Value must be numeric\\\"},\\\"v_max_length\\\":{\\\"value\\\":\\\"13\\\",\\\"err\\\":\\\"Phone number cannot be more than 13 digits\\\"},\\\"v_min_length\\\":{\\\"value\\\":\\\"8\\\",\\\"err\\\":\\\"Phone number cannot be less than 8 digits\\\"}},{\\\"key\\\":\\\"relationship_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Relationship with contact\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"relationship\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}},{\\\"key\\\":\\\"date_of_first_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of first contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of first contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"},{\\\"key\\\":\\\"date_of_last_contact\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"date_picker\\\",\\\"hint\\\":\\\"Date of last contact\\\",\\\"expanded\\\":false,\\\"duration\\\":{\\\"label\\\":\\\"Date of last contact\\\"},\\\"min_date\\\":\\\"today-100y\\\",\\\"max_date\\\":\\\"today\\\"},{\\\"key\\\":\\\"location_lbl\\\",\\\"type\\\":\\\"label\\\",\\\"text\\\":\\\"Location of contact\\\",\\\"text_color\\\":\\\"#000000\\\"},{\\\"key\\\":\\\"location\\\",\\\"openmrs_entity_parent\\\":\\\"\\\",\\\"openmrs_entity\\\":\\\"\\\",\\\"openmrs_entity_id\\\":\\\"\\\",\\\"type\\\":\\\"edit_text\\\",\\\"v_required\\\":{\\\"value\\\":\\\"false\\\",\\\"err\\\":\\\"\\\"}}]\"],\"set\":[],\"formSubmissionField\":\"has_close_contact_with_someone_outside_household\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_close_contact_with_infectious_animal\",\"parentCode\":\"\",\"values\":[\"No\"],\"set\":[],\"formSubmissionField\":\"has_close_contact_with_infectious_animal\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"has_close_contact_with_someone_outside_household_count\",\"parentCode\":\"\",\"values\":[\"4\"],\"set\":[],\"formSubmissionField\":\"has_close_contact_with_someone_outside_household_count\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_deb1441d0edf4766af8d397e9b9a1feb\",\"parentCode\":\"\",\"values\":[\"886998888\"],\"set\":[],\"formSubmissionField\":\"phone_number_deb1441d0edf4766af8d397e9b9a1feb\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_07041aa96a0b48c08eec9dbc673ef0c8\",\"parentCode\":\"\",\"values\":[\"88888999\"],\"set\":[],\"formSubmissionField\":\"phone_number_07041aa96a0b48c08eec9dbc673ef0c8\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_bde54e6822e64e51a47d37eb7910ffc4\",\"parentCode\":\"\",\"values\":[\"88888888\"],\"set\":[],\"formSubmissionField\":\"phone_number_bde54e6822e64e51a47d37eb7910ffc4\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_bf5e61d7443248f8b6b77599e5fd7573\",\"parentCode\":\"\",\"values\":[\"58888888\"],\"set\":[],\"formSubmissionField\":\"phone_number_bf5e61d7443248f8b6b77599e5fd7573\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_c8b2fc7b33ea4957bc7f109062f6506c\",\"parentCode\":\"\",\"values\":[\"58899998\"],\"set\":[],\"formSubmissionField\":\"phone_number_c8b2fc7b33ea4957bc7f109062f6506c\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_ee32f86a3d45462596931fd7c41c14dd\",\"parentCode\":\"\",\"values\":[\"88555555\"],\"set\":[],\"formSubmissionField\":\"phone_number_ee32f86a3d45462596931fd7c41c14dd\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_68334a8736754685a74e9542e76d6767\",\"parentCode\":\"\",\"values\":[\"8888558555\"],\"set\":[],\"formSubmissionField\":\"phone_number_68334a8736754685a74e9542e76d6767\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_9c0dbf815ea54d0193db29e08f6f7559\",\"parentCode\":\"\",\"values\":[\"88889985\"],\"set\":[],\"formSubmissionField\":\"phone_number_9c0dbf815ea54d0193db29e08f6f7559\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"formsubmissionField\",\"fieldDataType\":\"text\",\"fieldCode\":\"phone_number_60b6cdd7b8c344c082d59385019a3ca7\",\"parentCode\":\"\",\"values\":[\"887775555\"],\"set\":[],\"formSubmissionField\":\"phone_number_60b6cdd7b8c344c082d59385019a3ca7\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"concept\",\"fieldDataType\":\"start\",\"fieldCode\":\"163137AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"2020-09-03 12:08:28\"],\"set\":[],\"formSubmissionField\":\"start\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"concept\",\"fieldDataType\":\"end\",\"fieldCode\":\"163138AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"2020-09-03 12:10:53\"],\"set\":[],\"formSubmissionField\":\"end\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldType\":\"concept\",\"fieldDataType\":\"deviceid\",\"fieldCode\":\"163149AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\",\"parentCode\":\"\",\"values\":[\"352121115004895\"],\"set\":[],\"formSubmissionField\":\"deviceid\",\"humanReadableValues\":[],\"saveObsAsArray\":false},{\"fieldCode\":\"phone_os_version\",\"values\":[\"9\"],\"set\":[],\"formSubmissionField\":\"phone_os_version\",\"saveObsAsArray\":false},{\"fieldCode\":\"app_version\",\"values\":[\"v1.4.0-COVID-ID\"],\"set\":[],\"formSubmissionField\":\"app_version\",\"saveObsAsArray\":false},{\"fieldCode\":\"phone_manufacturer\",\"values\":[\"samsung\"],\"set\":[],\"formSubmissionField\":\"phone_manufacturer\",\"saveObsAsArray\":false},{\"fieldCode\":\"phone_model\",\"values\":[\"SM-A107F\"],\"set\":[],\"formSubmissionField\":\"phone_model\",\"saveObsAsArray\":false}],\"entityType\":\"\",\"details\":{\"formVersion\":\"\",\"appVersionName\":\"1.14.2.0kfsajdhskjd32-SNAPSHOT\"},\"version\":1599124253400,\"teamId\":\"752c8d0e-3572-4d0e-b90a-cd0a51819164\",\"team\":\"indonesia-team-1\",\"_id\":\"8ae30372-cc9f-4c42-901c-9157acd01e61\",\"_rev\":\"v2\"}";
    private static final String PATIENT_KEY = "test_patient";
    private static final String PATIENT_VALUE = "Test Patient";

    private CovidPatientHistoryActivityInteractor interactor;

    private PatientHistoryRepository patientHistoryRepository;
    private Map<String, String> formWidgetKeyToTextMap;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        interactor = new CovidPatientHistoryActivityInteractor();

        EventClientRepository repository = new EventClientRepository();
        Event event = repository.convert(EVENT_JSON, Event.class);
        EventClient eventClient = new EventClient(event);

        patientHistoryRepository = Mockito.mock(PatientHistoryRepository.class);
        ReflectionHelpers.setField(interactor, "patientHistoryRepository", patientHistoryRepository);

        Mockito.doReturn(eventClient).when(patientHistoryRepository).getEvent(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(), ArgumentMatchers.anyString());

        formWidgetKeyToTextMap = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();
    }

    @Test
    public void testGetPatientHistoryEntries() throws Exception {
        List<PatientHistoryEntry> list = interactor.getPatientHistoryEntries("", "", "");

        Assert.assertNotNull(list);

        for (PatientHistoryEntry entry : list) {
            Assert.assertNotEquals("", entry.getValue());
            if ("Dimana assessment ini dilakukan?".equals(entry.getKey())) {
                Assert.assertEquals("rumah", entry.getValue());
            }
        }

        int fieldCount = 0;
        EventClient eventClient = patientHistoryRepository.getEvent("", "", "");
        if (eventClient != null) {
            Event event = eventClient.getEvent();
            for (Obs obs : event.getObs()) {
                if (!((boolean) Whitebox.invokeMethod(interactor, "shouldAddObs", obs, event))) {
                    continue;
                }
                String text = formWidgetKeyToTextMap.get(obs.getFormSubmissionField());
                if (StringUtils.isNotBlank(text)) {
                    fieldCount += 1;
                }
            }
        }

        Assert.assertEquals(fieldCount, list.size());
    }

    @Test
    public void testShouldAddObs() throws Exception {
        Obs obs = new Obs();
        obs.setFormSubmissionField("rdt_id");
        List<Obs> list = new ArrayList<>();
        list.add(obs);
        Event event = new Event();
        event.setObs(list);

        boolean result1 = Whitebox.invokeMethod(interactor, "shouldAddObs", obs, event);
        Assert.assertTrue(result1);

        obs.setFormSubmissionField("has_close_contact_with_someone_outside_household_count");

        boolean result2 = Whitebox.invokeMethod(interactor, "shouldAddObs", obs, event);
        Assert.assertTrue(result2);

        obs.setValues(Collections.singletonList("4"));

        Obs obs1 = new Obs();
        obs1.setFormSubmissionField("has_close_contact_with_someone_outside_household");

        boolean result3 = Whitebox.invokeMethod(interactor, "shouldAddObs", obs1, event);
        Assert.assertFalse(result3);
    }

    @Test
    public void testGetValues() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("travel_location", "");

        Obs obs = new Obs();
        obs.setKeyValPairs(map);

        String values = Whitebox.invokeMethod(interactor, "getValues", obs, formWidgetKeyToTextMap);

        Assert.assertEquals("travel_location", values);
    }

    @Test
    public void testGetValue() throws Exception {
        formWidgetKeyToTextMap.put(PATIENT_KEY, "");

        Obs obs = new Obs();
        obs.setValue(PATIENT_VALUE);

        String value = Whitebox.invokeMethod(interactor, "getValue", PATIENT_KEY, formWidgetKeyToTextMap, obs);
        Assert.assertEquals(PATIENT_VALUE, value);
    }

    @Override
    public void tearDown() {
        super.tearDown();
        FormKeyTextExtractionUtil.destroyFormWidgetKeyToTextMap();
    }
}
