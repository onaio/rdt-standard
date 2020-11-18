package io.ona.rdt.robolectric.util;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.shadows.ShadowToast;
import org.smartregister.client.utils.constants.JsonFormConstants;
import org.smartregister.util.JsonFormUtils;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.util.Utils;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 29/07/2020
 */
public class UtilsTest extends RobolectricTest {

    @Test
    public void testRecordExceptionInCrashlyticsShouldRecordException() {
        Throwable throwable = mock(Throwable.class);
        Utils.recordExceptionInCrashlytics(throwable);
        verify(FirebaseCrashlytics.getInstance()).recordException(eq(throwable));
    }

    @Test
    public void testLogEventToCrashlyticsShouldLogEvent() {
        String message = "message";
        Utils.logEventToCrashlytics(message);
        verify(FirebaseCrashlytics.getInstance()).log(eq(message));
    }

    @Test
    public void testShowToastInFGShouldShowToast() {
        Utils.showToastInFG(RuntimeEnvironment.application, "message");
        Assert.assertNotNull(ShadowToast.getLatestToast());
    }

    @Test
    public void testConvertToJsonArrShouldConvertJsonArrIfValid() {
        Assert.assertNull(Utils.convertToJsonArr(""));
        Assert.assertNull(Utils.convertToJsonArr(null));

        String jsonArray = "[{}]";
        Assert.assertEquals(jsonArray, Utils.convertToJsonArr(jsonArray).toString());
    }

    @Test
    public void testCreateOptionsBlock() throws JSONException {
        Map<String, String> keyValPairs = new HashMap<>();
        keyValPairs.put("option1", "val1");
        keyValPairs.put("option2", "val2");
        keyValPairs.put("option3", "val3");

        JSONArray jsonArray = Utils.createOptionsBlock(keyValPairs, "entity", "entity_id");
        final int three = 3;
        Assert.assertEquals(three, jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            String key = jsonArray.getJSONObject(i).getString(JsonFormUtils.KEY);
            String value = jsonArray.getJSONObject(i).getString(JsonFormConstants.TEXT);
            Assert.assertEquals(keyValPairs.get(key), value);
        }
    }

    @Test
    public void testGetParentLocationIdShouldGetCorrectParentLocationId() {
        RDTApplication.getInstance().getContext().allSharedPreferences().saveDefaultLocalityId("", "");
        Assert.assertEquals(OpenSRPContextShadow.PARENT_LOCATION_ID, Utils.getParentLocationId());
    }
}
