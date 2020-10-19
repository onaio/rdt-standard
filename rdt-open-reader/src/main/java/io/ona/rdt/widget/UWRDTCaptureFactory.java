package io.ona.rdt.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.smartregister.util.JsonFormUtils;

import java.util.List;

import androidx.core.content.ContextCompat;
import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.util.DeviceDefinitionProcessor;
import io.ona.rdt.util.RDTJsonFormUtils;
import io.ona.rdt.util.StepStateConfig;
import timber.log.Timber;

import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static edu.washington.cs.ubicomplab.rdt_reader.core.Constants.RDT_JSON_CONFIG;

/**
 * Created by Vincent Karuri on 17/06/2020
 */
public class UWRDTCaptureFactory extends RDTCaptureFactory {

    private final String TAG = UWRDTCaptureFactory.class.getName();
    public static final String RDT_NAME = "rdt_name";

    public static final long CAPTURE_TIMEOUT_MS = 30000;

    public static final String CAPTURE_TIMEOUT = "capture_timeout";

    private String baseEntityId;

    private JSONObject stepStateConfig;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.baseEntityId = ((JsonApi) context).getmJSONObject().optString(JsonFormUtils.ENTITY_ID);
        this.widgetArgs = new WidgetArgs();
        widgetArgs.withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withContext(context)
                .withStepName(stepName);

        stepStateConfig = StepStateConfig.getInstance(context).getStepStateObj();

        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    @Override
    protected void launchRDTCaptureActivity() {
        Context context = widgetArgs.getContext();
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(context, CustomRDTCaptureActivity.class);
            intent.putExtra(JsonFormUtils.ENTITY_ID, baseEntityId);
            intent.putExtra(RDT_NAME, ((RDTJsonFormActivity) context).getRdtType());
            intent.putExtra(CAPTURE_TIMEOUT, CAPTURE_TIMEOUT_MS);
            new LaunchRDTCameraTask().execute(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();

        RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
        if (requestCode == JsonFormConstants.RDT_CAPTURE_CODE && resultCode == Activity.RESULT_OK && data != null) {
            try {
                ParcelableImageMetadata parcelableImageMetadata = data.getParcelableExtra(Constants.Test.PARCELABLE_IMAGE_METADATA);
                populateRelevantFields(parcelableImageMetadata);
                if (!formFragment.next()) {
                    formFragment.save(true);
                }
            } catch (JSONException e) {
                Log.e(TAG, e.getStackTrace().toString());
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            formFragment.setMoveBackOneStep(true);
        } else if (data == null) {
            Log.i(TAG, "No result data for RDT capture!");
        }
    }

    protected void populateRelevantFields(ParcelableImageMetadata parcelableImageMetadata) throws JSONException {
        LineReadings lineReadings = parcelableImageMetadata.getLineReadings();
        JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.FormFields.RDT_CAPTURE_TOP_LINE_READING, String.valueOf(lineReadings.isTopLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.FormFields.RDT_CAPTURE_MIDDLE_LINE_READING, String.valueOf(lineReadings.isMiddleLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.FormFields.RDT_CAPTURE_BOTTOM_LINE_READING, String.valueOf(lineReadings.isBottomLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.RDT_CAPTURE_DURATION, String.valueOf(parcelableImageMetadata.getCaptureDuration()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.RDTType.RDT_TYPE, ((RDTJsonFormActivity) widgetArgs.getContext()).getRdtType(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.CROPPED_IMG_ID, parcelableImageMetadata.getCroppedImageId(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.TIME_IMG_SAVED, String.valueOf(parcelableImageMetadata.getImageTimeStamp()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), JsonFormConstants.RDT_CAPTURE, parcelableImageMetadata.getFullImageId(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.FLASH_ON, String.valueOf(parcelableImageMetadata.isFlashOn()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.CASSETTE_BOUNDARY, parcelableImageMetadata.getCassetteBoundary(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.IS_MANUAL_CAPTURE, String.valueOf(parcelableImageMetadata.isManualCapture()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.CROPPED_IMG_MD5_HASH, String.valueOf(parcelableImageMetadata.getCroppedImageMD5Hash()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), Constants.Test.FULL_IMG_MD5_HASH, String.valueOf(parcelableImageMetadata.getFullImageMD5Hash()), "", "", "", false);
    }

    private class LaunchRDTCameraTask extends AsyncTask<Intent, Void, Void> {

        private Context context = widgetArgs.getContext();

        @Override
        protected void onPreExecute() {
            showProgressDialog(com.vijay.jsonwizard.R.string.please_wait_title, com.vijay.jsonwizard.R.string.launching_rdt_capture_message, context);
        }

        @Override
        protected Void doInBackground(Intent... intents) {
            try {
                Activity activity = (Activity) context;
                JSONObject rdtTypeField = RDTJsonFormUtils.getField(stepStateConfig.getString(CovidConstants.Step.COVID_SELECT_RDT_TYPE_PAGE),
                        Constants.RDTType.RDT_TYPE, context);
                intents[0].putExtra(RDT_JSON_CONFIG, DeviceDefinitionProcessor.getInstance(context)
                        .extractDeviceConfig(rdtTypeField.getString(JsonFormConstants.VALUE)).toString());
                activity.startActivityForResult(intents[0], JsonFormConstants.RDT_CAPTURE_CODE);
            } catch (Exception e) {
                Timber.e(e);
            }
            return null;
        }
    }
}
