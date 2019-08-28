package io.ona.rdt_app.widget;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.interfaces.JsonApi;

import org.junit.Before;
import org.junit.Test;
import org.powermock.reflect.Whitebox;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import io.ona.rdt_app.presenter.JsonApiStub;

import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_CONTROL_RESULT;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_PF_RESULT;
import static io.ona.rdt_app.util.Constants.Form.RDT_CAPTURE_PV_RESULT;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Vincent Karuri on 13/08/2019
 */
public class CustomRDTCaptureFactoryTest {

    private CustomRDTCaptureFactory rdtCaptureFactory;

    @Before
    public void setUp() {
        rdtCaptureFactory = new CustomRDTCaptureFactory();
    }

    @Test
    public void testPopulateRelevantFieldsShouldPopulateWithCorrectValues() throws Exception {
        JsonApi jsonApi = spy(new JsonApiStub());
        String[] imgIDAndTimeStamp = new String[]{"image_id", "3894391"};
        String imgIdAddress = "step1:img_id";
        String imgTimeStampAddress = "step1:img_timestamp";

        Whitebox.setInternalState(rdtCaptureFactory, "widgetArgs", new WidgetArgs());

        ImageProcessor.InterpretationResult result = mock(ImageProcessor.InterpretationResult.class);
        result.bottomLine = true;
        result.middleLine = false;
        result.topLine = true;
        Whitebox.invokeMethod(rdtCaptureFactory, "populateRelevantFields", imgIDAndTimeStamp, imgIdAddress, imgTimeStampAddress, result, jsonApi);

        verify(jsonApi).writeValue(eq("step1"), eq("img_id"), eq("image_id"), eq(""), eq(""), eq(""), eq(false));
        verify(jsonApi).writeValue(eq("step1"), eq("img_timestamp"), eq("3894391"), eq(""), eq(""), eq(""), eq(false));

        jsonApi.writeValue(eq("step1"), eq(RDT_CAPTURE_CONTROL_RESULT), eq(String.valueOf(result.topLine)), eq(""), eq(""), eq(""), eq(false));
        jsonApi.writeValue(eq("step1"), eq(RDT_CAPTURE_PV_RESULT), eq(String.valueOf(result.middleLine)), eq(""), eq(""), eq(""), eq(false));
        jsonApi.writeValue(eq("step1"), eq(RDT_CAPTURE_PF_RESULT), eq(String.valueOf(result.bottomLine)), eq(""), eq(""), eq(""), eq(false));
    }
}
