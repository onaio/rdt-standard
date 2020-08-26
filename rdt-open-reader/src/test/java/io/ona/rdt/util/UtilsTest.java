package io.ona.rdt.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.reflect.Whitebox;
import org.smartregister.job.BaseJob;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.repository.AllSharedPreferences;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;
import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.MalariaRDTBarcodeFactory.OPEN_RDT_DATE_FORMAT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.verifyStatic;

/**
 * Created by Vincent Karuri on 31/07/2019
 */

@PrepareForTest({RDTApplication.class, PullUniqueIdsServiceJob.class, RDTSyncServiceJob.class, BaseJob.class})
public class UtilsTest extends PowerMockTest {

    private Utils utils;

    @Mock
    private RDTApplication rdtApplication;

    @Mock
    private org.smartregister.Context drishtiContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        utils = new Utils();
    }

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
    public void testUpdateLocale() throws Exception {

        AllSharedPreferences sharedPreferences = PowerMockito.mock(AllSharedPreferences.class);
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        Configuration configuration = mock(Configuration.class);
        DisplayMetrics displayMetrics = mock(DisplayMetrics.class);
        doReturn(BuildConfig.LOCALE).when(sharedPreferences).fetchLanguagePreference();
        doReturn(resources).when(context).getResources();
        doReturn(configuration).when(resources).getConfiguration();
        doReturn(displayMetrics).when(resources).getDisplayMetrics();
        Whitebox.invokeMethod(utils, "updateLocale", context, sharedPreferences);
        verify(configuration).setLocale(any(Locale.class));
        verify(resources).updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    @Test
    public void testGetFlexValue() throws Exception {
        long flexValue = Whitebox.invokeMethod(utils, "getFlexValue", 3l);
        assertEquals(1, flexValue);
    }

    @Test
    public void testScheduleJobsImmediately() throws Exception {
        mockStaticClasses();
        Whitebox.invokeMethod(Utils.class, "scheduleJobsImmediately");

        verifyStatic(BaseJob.class, times(1));
        BaseJob.scheduleJobImmediately(eq(PullUniqueIdsServiceJob.TAG));

        verifyStatic(BaseJob.class, times(1));
        BaseJob.scheduleJobImmediately(eq(RDTSyncSettingsServiceJob.TAG));
    }

    @Test
    public void testScheduleJobsPeriodically() throws Exception {
        mockStaticClasses();
        Whitebox.invokeMethod(Utils.class, "scheduleJobsPeriodically");

        verifyStatic(BaseJob.class, times(1));
        BaseJob.scheduleJob(eq(PullUniqueIdsServiceJob.TAG), eq(BuildConfig.SYNC_INTERVAL_MINUTES),
                eq(Utils.getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES)));

        verifyStatic(BaseJob.class, times(1));
        BaseJob.scheduleJob(eq(RDTSyncSettingsServiceJob.TAG), eq(BuildConfig.SYNC_INTERVAL_MINUTES),
                eq(Utils.getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES)));
    }

    @Test
    public void testIsImageSyncEnabled() throws Exception {
        mockStaticClasses();
        assertFalse(Whitebox.invokeMethod(utils, "isImageSyncEnabled"));
    }

    @Test
    public void testIsExpiredShouldReturnCorrectStatus() {
        assertFalse(Utils.isExpired(getDateWithOffset(1)));
        assertTrue(Utils.isExpired(getDateWithOffset(-1)));
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
            assertTrue(expectedStrings.contains(str));
        }
    }

    @Test
    public void testTableExistsShouldReturnTrueForExistingTableFalseOtherwise() {
        SQLiteDatabase db = mock(SQLiteDatabase.class);
        Cursor cursor = mock(Cursor.class);
        doReturn(1).when(cursor).getCount();
        doReturn(cursor).when(db).rawQuery(eq("SELECT name FROM sqlite_master WHERE type=? AND name=?"),
                any(String[].class));
        assertTrue(Utils.tableExists(db, "table"));
        doReturn(0).when(cursor).getCount();
        assertFalse(Utils.tableExists(db, "table"));
    }

    @Test
    public void testConvertDateShouldReturnCorrectDateFormat() throws ParseException {
        assertEquals("1990-09-12", Utils.convertDate("12/09/1990", "dd/MM/yyyy", "yyyy-MM-dd"));
    }

    private void mockStaticClasses() {
        mockStatic(BaseJob.class);

        mockStatic(RDTApplication.class);
        PowerMockito.when(RDTApplication.getInstance()).thenReturn(rdtApplication);
        PowerMockito.when(rdtApplication.getContext()).thenReturn(drishtiContext);

        AllSharedPreferences allSharedPreferences = mock(AllSharedPreferences.class);
        doReturn("false").when(allSharedPreferences).getPreference(IS_IMG_SYNC_ENABLED);
        doReturn(allSharedPreferences).when(drishtiContext).allSharedPreferences();
    }
}
