package io.ona.rdt.util;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import java.util.List;

/**
 * Created by Vincent Karuri on 07/07/2020
 */
public class OneScanHelper {

    private Activity mActivity;
    private String mPackageName;
    private String mActivityName;
    private int mRequestCode;

    private final SparseArray<IntentResultCallback> mCallbacks = new SparseArray<>();

    private static final String TAG = "OneScanScanner";
    private static final String PACKAGE_NAME = "com.temptimecorp.onescan";
    private static final String ACTIVITY_NAME = "CameraActivityLolipop";
    private static final String ACTION_GET_VERSION = "action.version";
    private static final String ACTION_SCANNER = "action.scanner";

    public OneScanHelper(Activity activity) {
        mActivity = activity;
        mPackageName = PACKAGE_NAME;
        mActivityName = PACKAGE_NAME + "." + ACTIVITY_NAME;
        mRequestCode = 1234;
    }

    private void send(String action, Bundle extras, IntentResultCallback callback) {
        Intent intent = new Intent();
        intent.setClassName(mPackageName, mActivityName);
        intent.putExtra("Action", action);
        intent.putExtra("Extras", extras);
        if (!isCallable(mActivity, intent)) {
            Log.e(TAG, "Intent is not Callable!");
            if (callback != null) {
                callback.onResult(Activity.RESULT_CANCELED, null);
            }
            return;
        }
        if (callback != null) {
            mActivity.startActivityForResult(intent, mRequestCode);
            mCallbacks.put(mRequestCode, callback);
            ++mRequestCode;
        } else {
            mActivity.startActivity(intent);
        }
    }

    public void send(Request request, IntentResultCallback callback) {
        send(request.action, request.getBundle(), callback);
    }

    private static boolean isCallable(Activity activity, Intent intent) {
        List<ResolveInfo> list = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public void doActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResultCallback callback = mCallbacks.get(requestCode);
        if (callback != null) {
            Bundle bundle = null;
            if (resultCode == Activity.RESULT_OK)
                bundle = data.getBundleExtra("Extras");
            callback.onResult(resultCode, bundle);
            mCallbacks.delete(requestCode);
        }
    }

    /****** Internal Types *******/

    interface IntentResultCallback {
        void onResult(int resultCode, Bundle bundle);
    }

    private static class Request {
        private String action;

        Request(String action) {
            this.action = action;
        }

        Bundle getBundle() {
            return null;
        }
    }

    public static class VersionRequest extends Request {
        VersionRequest() {
            super(OneScanHelper.ACTION_GET_VERSION);
        }
    }

    public static class VersionResponse {
        public String version;

        VersionResponse(Bundle bundle) {
            version = bundle.getString("version");
        }
    }

    public static class ScanRequest extends Request {
        public String reader;
        public String title;
        public String clientId;
        public String token;
        public String appName;
        public String appVersion;
        public String appUserName;

        ScanRequest() {
            super(OneScanHelper.ACTION_SCANNER);
        }

        @Override
        Bundle getBundle() {
            Bundle bundle = new Bundle();
            bundle.putString("reader", reader);
            bundle.putString("title", title);
            bundle.putString("clientId", clientId);
            bundle.putString("token", token);
            bundle.putString("appName", appName);
            bundle.putString("appVersion", appVersion);
            bundle.putString("appUserName", appUserName);
            return bundle;
        }
    }

    public static class ScanResponse {
        public String status;
        public String barcodeText;
        public String productId;
        public String lot;
        public String expirationDate;
        public String serialNumber;
        public String additionalIdentifier;
        public boolean sensorTriggered;

        ScanResponse (Bundle bundle) {
            status = bundle.getString("status");
            barcodeText = bundle.getString("barcodeText");
            sensorTriggered = bundle.getBoolean("sensorTriggered");
            productId = bundle.getString("productId");
            lot = bundle.getString("lot");
            expirationDate = bundle.getString("expirationDate");
            serialNumber = bundle.getString("serialNumber");
            additionalIdentifier = bundle.getString("additionalIdentifier");
        }
    }
}
