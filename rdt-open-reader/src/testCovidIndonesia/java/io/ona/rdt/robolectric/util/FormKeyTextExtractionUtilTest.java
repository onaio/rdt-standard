package io.ona.rdt.robolectric.util;

import org.junit.Assert;
import org.junit.Test;

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

    private Map<String, String> getTestFormWidgetKeyToTextMap() {

        Map<String, String> expectedData = new HashMap<>();

        expectedData.put("facility_type", "Di mana pemeriksaan ini dilakukan?");
        expectedData.put("covid_close_contacts_count", "# jumlah kontak erat");
        expectedData.put("what_ppe", "Alat Pelindung Diri (APD) apa yang dipakai saat melakukan perawatan pada pasien suspek/probable/konfirmasi?");

        return expectedData;
    }
}
