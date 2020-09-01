package io.ona.rdt.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.TypedValue;

import androidx.annotation.StringRes;

import com.google.firebase.crashlytics.FirebaseCrashlytics;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.smartregister.job.PullUniqueIdsServiceJob;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;

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
        RDTSyncSettingsServiceJob.scheduleJob(RDTSyncSettingsServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));

        PullUniqueIdsServiceJob.scheduleJob(PullUniqueIdsServiceJob.TAG, BuildConfig.SYNC_INTERVAL_MINUTES,
                getFlexValue(BuildConfig.SYNC_INTERVAL_MINUTES));
    }

    public static void scheduleJobsImmediately() {
        RDTSyncSettingsServiceJob.scheduleJobImmediately(RDTSyncSettingsServiceJob.TAG);
        PullUniqueIdsServiceJob.scheduleJobImmediately(PullUniqueIdsServiceJob.TAG);
    }

    public static boolean isImageSyncEnabled() {
        return Boolean.valueOf(RDTApplication.getInstance().getContext().allSharedPreferences().getPreference(IS_IMG_SYNC_ENABLED));
    }

    public static long getFlexValue(long value) {
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

    public static String convertDate(String dateStr, String originalFormat, String targetFormat) throws ParseException {
        if (StringUtils.isEmpty(dateStr)) {
            return null;
        }
        DateFormat originalDate = new SimpleDateFormat(originalFormat, Locale.ENGLISH);
        DateFormat targetDate = new SimpleDateFormat(targetFormat);
        Date date = originalDate.parse(dateStr);
        return targetDate.format(date);
    }

    public static int convertDpToPixels(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void updateLocale(Context context) {

        String savedLocale = RDTApplication.getInstance().getContext().allSharedPreferences().getPreference(CovidConstants.Locale.LOCALE);

        if (StringUtils.isNotBlank(savedLocale)) {
            Locale locale = new Locale(savedLocale.length() > 0 ? savedLocale : BuildConfig.LOCALE);
            Resources resources = context.getResources();
            Configuration configuration = resources.getConfiguration();
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        }
    }

    public static void showProgressDialogInFG(Activity activity, @StringRes int title, @StringRes int message) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProgressDialog(message, title, activity);
            }
        });
    }

    public static void hideProgressDialogFromFG(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        });
    }

    public static boolean isExpired(Date expirationDate) {
        return new Date().after(expirationDate);
    }

    public static List<String> convertJsonArrToListOfStrings(JSONArray jsonArray) throws JSONException {
        List<String> strings = new ArrayList<>();
        if (jsonArray == null) { return  strings; }
        for (int i = 0; i < jsonArray.length(); i++) {
            strings.add(jsonArray.getString(i));
        }
        return strings;
    }

    public static void recordExceptionInCrashlytics(Throwable throwable) {
        FirebaseCrashlytics.getInstance().recordException(throwable);
    }

    public static void logEventToCrashlytics(String eventMessage) {
        FirebaseCrashlytics.getInstance().log(eventMessage);
    }

    public static boolean tableExists(SQLiteDatabase db, String tableName) {
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type=? AND name=?", new String[]{"table", tableName});
        boolean tableExists = !isEmptyCursor(cursor);
        if (cursor != null) {
            cursor.close();
        }
        return tableExists;
    }

    public static boolean isEmptyCursor(Cursor cursor) {
        return cursor == null || cursor.getCount() == 0;
    }
}
