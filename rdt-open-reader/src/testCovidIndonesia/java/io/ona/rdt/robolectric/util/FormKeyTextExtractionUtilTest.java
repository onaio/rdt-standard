package io.ona.rdt.robolectric.util;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.widgets.RepeatingGroupFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
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
            Assert.assertEquals(expectedValue, actualValue);
        }
    }

    private Map<String, String> getTestFormWidgetKeyToTextMap() throws Exception {
        Map<String, String> expectedData = new HashMap<>();
        expectedData.put("no", "Tidak");
        expectedData.put("other", "Lainnya");
        expectedData.put("discarded", "Discarded");
        expectedData.put("traveler", "Pelaku Perjalanan");
        expectedData.put("office", "Kantor");
        expectedData.put("none", "Tidak memakai APD");
        expectedData.put("negative_screening_result", "Skrining negatif");
        expectedData.put("gown", "Gown");
        expectedData.put("close_contact", "Kontak erat");
        expectedData.put("gloves", "Sarung tangan");
        expectedData.put("masker", "Masker NIOSH-N95. AN EU STANDARD FFP2");
        expectedData.put("symptomatic_confirmed", "Konfirmasi dengan gejala");
        expectedData.put("facility_type", "Di mana pemeriksaan ini dilakukan?");
        expectedData.put("hospital", "Rumah Sakit");
        expectedData.put("mask", "Masker medis");
        expectedData.put("what_ppe", "Alat Pelindung Diri (APD) apa yang dipakai saat melakukan perawatan pada pasien suspek/probable/konfirmasi?");
        expectedData.put("yes", "Ya");
        expectedData.put("suspected", "Suspek");
        expectedData.put("probable", "Probabel");
        expectedData.put("ffp3", "FFP3");
        expectedData.put("home", "Rumah");
        expectedData.put("feels_malaised", "Apakah pasien merasakan malaise?");
        expectedData.put("isolation_completed", "Selesai Isolasi");
        expectedData.put("goggles", "Kacamata pelindung");
        expectedData.put("asymptomatic_confirmed", "Konfirmasi tanpa gejala");
        expectedData.put("clinic", "Klinik");
        expectedData.put("public_health_center", "Puskesmas");
        expectedData.put("covid_close_contacts_count", "# jumlah kontak erat");
        expectedData.put("patient_case_category", "Kategori Kasus Pasien?");

        return expectedData;
    }
}
