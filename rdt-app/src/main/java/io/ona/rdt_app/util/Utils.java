package io.ona.rdt_app.util;

import org.apache.commons.lang3.StringUtils;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.job.SyncServiceJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import io.ona.rdt_app.BuildConfig;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.job.ImageUploadSyncServiceJob;
import io.ona.rdt_app.job.RDTSyncServiceJob;

import static io.ona.rdt_app.util.Constants.IS_IMG_SYNC_ENABLED;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class Utils {
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = Constants.Tags.HEALTH_CENTER;

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add(DEFAULT_LOCATION_LEVEL);
        ALLOWED_LEVELS.add(Constants.Tags.COUNTRY);
        ALLOWED_LEVELS.add(Constants.Tags.PROVINCE);
        ALLOWED_LEVELS.add(Constants.Tags.DISTRICT);
        ALLOWED_LEVELS.add(Constants.Tags.VILLAGE);
    }

    public static void scheduleJobsPeriodically() {
        PullUniqueIdsServiceJob.scheduleJob(PullUniqueIdsServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));

        RDTSyncServiceJob.scheduleJob(RDTSyncServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                        getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));
    }

    public static void scheduleJobsImmediately() {
        PullUniqueIdsServiceJob.scheduleJobImmediately(PullUniqueIdsServiceJob.TAG);
        RDTSyncServiceJob.scheduleJobImmediately(RDTSyncServiceJob.TAG);
    }

    public static boolean isImageSyncEnabled() {
        return Boolean.valueOf(RDTApplication.getInstance().getContext().allSharedPreferences().getPreference(IS_IMG_SYNC_ENABLED));
    }

    private static long getFlexValue(long value) {
        final long MINIMUM_JOB_FLEX_VALUE = 1;
        long minutes = MINIMUM_JOB_FLEX_VALUE;
        if (value > MINIMUM_JOB_FLEX_VALUE) {
            minutes = (long) Math.ceil(value / 3);
        }
        return minutes;
    }

    public static Date convertDate(String dateStr, String format) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(dateStr);
    }
}
