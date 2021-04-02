package io.ona.rdt.robolectric.util;

import android.content.Context;
import android.os.AsyncTask;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.ReflectionHelpers;
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

import io.ona.rdt.TestUtils;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;
import io.ona.rdt.robolectric.RobolectricTest;
import io.ona.rdt.robolectric.shadow.BaseJobShadow;
import io.ona.rdt.robolectric.shadow.OpenSRPContextShadow;
import io.ona.rdt.util.Utils;
import io.ona.rdt.widget.MalariaRDTBarcodeFactory;

import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 29/07/2020
 */

@Config(shadows = { BaseJobShadow.class })
public class UtilsTest extends RobolectricTest {

    @After
    public void tearDown() {
        BaseJobShadow.getJobTags().clear();
    }

    @Test
    public void testConvertDateShouldReturnNullDateForNullDateStr() throws Exception {
        String dateStr = null;
        Date result = Utils.convertDate(dateStr, MalariaRDTBarcodeFactory.OPEN_RDT_DATE_FORMAT);
        Assert.assertNull(result);
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateForValidDateFormat() throws Exception {
        Date date = Utils.convertDate("201217", "ddMMyy");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        final int day = 20;
        final int mth = 11;
        final int yr = 2017;

        Assert.assertEquals(calendar.get(Calendar.DATE), day);
        Assert.assertEquals(calendar.get(Calendar.MONTH), mth); // month is 0-indexed
        Assert.assertEquals(calendar.get(Calendar.YEAR), yr);
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
        final long flexVal = 3l;
        long flexValue = Utils.getFlexValue(flexVal);
        Assert.assertEquals(1, flexValue);
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
        Assert.assertFalse(Utils.isExpired(TestUtils.getDateWithOffset(1)));
        Assert.assertTrue(Utils.isExpired(TestUtils.getDateWithOffset(-1)));
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
        Assert.assertEquals(expectedStrings.size(), actualStrings.size());
        for (String str : actualStrings) {
            Assert.assertTrue(expectedStrings.contains(str));
        }
    }

    @Test
    public void testTableExistsShouldReturnTrueForExistingTableFalseOtherwise() {
        final String TABLE = "table";
        SQLiteDatabase db = Mockito.mock(SQLiteDatabase.class);
        Cursor cursor = Mockito.mock(Cursor.class);
        Mockito.doReturn(1).when(cursor).getCount();
        Mockito.doReturn(cursor).when(db).rawQuery(ArgumentMatchers.eq("SELECT name FROM sqlite_master WHERE type=? AND name=?"),
                ArgumentMatchers.any(String[].class));
        Assert.assertTrue(Utils.tableExists(db, TABLE));
        Mockito.doReturn(0).when(cursor).getCount();
        Assert.assertFalse(Utils.tableExists(db, TABLE));
    }

    @Test
    public void testConvertDateShouldReturnNullForInvalidDateFormat() throws Exception {
        String dateStr = null;
        String result = Utils.convertDate(dateStr, "dd/MM/yyyy", "yyyy-MM-dd");
        Assert.assertNull(result);
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateFormat() throws ParseException {
        Assert.assertEquals("1990-09-12", Utils.convertDate("12/09/1990", "dd/MM/yyyy", "yyyy-MM-dd"));
    }

    @Test
    public void testRecordExceptionInCrashlyticsShouldRecordException() {
        Throwable throwable = Mockito.mock(Throwable.class);
        Utils.recordExceptionInCrashlytics(throwable);
        verify(FirebaseCrashlytics.getInstance()).recordException(ArgumentMatchers.eq(throwable));
    }

    @Test
    public void testLogEventToCrashlyticsShouldLogEvent() {
        String message = "message";
        Utils.logEventToCrashlytics(message);
        verify(FirebaseCrashlytics.getInstance()).log(ArgumentMatchers.eq(message));
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

        JSONArray jsonArray = Utils.createOptionsBlock(keyValPairs, "entity", "entity_id", "");
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

    @Test
    public void testIsValidJSONObjectShouldReturnCorrectStatus() {
        Assert.assertTrue(Utils.isValidJSONObject(new JSONObject().toString()));
        Assert.assertFalse(Utils.isValidJSONObject(null));
        Assert.assertFalse(Utils.isValidJSONObject(""));
    }

    @Test
    public void testVerifyUserAuthorizationShouldExecuteAuthorizationTask() {

        // verify pending state
        Utils.UserAuthorizationVerificationTask userAuthorizationVerificationTask = Mockito.mock(Utils.UserAuthorizationVerificationTask.class);
        ReflectionHelpers.setStaticField(Utils.UserAuthorizationVerificationTask.class, "INSTANCE", userAuthorizationVerificationTask);
        Mockito.when(userAuthorizationVerificationTask.getStatus()).thenReturn(AsyncTask.Status.PENDING);
        Utils.verifyUserAuthorization(RDTApplication.getInstance());
        Mockito.verify(userAuthorizationVerificationTask, Mockito.times(1)).execute();

        Mockito.when(userAuthorizationVerificationTask.getStatus()).thenReturn(AsyncTask.Status.RUNNING);
        Mockito.verify(userAuthorizationVerificationTask, Mockito.times(1)).execute();

        // verify finished state
        Mockito.when(userAuthorizationVerificationTask.getStatus()).thenReturn(AsyncTask.Status.FINISHED);
        Utils.verifyUserAuthorization(RDTApplication.getInstance());
        Mockito.verify(userAuthorizationVerificationTask, Mockito.times(1)).destroyInstance();
        Mockito.verify(userAuthorizationVerificationTask, Mockito.times(2)).execute();
        ReflectionHelpers.setStaticField(Utils.UserAuthorizationVerificationTask.class, "INSTANCE", null);
    }

}
