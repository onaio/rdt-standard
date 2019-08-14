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

    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7, boolean b) throws JSONException {

    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5) throws JSONException {

    }

    @Override
    public void writeValue(String s, String s1, String s2, String s3, String s4, String s5, String s6, String s7) throws JSONException {

    }

    @Override
    public void writeMetaDataValue(String s, Map<String, String> map) throws JSONException {

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

    }

    @Override
    public void onFormFinish() {

    }

    @Override
    public void clearSkipLogicViews() {

    }

    @Override
    public void clearCalculationLogicViews() {

    }

    @Override
    public void clearConstrainedViews() {

    }

    @Override
    public void clearFormDataViews() {

    }

    @Override
    public void addSkipLogicView(View view) {

    }

    @Override
    public void addCalculationLogicView(View view) {

    }

    @Override
    public void addConstrainedView(View view) {

    }

    @Override
    public void refreshHiddenViews(boolean b) {

    }

    @Override
    public void refreshSkipLogic(String s, String s1, boolean b) {

    }

    @Override
    public void refreshCalculationLogic(String s, String s1, boolean b) {

    }

    @Override
    public void invokeRefreshLogic(String s, boolean b, String s1, String s2) {

    }

    @Override
    public void addFormDataView(View view) {

    }

    @Override
    public ArrayList<View> getFormDataViews() {
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

    }

    @Override
    public void addOnActivityResultListener(Integer integer, OnActivityResultListener onActivityResultListener) {

    }

    @Override
    public void addOnActivityRequestPermissionResultListener(Integer integer, OnActivityRequestPermissionResultListener onActivityRequestPermissionResultListener) {

    }

    @Override
    public void removeOnActivityRequestPermissionResultListener(Integer integer) {

    }

    @Override
    public void resetFocus() {

    }

    @Override
    public JSONObject getmJSONObject() {
        return null;
    }

    @Override
    public void setmJSONObject(JSONObject jsonObject) {

    }

    @Override
    public void updateGenericPopupSecondaryValues(JSONArray jsonArray) {

    }

    @Override
    public void registerLifecycleListener(LifeCycleListener lifeCycleListener) {

    }

    @Override
    public void unregisterLifecycleListener(LifeCycleListener lifeCycleListener) {

    }

    @Override
    public void setGenericPopup(GenericPopupDialog genericPopupDialog) {

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

    }

    @Override
    public String getConfirmCloseTitle() {
        return null;
    }

    @Override
    public void setConfirmCloseTitle(String s) {

    }

    @Override
    public void showPermissionDeniedDialog() {

    }
}
