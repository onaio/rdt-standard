package io.ona.rdt.widget;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.powermock.reflect.Whitebox;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.presenter.JsonApiStub;

import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_CONTROL_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_PF_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_CAPTURE_PV_RESULT;
import static io.ona.rdt.util.Constants.Form.RDT_TYPE;
import static io.ona.rdt.util.Constants.Test.CASSETTE_BOUNDARY;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMG_ID;
import static io.ona.rdt.util.Constants.Test.FLASH_ON;
import static io.ona.rdt.util.Constants.Test.RDT_CAPTURE_DURATION;
import static io.ona.rdt.util.Constants.Test.TIME_IMG_SAVED;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class CustomRDTCaptureFactoryTest {

    private CustomRDTCaptureFactory rdtCaptureFactory;

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
}
