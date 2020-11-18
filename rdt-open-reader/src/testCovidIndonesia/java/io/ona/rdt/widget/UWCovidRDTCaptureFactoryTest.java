package io.ona.rdt.widget;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.utils.Utils;

import org.json.JSONObject;
import org.junit.Assert;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.robolectric.Shadows;
import org.robolectric.shadows.ShadowToast;

import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.robolectric.widget.UWRDTCaptureFactoryTest;
import io.ona.rdt.shadow.DeviceDefinitionProcessorShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;

import static org.mockito.Mockito.mock;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;

/**
 * Created by Vincent Karuri on 18/11/2020
 */
public class UWCovidRDTCaptureFactoryTest extends UWRDTCaptureFactoryTest {

    @Override
    protected void verifyCorrectActionsForRDTCapture(String entityId) throws Exception {
        // verify rdt capture is launched for rdt with fhir config
        Intent expectedIntent = new Intent(jsonFormActivity, CustomRDTCaptureActivity.class);
        Intent actualIntent = Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        Assert.assertEquals(entityId, actualIntent.getStringExtra(ENTITY_ID));
        Assert.assertEquals(rdtType, actualIntent.getStringExtra(UWRDTCaptureFactory.RDT_NAME));
        Assert.assertEquals(UWRDTCaptureFactory.CAPTURE_TIMEOUT_MS, actualIntent.getLongExtra(UWRDTCaptureFactory.CAPTURE_TIMEOUT, -1));

        // verify rdt capture is launched for other rdt selection
        DeviceDefinitionProcessorShadow.setJSONObject(new JSONObject());
        jsonObject.put(JsonFormConstants.VALUE, CovidConstants.FormFields.OTHER_KEY);
        RDTJsonFormUtilsShadow.setJsonObject(jsonObject);
        rdtCaptureFactory.getViewsFromJson("step1", jsonFormActivity, formFragment,
                jsonObject, mock(CommonListener.class));
        expectedIntent = new Intent(jsonFormActivity, CustomRDTCaptureActivity.class);
        actualIntent = Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        Assert.assertEquals(entityId, actualIntent.getStringExtra(ENTITY_ID));
        Assert.assertEquals(rdtType, actualIntent.getStringExtra(UWRDTCaptureFactory.RDT_NAME));
        Assert.assertEquals(-1, actualIntent.getLongExtra(UWRDTCaptureFactory.CAPTURE_TIMEOUT, -1));

        // verify that rdt capture is not launched for rdt with no config and other is not selected
        DeviceDefinitionProcessorShadow.setJSONObject(new JSONObject());
        jsonObject.put(JsonFormConstants.VALUE, JsonFormConstants.VALUE);
        RDTJsonFormUtilsShadow.setJsonObject(jsonObject);
        rdtCaptureFactory.getViewsFromJson("step1", jsonFormActivity, formFragment,
                jsonObject, mock(CommonListener.class));
        Assert.assertNull(Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity());
        Mockito.verify(formFragment).setMoveBackOneStep(ArgumentMatchers.eq(true));
        Assert.assertFalse(Utils.getProgressDialog().isShowing());
        Assert.assertNotNull(ShadowToast.getLatestToast());
    }

    @Override
    protected UWRDTCaptureFactory getRdtCaptureFactory() {
        return new UWCovidRDTCaptureFactory();
    }
}
