package io.ona.rdt.robolectric.widget;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.robolectric.Robolectric;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.robolectric.RobolectricTest;

/**
 * Created by Vincent Karuri on 11/08/2020
 */
public abstract class WidgetFactoryRobolectricTest extends RobolectricTest {

    protected RDTJsonFormActivity jsonFormActivity;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        jsonFormActivity = getRDTJsonFormActivityNonStatic();
    }

    @After
    public void tearDown() {
        jsonFormActivity.finish();
    }

    public static RDTJsonFormActivity getRDTJsonFormActivity() throws JSONException {
        return Mockito.spy(Robolectric.buildActivity(RDTJsonFormActivity.class,
                getJsonFormActivityIntent())
                .create()
                .resume()
                .get());
    }

    private static Intent getJsonFormActivityIntent() throws JSONException {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.put(JsonFormConstants.STEP1, new JSONObject());
        mJSONObject.put(JsonFormConstants.ENCOUNTER_TYPE, "encounter_type");
        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.JSON, mJSONObject.toString());
        return intent;
    }

    public RDTJsonFormActivity getRDTJsonFormActivityNonStatic() throws JSONException {
        return Mockito.spy(Robolectric.buildActivity(RDTJsonFormActivity.class,
                getJsonFormActivityIntentNonStatic())
                .create()
                .resume()
                .get());
    }

    private Intent getJsonFormActivityIntentNonStatic() throws JSONException {
        JSONObject mJSONObject = new JSONObject();
        mJSONObject.put(JsonFormConstants.STEP1, getJsonObject());
        mJSONObject.put(JsonFormConstants.ENCOUNTER_TYPE, "encounter_type");
        Intent intent = new Intent();
        intent.putExtra(JsonFormConstants.JSON_FORM_KEY.JSON, mJSONObject.toString());
        return intent;
    }

    protected JSONObject getJsonObject() throws JSONException {
        return new JSONObject();
    }
}
