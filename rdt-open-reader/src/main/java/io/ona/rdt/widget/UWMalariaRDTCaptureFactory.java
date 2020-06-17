package io.ona.rdt.widget;

import com.vijay.jsonwizard.interfaces.JsonApi;

import org.json.JSONException;

import io.ona.rdt.activity.RDTJsonFormActivity;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;

import static com.vijay.jsonwizard.constants.JsonFormConstants.RDT_CAPTURE;
import static io.ona.rdt.util.Constants.Form.RDT_TYPE;
import static io.ona.rdt.util.Constants.FormFields.RDT_CAPTURE_BOTTOM_LINE_RESULT;
import static io.ona.rdt.util.Constants.FormFields.RDT_CAPTURE_MIDDLE_LINE_RESULT;
import static io.ona.rdt.util.Constants.FormFields.RDT_CAPTURE_TOP_LINE_RESULT;
import static io.ona.rdt.util.Constants.Test.CASSETTE_BOUNDARY;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMG_ID;
import static io.ona.rdt.util.Constants.Test.CROPPED_IMG_MD5_HASH;
import static io.ona.rdt.util.Constants.Test.FLASH_ON;
import static io.ona.rdt.util.Constants.Test.FULL_IMG_MD5_HASH;
import static io.ona.rdt.util.Constants.Test.IS_MANUAL_CAPTURE;
import static io.ona.rdt.util.Constants.Test.RDT_CAPTURE_DURATION;
import static io.ona.rdt.util.Constants.Test.TIME_IMG_SAVED;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class UWMalariaRDTCaptureFactory extends UWRDTCaptureFactory {

    @Override
    protected void populateRelevantFields(ParcelableImageMetadata parcelableImageMetadata) throws JSONException {
        LineReadings lineReadings = parcelableImageMetadata.getLineReadings();
        JsonApi jsonApi = (JsonApi) widgetArgs.getContext();
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_TOP_LINE_RESULT, String.valueOf(lineReadings.isTopLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_MIDDLE_LINE_RESULT, String.valueOf(lineReadings.isMiddleLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_BOTTOM_LINE_RESULT, String.valueOf(lineReadings.isBottomLine()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE_DURATION, String.valueOf(parcelableImageMetadata.getCaptureDuration()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_TYPE, ((RDTJsonFormActivity) widgetArgs.getContext()).getRdtType(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), CROPPED_IMG_ID, parcelableImageMetadata.getCroppedImageId(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), TIME_IMG_SAVED, String.valueOf(parcelableImageMetadata.getImageTimeStamp()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), RDT_CAPTURE, parcelableImageMetadata.getFullImageId(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), FLASH_ON, String.valueOf(parcelableImageMetadata.isFlashOn()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), CASSETTE_BOUNDARY, parcelableImageMetadata.getCassetteBoundary(), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), IS_MANUAL_CAPTURE, String.valueOf(parcelableImageMetadata.isManualCapture()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), CROPPED_IMG_MD5_HASH, String.valueOf(parcelableImageMetadata.getCroppedImageMD5Hash()), "", "", "", false);
        jsonApi.writeValue(widgetArgs.getStepName(), FULL_IMG_MD5_HASH, String.valueOf(parcelableImageMetadata.getFullImageMD5Hash()), "", "", "", false);
    }
}
