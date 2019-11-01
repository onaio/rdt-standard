package io.ona.rdt_app.presenter;

import android.view.View;

import com.vijay.jsonwizard.customviews.GenericPopupDialog;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.interfaces.LifeCycleListener;
import com.vijay.jsonwizard.interfaces.OnActivityRequestPermissionResultListener;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.utils.ValidationStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Vincent Karuri on 14/08/2019
 */
public class JsonApiStub implements JsonApi {
    @Override
    public JSONObject getStep(String s) {
        return null;
    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5, boolean b) throws JSONException {
        // do nothing
    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, boolean b) throws JSONException {
        // do nothing
    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5) throws JSONException {
        // do nothing
    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) throws JSONException {
        // do nothing
    }

    @Override
    public void writeMetaDataValue(String s, Map<String, String> map) throws JSONException {
        // do nothing
    }

    @Override
    public String currentJsonState() {
        return null;
    }

    @Override
    public String getCount() {
        return null;
    }

    @Override
    public void onFormStart() {
        // do nothing
    }

    @Override
    public void onFormFinish() {
        // do nothing
    }

    @Override
    public void clearSkipLogicViews() {
        // do nothing
    }

    @Override
    public void clearCalculationLogicViews() {
        // do nothing
    }

    @Override
    public void clearConstrainedViews() {
        // do nothing
    }

    @Override
    public void clearFormDataViews() {
        // do nothing
    }

    @Override
    public void addSkipLogicView(View view) {
        // do nothing
    }

    @Override
    public void addCalculationLogicView(View view) {
        // do nothing
    }

    @Override
    public void addConstrainedView(View view) {
        // do nothing
    }

    @Override
    public void refreshHiddenViews(boolean b) {
        // do nothing
    }

    @Override
    public void refreshSkipLogic(String s, String s1, boolean b) {
        // do nothing
    }

    @Override
    public void refreshCalculationLogic(String s, String s1, boolean b) {
        // do nothing
    }

    @Override
    public void invokeRefreshLogic(String s, boolean b, String s1, String s2) {
        // do nothing
    }

    @Override
    public void addFormDataView(View view) {
        // do nothing
    }

    @Override
    public ArrayList<View> getFormDataViews() {
        return null;
    }

    @Override
    public View getFormDataView(String s) {
        return null;
    }

    @Override
    public JSONObject getObjectUsingAddress(String[] strings, boolean b) throws JSONException {
        return null;
    }

    @Override
    public JSONObject getObjectUsingAddress(String[] strings, boolean b, JSONObject jsonObject) throws JSONException {
        return null;
    }

    @Override
    public void refreshConstraints(String s, String s1, boolean b) {
        // do nothing
    }

    @Override
    public void addOnActivityResultListener(Integer integer, OnActivityResultListener onActivityResultListener) {
        // do nothing
    }

    @Override
    public void addOnActivityRequestPermissionResultListener(Integer integer, OnActivityRequestPermissionResultListener onActivityRequestPermissionResultListener) {
        // do nothing
    }

    @Override
    public void removeOnActivityRequestPermissionResultListener(Integer integer) {
        // do nothing
    }

    @Override
    public void resetFocus() {
        // do nothing
    }

    @Override
    public JSONObject getmJSONObject() {
        return null;
    }

    @Override
    public void setmJSONObject(JSONObject jsonObject) {
        // do nothing
    }

    @Override
    public void updateGenericPopupSecondaryValues(JSONArray jsonArray) {
        // do nothing
    }

    @Override
    public void registerLifecycleListener(LifeCycleListener lifeCycleListener) {
        // do nothing
    }

    @Override
    public void unregisterLifecycleListener(LifeCycleListener lifeCycleListener) {
        // do nothing
    }

    @Override
    public void setGenericPopup(GenericPopupDialog genericPopupDialog) {
        // do nothing
    }

    @Override
    public Map<String, ValidationStatus> getInvalidFields() {
        return null;
    }

    @Override
    public String getConfirmCloseMessage() {
        return null;
    }

    @Override
    public void setConfirmCloseMessage(String s) {
        // do nothing
    }

    @Override
    public String getConfirmCloseTitle() {
        return null;
    }

    @Override
    public void setConfirmCloseTitle(String s) {
        // do nothing
    }

    @Override
    public void showPermissionDeniedDialog() {
        // do nothing
    }

    @Override
    public boolean displayScrollBars() {
        return false;
    }

    @Override
    public boolean skipBlankSteps() {
        return false;
    }
}
