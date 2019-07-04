package io.ona.rdt_app.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.activity.CustomRDTCaptureActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static edu.washington.cs.ubicomplab.rdt_reader.Constants.SAVED_IMAGE_FILE_PATH;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE;
import static io.ona.rdt_app.util.Constants.Form.TIME_IMG_SAVED;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureFactory extends RDTCaptureFactory {

    private final String TAG = CustomRDTCaptureFactory.class.getName();

    private Context context;
    private JsonFormFragment formFragment;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.context = context;
        this.formFragment = formFragment;
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    private class LaunchRDTCameraTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(com.vijay.jsonwizard.R.string.please_wait_title, com.vijay.jsonwizard.R.string.launching_rdt_capture_message, context);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Activity activity = (Activity) context;
            Intent intent = new Intent(activity, CustomRDTCaptureActivity.class);
            activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            return null;
        }
    }

    @Override
    protected void launchRDTCaptureActivity() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            new LaunchRDTCameraTask().execute();
        }
    }

    private OnActivityResultListener createOnActivityResultListener() {

        OnActivityResultListener resultListener =  new OnActivityResultListener() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                hideProgressDialog();
                final JsonApi jsonApi = (JsonApi) context;
                if (requestCode == JsonFormConstants.RDT_CAPTURE_CODE && resultCode == RESULT_OK) {
                    if (data != null) {
                        try {
                            String[] imgIDAndTimeStamp = data.getExtras().getString(SAVED_IMAGE_FILE_PATH).split(",");
                            jsonApi.writeValue("step4", RDT_CAPTURE, imgIDAndTimeStamp[0], "", "", "", false);
                            jsonApi.writeValue("step5", TIME_IMG_SAVED, imgIDAndTimeStamp[1], "", "", "", false);
                        } catch (JSONException e) {
                            Log.e(TAG, e.getStackTrace().toString());
                        }
                        if (!formFragment.next()) {
                            formFragment.save(true);
                        }
                    } else {
                        Log.i(TAG, "No result data for RDT capture!");
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    ((Activity) context).finish();
                }
            }
        };

        return resultListener;
    }

    @Override
    public void setUpRDTCaptureActivity() {
        super.setUpRDTCaptureActivity();
        if (context instanceof JsonApi) {
            final JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(JsonFormConstants.RDT_CAPTURE_CODE , createOnActivityResultListener());
        }
    }
}
