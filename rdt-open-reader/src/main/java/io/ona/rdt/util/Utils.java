package io.ona.rdt.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;

import androidx.annotation.StringRes;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.vijay.jsonwizard.constants.JsonFormConstants;

import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.domain.Location;
import org.smartregister.domain.UniqueId;
import org.smartregister.job.PullUniqueIdsServiceJob;
import org.smartregister.repository.AllSharedPreferences;
import org.smartregister.util.LangUtils;
import org.smartregister.util.SyncUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.ona.rdt.BuildConfig;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.job.RDTSyncSettingsServiceJob;
import io.ona.rdt.sync.RDTSyncIntentService;
import timber.log.Timber;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt.util.Constants.Config.IS_IMG_SYNC_ENABLED;

/**
 * Created by Vincent Karuri on 16/07/2019
 */
public class Utils {
    public static final ArrayList<String> ALLOWED_LEVELS;
    public static final String DEFAULT_LOCATION_LEVEL = Constants.Tags.LOCATION;

    static {
        ALLOWED_LEVELS = new ArrayList<>();
        ALLOWED_LEVELS.add(DEFAULT_LOCATION_LEVEL);
        ALLOWED_LEVELS.add(Constants.Tags.COUNTRY);
        ALLOWED_LEVELS.add(Constants.Tags.DISTRICT);
        ALLOWED_LEVELS.add(Constants.Tags.DIVISION);
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

        String savedLocale = LangUtils.getLanguage(context);

        Locale locale = new Locale(StringUtils.isNotBlank(savedLocale) ? savedLocale : BuildConfig.LOCALE);
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }

    public static void showProgressDialogInFG(Context context, @StringRes int title, @StringRes int message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            showProgressDialog(message, title, context);
        });
    }

    public static void hideProgressDialogFromFG() {
        new Handler(Looper.getMainLooper()).post(() -> {
            hideProgressDialog();
        });
    }

    public static void showToastInFG(Context context, String message) {
        new Handler(Looper.getMainLooper()).post(() -> {
            com.vijay.jsonwizard.utils.Utils.showToast(context, message);
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

    public static JSONArray convertToJsonArr(String str) {
        try {
            return StringUtils.isBlank(str) ? null : new JSONArray(str);
        } catch (JSONException e) {
            Timber.e("This is not valid JSON!");
            return null;
        }
    }

    public static JSONArray createOptionsBlock(Map<String, String> keyValPairs, String openmrsEntity, String openmrsEntityId, String openmrsEntityParent) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        for (Map.Entry<String, String> entry : keyValPairs.entrySet()) {
            JSONObject option = new JSONObject();
            option.put(JsonFormConstants.KEY, entry.getKey());
            option.put(JsonFormConstants.TEXT, entry.getValue());
            option.put(JsonFormConstants.OPENMRS_ENTITY, openmrsEntity);
            option.put(JsonFormConstants.OPENMRS_ENTITY_ID, openmrsEntityId);
            option.put(JsonFormConstants.OPENMRS_ENTITY_PARENT, openmrsEntityParent);
            jsonArray.put(jsonArray.length(), option);
        }
        return jsonArray;
    }

    public static String getParentLocationId() {
        org.smartregister.Context context = RDTApplication.getInstance().getContext();
        AllSharedPreferences sharedPreferences = context.allSharedPreferences();
        Location location = context.getLocationRepository().getLocationById(sharedPreferences
                .fetchDefaultLocalityId(sharedPreferences.fetchRegisteredANM()));
        return location == null ? null : location.getProperties().getParentId();
    }

    public static String getUniqueId(List<UniqueId> uniqueIds) {
        List<String> ids = new ArrayList<>();
        for (UniqueId uniqueId : uniqueIds) {
            String currUniqueId = uniqueId.getOpenmrsId();
            if (StringUtils.isNotBlank(currUniqueId)) {
                currUniqueId = currUniqueId.replace("-", "");
                ids.add(currUniqueId);
            }
        }

        return org.smartregister.util.Utils.isEmptyCollection(ids) ? "" : ids.get(0);
    }

    public static boolean isValidJSONObject(String str) {
        if (str == null) {
            return false;
        }
        try {
            new JSONObject(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static void verifyUserAuthorization(Context context) {

        UserAuthorizationVerificationTask userAuthorizationVerificationTask = UserAuthorizationVerificationTask.getInstance(context);

        switch (userAuthorizationVerificationTask.getStatus()) {
            case RUNNING:
                return;
            case FINISHED:
                userAuthorizationVerificationTask.destroyInstance();
                userAuthorizationVerificationTask = UserAuthorizationVerificationTask.getInstance(context);
        }

        userAuthorizationVerificationTask.execute();
    }

    public static class UserAuthorizationVerificationTask extends AsyncTask<Void, Void, Void> {

        private static UserAuthorizationVerificationTask INSTANCE;
        private final SyncUtils syncUtils;

        public static UserAuthorizationVerificationTask getInstance(Context context) {
            if (INSTANCE == null) {
                INSTANCE = new UserAuthorizationVerificationTask(context);
            }
            return INSTANCE;
        }

        private UserAuthorizationVerificationTask(Context context) {
            syncUtils = new RDTSyncIntentService.RDTSyncUtils(context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            boolean isUserAuthorized = syncUtils.verifyAuthorization();
            if (!isUserAuthorized) {
                try {
                    syncUtils.logoutUser();
                } catch (Exception ex) {
                    Timber.e(ex);
                }
            }
            return null;
        }

        public void destroyInstance() {
            INSTANCE = null;
        }
    }
}
