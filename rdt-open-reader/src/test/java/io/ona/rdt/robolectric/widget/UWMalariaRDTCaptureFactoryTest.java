package io.ona.rdt.robolectric.widget;

import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.interfaces.OnActivityResultListener;
import com.vijay.jsonwizard.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.powermock.reflect.Whitebox;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;

import io.ona.rdt.activity.CustomRDTCaptureActivity;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.robolectric.shadow.ContextCompatShadow;
import io.ona.rdt.shadow.DeviceDefinitionProcessorShadow;
import io.ona.rdt.robolectric.shadow.RDTJsonFormUtilsShadow;
import io.ona.rdt.util.Constants;
import io.ona.rdt.util.CovidConstants;
import io.ona.rdt.widget.UWRDTCaptureFactory;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.KEY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.OPENMRS_ENTITY;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.smartregister.util.JsonFormUtils.ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.OPENMRS_ENTITY_ID;
import static org.smartregister.util.JsonFormUtils.OPENMRS_ENTITY_PARENT;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

public class UWMalariaRDTCaptureFactoryTest extends WidgetFactoryRobolectricTest {

    private UWRDTCaptureFactory rdtCaptureFactory;
    private WidgetArgs widgetArgs;
    private String rdtType = "rdt_type";

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        rdtCaptureFactory = new UWRDTCaptureFactory();
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateWithCorrectValues() throws Exception {
        setWidgetArgs();

        LineReadings lineReadings = new LineReadings(true, true, true);
        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withCassetteBoundary("cassette_boundary")
                .withBaseEntityId("base_entity_id")
                .withFlashOn(true)
                .withProviderId("provider_id")
                .withTimeTaken(0)
                .withLineReadings(lineReadings)
                .withFullmageId("full_image_id")
                .withCroppedImageId("cropped_image_id")
                .withImageTimeStamp(0);

        Whitebox.invokeMethod(rdtCaptureFactory, "populateRelevantFields", parcelableImageMetadata);

        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.FormFields.RDT_CAPTURE_TOP_LINE_READING), eq(String.valueOf(lineReadings.isTopLine())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.FormFields.RDT_CAPTURE_MIDDLE_LINE_READING), eq(String.valueOf(lineReadings.isMiddleLine())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.FormFields.RDT_CAPTURE_BOTTOM_LINE_READING), eq(String.valueOf(lineReadings.isBottomLine())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.Test.RDT_CAPTURE_DURATION), eq(String.valueOf(parcelableImageMetadata.getCaptureDuration())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.RDTType.RDT_TYPE), eq(rdtType), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.Test.CROPPED_IMG_ID), eq(parcelableImageMetadata.getCroppedImageId()), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.Test.TIME_IMG_SAVED), eq(String.valueOf(parcelableImageMetadata.getImageTimeStamp())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(JsonFormConstants.RDT_CAPTURE), eq(parcelableImageMetadata.getFullImageId()), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.Test.FLASH_ON), eq(String.valueOf(parcelableImageMetadata.isFlashOn())), eq(""), eq(""), eq(""), eq(false));
        Mockito.verify(jsonFormActivity).writeValue(eq(widgetArgs.getStepName()), eq(Constants.Test.CASSETTE_BOUNDARY), eq(parcelableImageMetadata.getCassetteBoundary()), eq(""), eq(""), eq(""), eq(false));
    }

    @Test
    public void testSetUpRDTCaptureActivity() {
        setWidgetArgs();
        rdtCaptureFactory.setUpRDTCaptureActivity();
        Mockito.verify(jsonFormActivity).addOnActivityResultListener(eq(RDT_CAPTURE_CODE), any(OnActivityResultListener.class));
    }

    @Test
    public void testOnActivityResultShouldWriteCorrectRDTData() throws JSONException {
        setWidgetArgs();
        Intent data = mock(Intent.class);

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withBaseEntityId("base_entity_id")
                .withProviderId("provider_id")
                .withCassetteBoundary("cassette_boundary")
                .withFlashOn(false)
                .withCroppedImageId("cropped_img_id")
                .withFullmageId("full_img_id")
                .withImageTimeStamp(0l)
                .withManualCapture(true)
                .withLineReadings(new LineReadings(false, true, false));
        doReturn(parcelableImageMetadata).when(data).getParcelableExtra(PARCELABLE_IMAGE_METADATA);

        rdtCaptureFactory.onActivityResult(RDT_CAPTURE_CODE , RESULT_OK, data);
        Mockito.verify(jsonFormActivity, atLeastOnce()).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq(false));
        Mockito.verify(widgetArgs.getFormFragment()).next();
    }

    @Test
    public void testOnActivityResultShouldMoveBackOneStepOnCancel() {
        setWidgetArgs();
        rdtCaptureFactory.onActivityResult(BARCODE_REQUEST_CODE, RESULT_CANCELED, mock(Intent.class));
        Mockito.verify((RDTJsonFormFragment) widgetArgs.getFormFragment()).setMoveBackOneStep(eq(true));
    }

    @Config(shadows = {ContextCompatShadow.class, DeviceDefinitionProcessorShadow.class, RDTJsonFormUtilsShadow.class})
    @Test
    public void testGetViewsFromJsonShouldCorrectlyPopulateFields() throws Exception {
        String entityId = "entity_id";

        JSONObject jsonObject = new JSONObject();
        jsonObject.put(ENTITY_ID, entityId);
        jsonObject.put(OPENMRS_ENTITY_ID, "openmrs_entity_id");
        jsonObject.put(OPENMRS_ENTITY, "openmrs_entity");
        jsonObject.put(OPENMRS_ENTITY_PARENT, "openmrs_entity_parent");
        jsonObject.put(KEY, "key");
        jsonObject.put(JsonFormConstants.VALUE, JsonFormConstants.VALUE);
        doReturn(jsonObject).when(jsonFormActivity).getmJSONObject();

        DeviceDefinitionProcessorShadow.setJSONObject(jsonObject);
        RDTJsonFormUtilsShadow.setJsonObject(jsonObject);

        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        rdtCaptureFactory.getViewsFromJson("step1", jsonFormActivity, formFragment,
                jsonObject, mock(CommonListener.class));

        Assert.assertEquals(entityId, Whitebox.getInternalState(rdtCaptureFactory, "baseEntityId"));

        WidgetArgs actualWidgetArgs =  Whitebox.getInternalState(rdtCaptureFactory, "widgetArgs");
        Assert.assertEquals(formFragment, actualWidgetArgs.getFormFragment());
        Assert.assertEquals(jsonObject, actualWidgetArgs.getJsonObject());
        Assert.assertEquals(jsonFormActivity, actualWidgetArgs.getContext());
        Assert.assertEquals("step1", actualWidgetArgs.getStepName());

        // verify rdt capture is launched for rdt with fhir config
        Intent expectedIntent = new Intent(jsonFormActivity, CustomRDTCaptureActivity.class);
        Intent actualIntent = Shadows.shadowOf(RDTApplication.getInstance()).getNextStartedActivity();
        Assert.assertEquals(expectedIntent.getComponent(), actualIntent.getComponent());
        Assert.assertEquals(entityId, actualIntent.getStringExtra(ENTITY_ID));
        Assert.assertEquals(Constants.RDTType.ONA_RDT, actualIntent.getStringExtra(UWRDTCaptureFactory.RDT_NAME));
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
        Assert.assertEquals(Constants.RDTType.ONA_RDT, actualIntent.getStringExtra(UWRDTCaptureFactory.RDT_NAME));
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

    private void setWidgetArgs() {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        doReturn(rdtType).when(jsonFormActivity).getRdtType();

        widgetArgs.withFormFragment(formFragment)
                .withContext(jsonFormActivity)
                .withStepName("step1");

        Whitebox.setInternalState(rdtCaptureFactory, "widgetArgs", widgetArgs);
    }
}
