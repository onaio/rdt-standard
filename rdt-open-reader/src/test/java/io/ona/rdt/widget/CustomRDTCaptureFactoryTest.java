package io.ona.rdt.widget;

import android.content.Intent;

import com.vijay.jsonwizard.activities.JsonFormActivity;
import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;
import com.vijay.jsonwizard.presenters.JsonFormFragmentPresenter;
import com.vijay.jsonwizard.widgets.RDTCaptureFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import edu.washington.cs.ubicomplab.rdt_reader.presenter.RDTCapturePresenter;
import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.fragment.RDTJsonFormFragment;
import io.ona.rdt.stub.JsonApiStub;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.vijay.jsonwizard.constants.JsonFormConstants.BARCODE_CONSTANTS.BARCODE_REQUEST_CODE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE;
import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE_CODE;
import static io.ona.rdt.util.Constants.EXPIRED_PAGE_ADDRESS;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_CONTROL_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_PF_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_PV_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_TYPE;
import static io.ona.rdt.util.Constants.Test.CASSETTE_BOUNDARY;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMG_ID;
import static io.ona.rdt.util.Constants.Test.FLASH_ON;
import static io.ona.rdt.util.Constants.Test.PARCELABLE_IMAGE_METADATA;
import static io.ona.rdt.util.Constants.Test.RDT_CAPTURE_DURATION;
import static io.ona.rdt.util.Constants.Test.TIME_IMG_SAVED;
import static io.ona.rdt.widget.RDTBarcodeFactory.EXPIRATION_DATE_ADDRESS;
import static io.ona.rdt.widget.RDTBarcodeFactory.RDT_ID_ADDRESS;
import static io.ona.rdt.widget.RDTBarcodeFactory.RDT_ID_LBL_ADDRESSES;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.powermock.api.support.membermodification.MemberMatcher.methods;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

/**
 * Created by Vincent Karuri on 13/08/2019
 */

public class CustomRDTCaptureFactoryTest {

    private CustomRDTCaptureFactory rdtCaptureFactory;
    private RDTBarcodeFactory barcodeFactory;
    private WidgetArgs widgetArgs;
    private RDTJsonFormActivity jsonFormActivity;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        rdtCaptureFactory = new CustomRDTCaptureFactory();
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateWithCorrectValues() throws Exception {
        JsonApi jsonApi = spy(new JsonApiStub());

        WidgetArgs widgetArgs = mock(WidgetArgs.class);
        Whitebox.setInternalState(rdtCaptureFactory, "widgetArgs", widgetArgs);

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


        RDTJsonFormActivity jsonFormActivity = mock(RDTJsonFormActivity.class);
        doReturn("rdt_type").when(jsonFormActivity).getRdtType();
        doReturn(jsonFormActivity).when(widgetArgs).getContext();
        doReturn("step1").when(widgetArgs).getStepName();

        Whitebox.invokeMethod(rdtCaptureFactory, "populateRelevantFields", parcelableImageMetadata, jsonApi);

        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_CAPTURE_CONTROL_RESULT), eq(String.valueOf(lineReadings.isTopLine())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_CAPTURE_PV_RESULT), eq(String.valueOf(lineReadings.isMiddleLine())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_CAPTURE_PF_RESULT), eq(String.valueOf(lineReadings.isBottomLine())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_CAPTURE_DURATION), eq(String.valueOf(parcelableImageMetadata.getCaptureDuration())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_TYPE), eq("rdt_type"), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(CROPPED_IMG_ID), eq(parcelableImageMetadata.getCroppedImageId()), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(TIME_IMG_SAVED), eq(String.valueOf(parcelableImageMetadata.getImageTimeStamp())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(RDT_CAPTURE), eq(parcelableImageMetadata.getFullImageId()), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(FLASH_ON), eq(String.valueOf(parcelableImageMetadata.isFlashOn())), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq(widgetArgs.getStepName()), eq(CASSETTE_BOUNDARY), eq(parcelableImageMetadata.getCassetteBoundary()), eq(""), eq(""), eq(""), eq(false));
    }

    @Test
    public void testSetUpRDTCaptureActivity() {
//        JsonFormActivity jsonFormActivity = mock(JsonFormActivity.class);
//        WidgetArgs widgetArgs = new WidgetArgs();
//        widgetArgs.setContext(jsonFormActivity);
//        Whitebox.setInternalState(rdtCaptureFactory, "widgetArgs", widgetArgs);
//        suppress(methods(RDTCaptureFactory.class, "setUpRDTCaptureActivity"));
//        rdtCaptureFactory.setUpRDTCaptureActivity();
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
        verify(jsonFormActivity, atLeastOnce()).writeValue(anyString(), anyString(), anyString(), anyString(), anyString(), anyString(), eq(false));
        verify(widgetArgs.getFormFragment()).next();
    }

    private void setWidgetArgs() {
        widgetArgs = new WidgetArgs();
        RDTJsonFormFragment formFragment = mock(RDTJsonFormFragment.class);
        jsonFormActivity = mock(RDTJsonFormActivity.class);

        widgetArgs.withFormFragment(formFragment)
                .withContext(jsonFormActivity)
                .withStepName("step1");

        Whitebox.setInternalState(rdtCaptureFactory, "widgetArgs", widgetArgs);
    }
}
