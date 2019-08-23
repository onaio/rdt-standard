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
import android.view.LayoutInflater;
import android.view.View;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.FormWidgetFactory;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.ona.rdt_app.R;
import io.ona.rdt_app.activity.RDTExpirationDateActivity;
import io.ona.rdt_app.activity.RDTJsonFormActivity;
import io.ona.rdt_app.fragment.RDTJsonFormFragment;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt_app.util.Constants.EXPIRATION_DATE;
import static io.ona.rdt_app.util.Constants.EXPIRATION_DATE_RESULT;
import static io.ona.rdt_app.util.Constants.EXPIRED_PAGE_ADDRESS;
import static io.ona.rdt_app.util.Constants.ONA_RDT;

/**
 * Created by Vincent Karuri on 21/06/2019
 */
public class RDTExpirationDateReaderFactory implements FormWidgetFactory {

    public static final String EXPIRATION_DATE_CAPTURE = "expiration_date_capture";

    private static final String TAG = RDTExpirationDateReaderFactory.class.getName();

    private WidgetArgs widgetArgs;
    private View rootLayout;
    private JSONObject jsonObject;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.jsonObject = jsonObject;
        this.widgetArgs = new WidgetArgs();
        widgetArgs.withStepName(stepName)
                .withContext(context)
                .withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withListener(listener)
                .withPopup(popup);

        this.rootLayout = LayoutInflater.from(context).inflate(getLayout(), null);

        addWidgetTags(jsonObject);
        setUpRDTExpirationDateActivity();
        launchRDTExpirationDateActivity();

        List<View> views = new ArrayList<>(1);
        views.add(rootLayout);

        return views;
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    private void addWidgetTags(JSONObject jsonObject) throws JSONException {
        String openMrsEntityParent = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY_PARENT);
        String openMrsEntity = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY);
        String openMrsEntityId = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY_ID);
        String key = jsonObject.getString(JsonFormConstants.KEY);

        rootLayout.setTag(com.vijay.jsonwizard.R.id.key, key);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent, openMrsEntityParent);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity, openMrsEntity);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_id, openMrsEntityId);
    }


    private OnActivityResultListener createOnActivityResultListener() {

        OnActivityResultListener resultListener =  new OnActivityResultListener() {

            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                hideProgressDialog();
                if (requestCode == JsonFormConstants.RDT_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {
                    try {
                        final JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
                        Boolean isNotExpired = data.getExtras().getBoolean(EXPIRATION_DATE_RESULT);
                        String expirationDate = data.getExtras().getString(EXPIRATION_DATE);
                        populateRelevantFields(jsonApi, expirationDate);
                        conditionallyMoveToNextStep(isNotExpired);
                    } catch (JSONException e) {
                        Log.e(TAG, e.getStackTrace().toString());
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    ((RDTJsonFormActivity) widgetArgs.getContext()).setRdtType(ONA_RDT);
                    ((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(true);
                } else if (data == null) {
                    Log.i(TAG, "No result data for expiration date capture!");
                }
            }
        };

        return resultListener;
    }

    private void populateRelevantFields(JsonApi jsonApi, String value) throws JSONException {
        String key = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.key);
        String openMrsEntityParent = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent);
        String openMrsEntity = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity);
        String openMrsEntityId = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_id);
        jsonApi.writeValue(widgetArgs.getStepName(), key, value, openMrsEntityParent,
                openMrsEntity, openMrsEntityId, widgetArgs.isPopup());
    }

    private void conditionallyMoveToNextStep(boolean isNotExpired) {
        JsonFormFragment formFragment = widgetArgs.getFormFragment();
        if (isNotExpired) {
            if (!formFragment.next()) {
                formFragment.save(true);
            }
        } else {
            String expiredPageAddr = jsonObject.optString(EXPIRED_PAGE_ADDRESS, "step2");
            JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(expiredPageAddr);
            formFragment.transactThis(nextFragment);
        }
    }

    public void setUpRDTExpirationDateActivity() {
        Context context = widgetArgs.getContext();
        if (context instanceof JsonApi) {
            final JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(JsonFormConstants.RDT_CAPTURE_CODE, createOnActivityResultListener());
        }
    }

    private class LaunchRDTCameraTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(com.vijay.jsonwizard.R.string.please_wait_title, R.string.launching_expiration_date_widget, widgetArgs.getContext());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Activity activity = (Activity) widgetArgs.getContext();
            Intent intent = new Intent(activity, RDTExpirationDateActivity.class);
            activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
            return null;
        }
    }

    private void launchRDTExpirationDateActivity() {
        if (ContextCompat.checkSelfPermission(widgetArgs.getContext(), Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            new LaunchRDTCameraTask().execute();
        }
    }

    protected int getLayout() {
        return com.vijay.jsonwizard.R.layout.native_form_rdt_capture;
    }
}
