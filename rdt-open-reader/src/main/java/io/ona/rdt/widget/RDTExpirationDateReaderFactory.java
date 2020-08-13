package io.ona.rdt.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.core.content.ContextCompat;
import io.ona.rdt.R;
import io.ona.rdt.activity.RDTExpirationDateActivity;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.util.StepStateConfig;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static com.vijay.jsonwizard.utils.Utils.hideProgressDialog;
import static com.vijay.jsonwizard.utils.Utils.showProgressDialog;
import static io.ona.rdt.util.Constants.RDTType.ONA_RDT;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE;
import static io.ona.rdt.util.Constants.Result.EXPIRATION_DATE_RESULT;
import static io.ona.rdt.util.Constants.Step.MANUAL_EXPIRATION_DATE_ENTRY_PAGE;
import static io.ona.rdt.util.Constants.Step.PRODUCT_EXPIRED_PAGE;

/**
 * Created by Vincent Karuri on 21/06/2019
 */
public class RDTExpirationDateReaderFactory implements FormWidgetFactory, OnActivityResultListener {

    public static final String EXPIRATION_DATE_CAPTURE = "expiration_date_capture";
    private static final String TAG = RDTExpirationDateReaderFactory.class.getName();
    private long expirationDateCaptureTimeout = 15000;
    private final String EXPIRATION_DATE_CAPTURE_TIMEOUT_KEY = "expiration_date_capture_timeout";

    private WidgetArgs widgetArgs;
    private View rootLayout;
    private StepStateConfig stepStateConfig;

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        this.widgetArgs = new WidgetArgs();
        widgetArgs.withStepName(stepName)
                .withContext(context)
                .withFormFragment(formFragment)
                .withJsonObject(jsonObject)
                .withListener(listener)
                .withPopup(popup);

        this.rootLayout = LayoutInflater.from(context).inflate(getLayout(), null);
        this.stepStateConfig = RDTApplication.getInstance().getStepStateConfiguration();
        this.expirationDateCaptureTimeout = jsonObject.optLong(EXPIRATION_DATE_CAPTURE_TIMEOUT_KEY, expirationDateCaptureTimeout);

        addWidgetTags();
        setUpRDTExpirationDateActivity();
        launchRDTExpirationDateActivity();
        moveToNextStepDelayed();

        List<View> views = new ArrayList<>(1);
        views.add(rootLayout);

        return views;
    }

    private void moveToNextStepDelayed() {
        String manualExpPage = stepStateConfig.getStepStateObj().optString(MANUAL_EXPIRATION_DATE_ENTRY_PAGE, "");
        if (!manualExpPage.isEmpty()) {
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                if (widgetArgs.getFormFragment().isVisible()) {
                    JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(manualExpPage);
                    widgetArgs.getFormFragment().transactThis(nextFragment);
                    finishExpirationDateActivity();
                }
            }, expirationDateCaptureTimeout);
        }
    }

    private void finishExpirationDateActivity() {
        Activity rdtExpirationDateActivity = RDTApplication.getInstance().getCurrentActivity();
        rdtExpirationDateActivity.setResult(RESULT_OK);
        rdtExpirationDateActivity.finish();
    }

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener) throws Exception {
        return getViewsFromJson(stepName, context, formFragment, jsonObject, listener, false);
    }

    private void addWidgetTags() throws JSONException {
        JSONObject jsonObject = widgetArgs.getJsonObject();
        String openMrsEntityParent = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY_PARENT);
        String openMrsEntity = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY);
        String openMrsEntityId = jsonObject.getString(JsonFormConstants.OPENMRS_ENTITY_ID);
        String key = jsonObject.getString(JsonFormConstants.KEY);

        rootLayout.setTag(com.vijay.jsonwizard.R.id.key, key);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent, openMrsEntityParent);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity, openMrsEntity);
        rootLayout.setTag(com.vijay.jsonwizard.R.id.openmrs_entity_id, openMrsEntityId);
    }

    private void populateRelevantFields(String value) throws JSONException {
        String key = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.key);
        String openMrsEntityParent = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_parent);
        String openMrsEntity = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity);
        String openMrsEntityId = (String) rootLayout.getTag(com.vijay.jsonwizard.R.id.openmrs_entity_id);
        ((JsonApi) widgetArgs.getContext()).writeValue(widgetArgs.getStepName(), key, value, openMrsEntityParent,
                openMrsEntity, openMrsEntityId, widgetArgs.isPopup());
    }

    public static void conditionallyMoveToNextStep(JsonFormFragment formFragment, String expiredPageStep, boolean isExpired) {
        if (isExpired) {
            JsonFormFragment nextFragment = RDTJsonFormFragment.getFormFragment(expiredPageStep);
            formFragment.transactThis(nextFragment);
        } else {
            moveToNextOrSave(formFragment);
        }
    }

    private static void moveToNextOrSave(JsonFormFragment formFragment) {
        if (!formFragment.next()) {
            formFragment.save(true);
        }
    }

    public void setUpRDTExpirationDateActivity() {
        Context context = widgetArgs.getContext();
        if (context instanceof JsonApi) {
            final JsonApi jsonApi = (JsonApi) context;
            jsonApi.addOnActivityResultListener(RDT_CAPTURE_CODE, this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        hideProgressDialog();
        if (requestCode == RDT_CAPTURE_CODE && resultCode == RESULT_OK && data != null) {
            try {
                Boolean isExpired = data.getExtras().getBoolean(EXPIRATION_DATE_RESULT);
                String expirationDate = data.getExtras().getString(EXPIRATION_DATE);
                populateRelevantFields(expirationDate);
                conditionallyMoveToNextStep(widgetArgs.getFormFragment(),
                        stepStateConfig.getStepStateObj().optString(PRODUCT_EXPIRED_PAGE),
                        isExpired);
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

    private class LaunchRDTCameraTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            showProgressDialog(com.vijay.jsonwizard.R.string.please_wait_title, R.string.launching_expiration_date_widget, widgetArgs.getContext());
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Activity activity = (Activity) widgetArgs.getContext();
            Intent intent = new Intent(activity, RDTExpirationDateActivity.class);
            activity.startActivityForResult(intent, RDT_CAPTURE_CODE);
            return null;
        }
    }

    private void launchRDTExpirationDateActivity() {
        if (ContextCompat.checkSelfPermission(widgetArgs.getContext(), Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            new LaunchRDTCameraTask().execute();
        }
    }

    protected int getLayout() {
        return com.vijay.jsonwizard.R.layout.native_form_rdt_capture;
    }

    @Override
    public Set<String> getCustomTranslatableWidgetFields() {
        return new HashSet<>();
    }
}
