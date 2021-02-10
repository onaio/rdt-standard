package io.ona.rdt.robolectric.util;

import com.google.gson.Gson;
import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.powermock.reflect.Whitebox;
import org.smartregister.AllConstants;
import org.smartregister.util.JsonFormUtils;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class FormKeyTextExtractionUtilTest extends RobolectricTest {

    @Test
    public void testGetFormWidgetKeyToTextMapShouldPopulateNonEmptyMap() throws Exception {

        Map<String, String> expectedData = getTestFormWidgetKeyToTextMap();
        Map<String, String> actualData = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();

        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String expectedValue = entry.getValue();
            String actualValue = actualData.get(entry.getKey());
            System.out.println("expected: " + expectedValue + " - actual: " + actualValue);
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    private Map<String, String> getTestFormWidgetKeyToTextMap() throws Exception {
        Map<String, String> formWidgetKeyToTextMap = new HashMap<>();
        JSONObject formJsonObj = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getTranslatedForm", "patient-diagnostics-form.json");
        JSONArray fields = JsonFormUtils.fields(formJsonObj);
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String fieldType = field.getString(JsonFormConstants.TYPE);
            // add main widget text
            String widgetKey = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getWidgetKey", field.optString(JsonFormConstants.KEY));
            String widgetText = Whitebox.invokeMethod(FormKeyTextExtractionUtil.class, "getWidgetText", field);
            if (StringUtils.isNotBlank(widgetText)) {
                formWidgetKeyToTextMap.put(widgetKey, widgetText);
            }
            // for repeating group, use text hint to populate its _count field
            if (JsonFormConstants.REPEATING_GROUP.equals(fieldType)) {
                formWidgetKeyToTextMap.put(widgetKey + "_count",
                        field.getString(RepeatingGroupFactory.REFERENCE_EDIT_TEXT_HINT));
            }
            // add options text
            if (JsonFormConstants.CHECK_BOX.equals(fieldType)
                    || JsonFormConstants.NATIVE_RADIO_BUTTON.equals(fieldType)) {
                JSONArray options = field.getJSONArray(AllConstants.OPTIONS);
                for (int j = 0; j < options.length(); j++) {
                    JSONObject option = options.getJSONObject(j);
                    formWidgetKeyToTextMap.put(option.getString(JsonFormConstants.KEY),
                            option.getString(JsonFormConstants.TEXT));
                }
            }
        }
        return formWidgetKeyToTextMap;
    }

    private Map<String, String> getMockedMap() throws JSONException {
        JSONObject jsonObject = new JSONObject("{\"covid_sample_id\":\"ID Unik Manual?\",\"has_sore_throat\":\"Apakah pasien mengalami sakit tenggorokan?\",\"nonreactive\":\"NonReactive\",\"has_contact_with_ari_patients\":\"Has the patient had exposure to anyone with an Acute Respiratory Infection (ARI) or an Acute Respiratory Disease (ARD) in the past 14 days?\",\"igg_positive\":\"IgG\",\"lbl_affix_respiratory_specimen_label\":\"Tempelkan label secara manual\",\"has_cough\":\"Apakah pasien mengalami batuk?\",\"saliva_collection_kit_brand\":\"Merk kit pengumpul air liur\\/saliva?\",\"lmk\":\"LMK\",\"none\":\"Not wearing PPE\",\"has_local_transmission_area_travel_history\":\"Has the patient travelled or lived in the local transmission area in the past 14 days?\",\"americas\":\"Americas\",\"has_decreased_taste_sensitivity\":\"Apakah pasien mengalami penurunan sensitivitas rasa\\/indera pengecap?\",\"rdt_capture_top_line_reading\":\"Hasil pembacaan garis kontrol pada RDT?\",\"lbl_handle_rdt\":\"Simpan atau buang RDT sesuai dengan protokol yang ditetapkan\",\"masker\":\"Masker NIOSH-N95. AN EU STANDARD FFP2\",\"other_vtm_brand\":\"Sebutkan merek VTM\",\"rdt_capture_bottom_line_reading\":\"Hasil pembacaan garis bawah\\/bottom line pada RDT?\",\"copan\":\"Copan\",\"sampler_name\":\"Nama Pengambil Sampel\",\"has_muscle_pains\":\"Apakah pasien mengalami nyeri otot?\",\"has_contact_with_dead_ard_patients\":\"Has the patient had exposure to anyone with an Acute Respiratory Disease who died in the last 14 days?\",\"patient_health_status\":\"Status kesehatan pasien?\",\"has_close_contact_with_sick_traveller\":\"Apakah pasien memiliki kontak erat dengan seseorang yang menjadi sakit yang baru saja berpergian dari luar negeri dalam 14 hari terakhir?\",\"has_cancer_history\":\"Does the patient have cancer?\",\"lbl_patient_confirmation\":\"Mohon konfirmasi informasi tersebut\",\"other_health_facility_type\":\"Jenis fasilitas lainnya\",\"africa\":\"Africa\",\"antibody\":\"Antibody\",\"lymphocytes\":\"Limfosit \\/uL\",\"what_ppe\":\"What personal protective equipment (PPE) is used when treating a suspect\\/probable\\/confirmed patient?\",\"suspected\":\"Suspected\",\"lbl_collect_new_sample\":\"Mohon ambil sampel lainnya.\",\"lbl_collect_respiratory_sample\":\"Kumpulkan spesimen menggunakan metode yang disetujui.\",\"has_neurological_disease\":\"Apakah pasien memiliki penyakit neurologi atau neuromuskular kronis?\",\"probable\":\"Probable\",\"antigen\":\"Antigen\",\"manufacturing_lot_number\":\"Nomor lot produksi?\",\"is_pregnant_or_post_partum\":\"Apakah pasien sedang hamil atau pasca persalinan?\",\"platelets\":\"Platelets \\/uL\",\"has_contact_with_hospitalized_ard_patients\":\"Has the patient had exposure to anyone with an Acute Respiratory Disease that required hospitalization in the last 14 days?\",\"is_patient_health_worker\":\"Is the patient a health worker?\",\"has_chronic_lung_disease_history\":\"Apakah pasien memiliki penyakit paru-paru kronis?\",\"temp_sensor\":\"Sensor terpicu suhu tinggi?\",\"igm_positive\":\"IgM\",\"has_cold\":\"Apakah pasien mengalami pilek?\",\"fkui\":\"FKUI\",\"invalid\":\"invalid\",\"hospital_sample_number\":\"ID Sampel Rumah Sakit?\",\"western_pacific\":\"Western Pacific\",\"asymptomatic_confirmed\":\"Asymptomatic confirmed\",\"chw_result_antigen\":\"RDT result?\",\"lbl_sample_expired\":\"Sampel telah kedaluwarsa!\",\"covid_close_contacts_count\":\"# of close contacts\",\"is_part_of_ari_cluster\":\"Does the patient belong to a cluster of severe ARI (fever and pneumonia requiring hospital treatment) with no known cause?\",\"has_chronic_heart_disease_history\":\"Apakah pasien memiliki penyakit jantung kronis?\",\"other\":\"Lainnya\",\"discarded\":\"Discarded\",\"rdt_capture_middle_line_reading\":\"Hasil pembacaan garis tengah\\/middle line pada RDT?\",\"lbl_scan_barcode\":\"Pindai kode batang\\/barcode\",\"has_urinary_or_kidney_disease_history\":\"Apakah pasien memiliki riwayat penyakit saluran kemih atau ginjal?\",\"dead\":\"Mati\",\"sputum_collection_tube_brand\":\"Merk kit pengumpul sputum?\",\"doctor_name_label\":\"Nama Petugas Pemeriksa\",\"health_facility_type\":\"Jenis Fasilitas Kesehatan?\",\"lung_xray_result\":\"X-Ray Paru\",\"rdt_type\":\"Tipe RDT?\",\"undergoing_care\":\"Sedang menjalani perawatan\",\"has_chest_pains\":\"Apakah pasien mengalami nyeri dada?\",\"has_other_health_issues\":\"Apakah pasien memiliki masalah kesehatan lain?\",\"patient_name\":\"\",\"leukocytes\":\"Leukosit \\/uL\",\"eastern_mediterranean\":\"Eastern Mediterranean\",\"other_location\":\"Tentukan lokasi\",\"has_international_travel_history\":\"Has the patient travelled internationally in the past 14 days?\",\"europe\":\"Europe\",\"has_chronic_liver_disease_history\":\"Does the patient have chronic liver disease?\",\"performed_aerosol_procedures\":\"Did the patient perform aerosol-generating procedures?\",\"local_area_city\":\"Local area city of travel?\",\"positive\":\"positive\",\"nest\":\"NEST\",\"primary_health_care_center\":\"Puskesmas\",\"patient_info_unique_id\":\"ID Unik\",\"lbl_use_new_test\":\"Mohon gunakan tes yang baru\",\"has_close_contact_with_covid_patient\":\"Has the patient had close contact with confirmed cases of Covid-19 in the past 14 days?\",\"recovered\":\"Sembuh\",\"chw_rdt_type\":\"RDT Type\",\"serum\":\"Serum \\/ Serologi Sputum\",\"rdt_batch_id\":\"Masukkan ID Batch RDT\",\"nasopharyngeal\":\"Nasofaringeal\",\"dead_ard_patient_contacts_count\":\"# of dead ARD patients\",\"local_area_province\":\"Local area province?\",\"traveler\":\"Traveler\",\"lbl_which_rdt\":\"RDT yang mana?\",\"international_country\":\"International country of travel?\",\"other_rdt_type\":\"Jenis RDT lainnya\",\"international_city\":\"International city of travel?\",\"lbl_store_respiratory_specimen\":\"Simpan spesimen dengan aman dalam transport tube yang tersedia.\",\"chw_result_antibody\":\"RDT result?\",\"red_or_rusty\":\"Merah atau merah kecoklatan\",\"patient_info_name\":\"Nama Pasien\",\"office\":\"Kantor\",\"gray_or_black\":\"Abu-abu atau hitam\",\"sample_type\":\"Jenis sampel?\",\"has_shortness_of_breath\":\"Apakah pasien mengalami sesak napas?\",\"negative_screening_result\":\"Negative screening result\",\"gown\":\"Gown\",\"has_chronic_kidney_disease_history\":\"Apakah pasien memiliki penyakit ginjal kronis?\",\"sputum_color\":\"Warna sputum?\",\"has_abnormal_headaches\":\"Apakah pasien mengalami sakit kepala yang tidak normal?\",\"using_ventilator\":\"Apakah pasien menggunakan ventilator?\",\"off_white\":\"Putih pucat, kuning, atau hijau\",\"unique_id\":\"\",\"patient_dob\":\"\",\"capillary\":\"Kapiler\",\"ill\":\"Sakit\",\"lbl_rdt_instructions\":\"Lakukan RDT sesuai dengan petunjuk penggunaannya\",\"has_hypertension\":\"Apakah pasien memiliki hipertensi?\",\"has_nausea\":\"Apakah pasien mengalami mual atau muntah?\",\"ffp3\":\"FFP3\",\"feels_malaised\":\"Does the patient feel malaise?\",\"reactive\":\"Reactive\",\"swab\":\"Swab\",\"goggles\":\"Medical goggles\",\"se_asia\":\"South East Asia\",\"has_diabetes\":\"Apakah pasien memiliki penyakit Diabetes?\",\"lbl_enter_rdt_manually\":\"Masukkan RDT secara manual\",\"patient_case_category\":\"Patient case category?\",\"no\":\"No\",\"i3l\":\"i3L\",\"doctor_phone_number\":\"Nomor Telepon Petugas Pemeriksa?\",\"has_lost_smell\":\"Apakah pasien kehilangan kemampuan untuk mendeteksi aroma\\/bau?\",\"Yes\":\"Ya\",\"hospitalized_ard_patient_contacts_count\":\"# of hospitalized ARD patients\",\"rdt_id\":\"ID Unik Manual?\",\"additional_health_issues\":\"Masukkan penyakit tambahan lainnya\",\"has_close_contact_with_infectious_animal\":\"Apakah pasien memiliki riwayat kontak dengan hewan yang tertular COVID-19 (apabila hewan tertular tersebut telah teridentifikasi)?\",\"quantity_of_serum\":\"Jumlah serum dalam mL?\",\"sputum\":\"Sputum\",\"has_gout_history\":\"Apakah pasien memiliki riwayat penyakit asam urat?\",\"other_sample_type\":\"Sebutkan jenis sampel\",\"serum_type\":\"Pengambilan sampel darah?\",\"negative\":\"negative\",\"close_contact\":\"Close contact\",\"venus\":\"Vena\",\"gloves\":\"Medical gloves\",\"close_ari_patient_contacts_count\":\"# of ARI\\/ARD patients\",\"symptomatic_confirmed\":\"Symptomatic confirmed\",\"manual_expiration_date\":\"Tanggal kedaluwarsa?\",\"exp_date\":\"Tanggal kedaluwarsa?\",\"facility_type\":\"Di mana pemeriksaan ini dilakukan?\",\"hospital\":\"Rumah Sakit\",\"mask\":\"Mask\",\"has_diarrhoea\":\"Apakah pasien mengalami diare?\",\"No\":\"Tidak\",\"gtin\":\"Nomor Barang Perdagangan Global (GTIN)?\",\"lbl_rdt_expired\":\"RDT telah kedaluwarsa!\",\"swab_type\":\"Jenis Swab?\",\"vtm_brand\":\"Merk VTM?\",\"yes\":\"Yes\",\"oropharyngeal\":\"Orofaringeal\",\"no_covid_detected\":\"Tidak terdeteksi COVID-19\",\"zeesan\":\"Zeesan\",\"clear\":\"Jernih\",\"community_health_facility\":\"Puskesmas Kelurahan\\/Polindes\",\"has_fever_history\":\"Does the patient have the history of fever?\",\"gsi_sample_number\":\"ID Sampel GSI?\",\"has_fever\":\"Apakah pasien mengalami demam?\",\"has_abdominal_pain\":\"Does the patient have abdominal pain?\",\"home\":\"Rumah\",\"saliva\":\"Air liur\\/Saliva\",\"lbl_scan_respiratory_specimen_barcode\":\"Pindai kode batang\\/barcode spesimen\",\"lot_no\":\"Nomor lot?\",\"isolation_completed\":\"Isolation completed\",\"patient_info_dob\":\"Tanggal lahir pasien\",\"has_autoimmune_disease_history\":\"Apakah pasien memiliki penyakit autoimun?\",\"international_travel_location\":\"International travel location?\",\"lbl_affix_respiratory_sample_id\":\"Tempelkan label atau tulis ID pada transport tube\",\"clinic\":\"Klinik\",\"public_health_center\":\"Public health center\",\"has_fatigue\":\"Apakah pasien mengalami kelelahan yang tidak normal?\",\"antibody_type\":\"Antibodi?\",\"lbl_test_complete\":\"Simpan spesimen dengan aman dalam transport tube\",\"has_hiv\":\"Apakah pasien memiliki penyakit HIV\\/AIDS?\"}");
        return new Gson().fromJson(jsonObject.toString(), HashMap.class);
    }
}
