package io.ona.rdt.robolectric.shadow;

import android.app.Activity;
import android.content.Context;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 10/08/2020
 */

@Implements(RDTJsonFormUtils.class)
public class RDTJsonFormUtilsShadow {

    private static JSONObject jsonObject;
    private static MockCounter mockCounter;

    @Implementation
    public static void saveStaticImagesToDisk(final Context context, CompositeImage compositeImage, final OnImageSavedCallback onImageSavedCallBack) {
    }

    @Implementation
    public static JSONObject getField(String step, String key, Context context) {
        return jsonObject;
    }

    @Implementation
    public static void showLocationServicesDialog(final Activity activity) {
        getMockCounter().setCount(1);
    }

    public static void setJsonObject(JSONObject jsonObject) {
        RDTJsonFormUtilsShadow.jsonObject = jsonObject;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }

    public static MockCounter getMockCounter() {
        return mockCounter;
    }

    public static void setMockCounter(MockCounter mockCounter) {
        RDTJsonFormUtilsShadow.mockCounter = mockCounter;
    }
}
