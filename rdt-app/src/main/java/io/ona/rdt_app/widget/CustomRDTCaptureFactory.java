package io.ona.rdt_app.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONObject;

import java.util.List;

import io.ona.rdt_app.activity.CustomRDTCaptureActivity;

import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureFactory extends RDTCaptureFactory {

    private Context context;
    private String baseEntityId;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.context = context;
        baseEntityId = ((JsonApi) context).getmJSONObject().optString(ENTITY_ID);
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);
        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    private class LaunchRDTCameraTask extends AsyncTask<Intent, Void, Void> {

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
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(context, CustomRDTCaptureActivity.class);
            intent.putExtra(ENTITY_ID, baseEntityId);
            new LaunchRDTCameraTask().execute(intent);
        }
    }
}
