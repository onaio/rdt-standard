package io.ona.rdt.robolectric.util;

import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.util.FormKeyTextExtractionUtil;

public class FormKeyTextExtractionUtilTest extends RobolectricTest {

    private static final int DATA_SIZE = 150;

    @Test
    public void testGetFormWidgetKeyToTextMap() throws JSONException {
        Map<String, String> data = FormKeyTextExtractionUtil.getFormWidgetKeyToTextMap();
        Assert.assertFalse(data.isEmpty());
        Assert.assertEquals(DATA_SIZE, data.size());
    }
}
