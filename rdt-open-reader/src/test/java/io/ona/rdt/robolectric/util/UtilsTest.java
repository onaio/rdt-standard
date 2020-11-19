package io.ona.rdt.robolectric.util;

import android.content.Context;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implements;
import org.robolectric.shadow.api.Shadow;
import org.robolectric.shadows.ShadowToast;
import org.smartregister.client.utils.constants.JsonFormConstants;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.util.JsonFormUtils;
import org.smartregister.util.LangUtils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.util.Utils;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.MalariaRDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 29/07/2020
 */

@Config(shadows = { UtilsTest.NoOpUtilsShadow.class, BaseJobShadow.class })
public class UtilsTest extends RobolectricTest {

    // overrides UtilsShadow for this test
    @Implements(Utils.class)
    public static class NoOpUtilsShadow extends Shadow { }

    @Test
    public void testConvertDateShouldReturnNullDateForNullDateStr() throws Exception {
        String dateStr = null;
        Date result = convertDate(dateStr, OPEN_RDT_DATE_FORMAT);
        assertNull(result);
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateForValidDateFormat() throws Exception {
        Date date = convertDate("201217", "ddMMyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        assertEquals(calendar.get(Calendar.DATE), 20);
        assertEquals(calendar.get(Calendar.MONTH), 11); // month is 0-indexed
        assertEquals(calendar.get(Calendar.YEAR), 2017);
    }

    @Test
    public void testUpdateLocale()  {
        Context context = RuntimeEnvironment.application;
        LangUtils.saveLanguage(context, "en");
        Utils.updateLocale(context);
        Assert.assertEquals("en", context.getResources().getConfiguration().locale.getLanguage());
        LangUtils.saveLanguage(RuntimeEnvironment.application, "id");
        Utils.updateLocale(context);
        Assert.assertEquals("in", context.getResources().getConfiguration().locale.getLanguage());
    }

    @Test
    public void testGetFlexValue() {
        long flexValue = Utils.getFlexValue(3l);
        assertEquals(1, flexValue);
    }

    @Test
    public void testScheduleJobsImmediately() {
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        Utils.scheduleJobsImmediately();
        Assert.assertEquals(RDTSyncSettingsServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
        Assert.assertEquals(PullUniqueIdsServiceJob.TAG, BaseJobShadow.getJobTags().get(1));
    }

    @Test
    public void testScheduleJobsPeriodically() {
        Assert.assertTrue(BaseJobShadow.getJobTags().isEmpty());
        Utils.scheduleJobsPeriodically();
        Assert.assertEquals(RDTSyncSettingsServiceJob.TAG, BaseJobShadow.getJobTags().get(0));
        Assert.assertEquals(PullUniqueIdsServiceJob.TAG, BaseJobShadow.getJobTags().get(1));
    }

    @Test
    public void testIsImageSyncEnabled() {
        Assert.assertTrue(Utils.isImageSyncEnabled());
    }

    @Test
    public void testIsExpiredShouldReturnCorrectStatus() {
        Assert.assertFalse(Utils.isExpired(getDateWithOffset(1)));
        Assert.assertTrue(Utils.isExpired(getDateWithOffset(-1)));
    }

    @Test
    public void testConvertJsonArrToListOfStringsShouldAddAllJsonArrElements() throws JSONException {
        JSONArray jsonArray = new JSONArray();
        jsonArray.put("str1");
        jsonArray.put("str2");
        jsonArray.put("str3");

        Set<String> expectedStrings = new HashSet<>();
        expectedStrings.add("str1");
        expectedStrings.add("str2");
        expectedStrings.add("str3");

        Set<String> actualStrings = new HashSet<>(Utils.convertJsonArrToListOfStrings(jsonArray));
        assertEquals(expectedStrings.size(), actualStrings.size());
        for (String str : actualStrings) {
            Assert.assertTrue(expectedStrings.contains(str));
        }
    }

    @Test
    public void testTableExistsShouldReturnTrueForExistingTableFalseOtherwise() {
        final String TABLE = "table";
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        Cursor cursor = mock(Cursor.class);
        doReturn(1).when(cursor).getCount();
        doReturn(cursor).when(db).rawQuery(eq("SELECT name FROM sqlite_master WHERE type=? AND name=?"),
                any(String[].class));
        Assert.assertTrue(Utils.tableExists(db, TABLE));
        doReturn(0).when(cursor).getCount();
        Assert.assertFalse(Utils.tableExists(db, TABLE));
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateFormat() throws ParseException {
        assertEquals("1990-09-12", Utils.convertDate("12/09/1990", "dd/MM/yyyy", "yyyy-MM-dd"));
    }

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

    @After
    public void tearDown() {
        BaseJobShadow.getJobTags().clear();
    }
}
