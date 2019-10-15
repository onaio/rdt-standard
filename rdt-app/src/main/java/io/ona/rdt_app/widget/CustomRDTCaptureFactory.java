package io.ona.rdt_app.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import io.ona.rdt_app.activity.CustomRDTCaptureActivity;
import io.ona.rdt_app.activity.RDTJsonFormActivity;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_CONTROL_RESULT;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_PF_RESULT;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_PV_RESULT;
import static io.ona.rdt_app.util.Constants.Form.RDT_TYPE;
import static io.ona.rdt_app.util.Constants.Test.FULL_IMG_ID_AND_TIME_STAMP;
import static io.ona.rdt_app.util.Constants.Test.RDT_CAPTURE_DURATION;
import static io.ona.rdt_app.util.Constants.Test.TEST_CONTROL_RESULT;
import static io.ona.rdt_app.util.Constants.Test.TEST_PF_RESULT;
import static io.ona.rdt_app.util.Constants.Test.TEST_PV_RESULT;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureFactory extends RDTCaptureFactory {

    private final String TAG = CustomRDTCaptureFactory.class.getName();
    private final String IMAGE_TIMESTAMP_ADDRESS = "image_timestamp_address";
    private final String IMAGE_ID_ADDRESS = "image_id_address";
    private final String RDT_NAME = "rdt_name";

    private String baseEntityId;
    private WidgetArgs widgetArgs;

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
            activity.startActivityForResult(intents[0], JsonFormConstants.RDT_CAPTURE_CODE);
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
            new LaunchRDTCameraTask().execute(intent);
        }
    }

    private OnActivityResultListener createOnActivityResultListener() {

        OnActivityResultListener resultListener =  new OnActivityResultListener() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                hideProgressDialog();
                JSONObject jsonObject = widgetArgs.getJsonObject();
                RDTJsonFormFragment formFragment = (RDTJsonFormFragment) widgetArgs.getFormFragment();
                if (requestCode == JsonFormConstants.RDT_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {
                    try {
                        Bundle extras = data.getExtras();
                        String controlResult = extras.getString(TEST_CONTROL_RESULT);
                        String pvResult = extras.getString(TEST_PV_RESULT);
                        String pfResult = extras.getString(TEST_PF_RESULT);
                        String imageCaptureDuration = extras.getString(RDT_CAPTURE_DURATION);

                        String[] imgIDAndTimeStamp = extras.getString(FULL_IMG_ID_AND_TIME_STAMP).split(",");
                        String imgIdAddress = jsonObject.optString(IMAGE_ID_ADDRESS, "");
                        String imgTimeStampAddress = jsonObject.optString(IMAGE_TIMESTAMP_ADDRESS, "");

                        ImageProcessor.InterpretationResult interpretationResult = new ImageProcessor.InterpretationResult();
                        interpretationResult.topLine = Boolean.valueOf(controlResult);
                        interpretationResult.middleLine = Boolean.valueOf(pvResult);
                        interpretationResult.bottomLine = Boolean.valueOf(pfResult);

                        populateRelevantFields(imgIDAndTimeStamp, imgIdAddress, imgTimeStampAddress, interpretationResult, imageCaptureDuration, (JsonApi) widgetArgs.getContext());
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
        };

        return resultListener;
    }

    private void populateRelevantFields(String[] imgIDAndTimeStamp, String imgIdAddress, String imgTimeStampAddress, ImageProcessor.InterpretationResult testResult, String imgCaptureDuration, JsonApi jsonApi) throws JSONException {
        String[] stepAndId = new String[0];

        stepAndId = imgIdAddress.isEmpty() ? stepAndId : imgIdAddress.split(":");
        if (stepAndId.length == 2) {
            jsonApi.writeValue(stepAndId[0].trim(), stepAndId[1].trim(), imgIDAndTimeStamp[0], "", "", "", false);
        }

        stepAndId = imgTimeStampAddress.isEmpty() ? new String[0] : imgTimeStampAddress.split(":");
        if (stepAndId.length == 2) {
            jsonApi.writeValue(stepAndId[0].trim(), stepAndId[1].trim(), imgIDAndTimeStamp[1], "", "", "", false);
        }

        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_CONTROL_RESULT , String.valueOf(testResult.topLine), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_PV_RESULT, String.valueOf(testResult.middleLine), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_PF_RESULT, String.valueOf(testResult.bottomLine), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_DURATION, imgCaptureDuration, "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_TYPE, ((RDTJsonFormActivity) widgetArgs.getContext()).getRdtType(), "", "", "", false);
    }

    @Override
    public void setUpRDTCaptureActivity() {
        super.setUpRDTCaptureActivity();
        Context context = widgetArgs.getContext();
        if (context instanceof JsonApi) {
            final JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(JsonFormConstants.RDT_CAPTURE_CODE , createOnActivityResultListener());
        }
    }
}
