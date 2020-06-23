package io.ona.rdt.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.core.content.ContextCompat;
import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.fragment.RDTJsonFormFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 17/06/2020
 */
public abstract class UWRDTCaptureFactory extends RDTCaptureFactory {

    private final String TAG = UWRDTCaptureFactory.class.getName();
    private final String RDT_NAME = "rdt_name";

    private static final long CAPTURE_TIMEOUT_MS = 30000;

    public static final String CAPTURE_TIMEOUT = "capture_timeout";

    private String baseEntityId;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.baseEntityId = ((JsonApi) context).getmJSONObject().optString(ENTITY_ID);
        this.widgetArgs = new WidgetArgs();
        widgetArgs.withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withContext(context)
                .withStepName(stepName);

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    private class LaunchRDTCameraTask extends AsyncTask<Intent, Void, Void> {

        private Context context = widgetArgs.getContext();

        @Override
        protected void onPreExecute() {
            showProgressDialog(com.vijay.jsonwizard.R.string.please_wait_title, com.vijay.jsonwizard.R.string.launching_rdt_capture_message, context);
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            Activity activity = (Activity) context;
            activity.startActivityForResult(intents[0], RDT_CAPTURE_CODE);
            return null;
        }
    }

    @Override
    protected void launchRDTCaptureActivity() {
        Context context = widgetArgs.getContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(context, CustomRDTCaptureActivity.class);
            intent.putExtra(ENTITY_ID, baseEntityId);
            intent.putExtra(RDT_NAME, ((RDTJsonFormActivity) context).getRdtType());
            intent.putExtra(CAPTURE_TIMEOUT, CAPTURE_TIMEOUT_MS);
            new LaunchRDTCameraTask().execute(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();

        RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
        if (requestCode == RDT_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {
            try {
                ParcelableImageMetadata parcelableImageMetadata = data.getParcelableExtra(PARCELABLE_IMAGE_METADATA);
                populateRelevantFields(parcelableImageMetadata);
                if (!formFragment.next()) {
                    formFragment.save(true);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getStackTrace().toString());
            }
        } else if (resultCode == RESULT_CANCELED) {
            formFragment.setMoveBackOneStep(true);
        } else if (data == null) {
            Log.i(TAG, "No result data for RDT capture!");
        }
    }

    protected abstract void populateRelevantFields(ParcelableImageMetadata parcelableImageMetadata) throws JSONException;
}
