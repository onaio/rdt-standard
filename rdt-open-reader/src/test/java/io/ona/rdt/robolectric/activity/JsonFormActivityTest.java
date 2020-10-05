package io.ona.rdt.robolectric.activity;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;

/**
 * Created by Vincent Karuri on 16/09/2020
 */
public abstract class JsonFormActivityTest extends ActivityRobolectricTest {

    protected Intent intent;
    private JSONObject mJSONObject;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mockMethods();
    }

    private void mockMethods() throws JSONException {
        mJSONObject = new JSONObject();
        mJSONObject.put(JsonFormConstants.STEP1, new JSONObject());
        mJSONObject.put(JsonFormConstants.ENCOUNTER_TYPE, "encounter_type");
        intent = new Intent();
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.JSON, mJSONObject.toString());
    }
}
