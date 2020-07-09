package io.ona.rdt.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.PowerMockTest;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncServiceJob;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;

import static io.ona.rdt.TestUtils.getDateWithOffset;
import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;
import static io.ona.rdt.util.Utils.convertDate;
import static io.ona.rdt.widget.MalariaGoogleBarcodeFactory.OPEN_RDT_DATE_FORMAT;
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
        Context context = mock(Context.class);
        Resources resources = mock(Resources.class);
        Configuration configuration = mock(Configuration.class);
        doReturn(resources).when(context).getResources();
        doReturn(configuration).when(resources).getConfiguration();
        Whitebox.invokeMethod(utils, "updateLocale", context);
        verify(configuration).setLocale(any(Locale.class));
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
