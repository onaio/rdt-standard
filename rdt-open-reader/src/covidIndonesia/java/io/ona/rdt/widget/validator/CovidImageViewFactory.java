package io.ona.rdt.widget.validator;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.widgets.ImageViewFactory;

import io.ona.rdt.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 27/11/2020
 */
public class CovidImageViewFactory extends ImageViewFactory {

    public static final String BASE64_ENCODED_IMG = "base64_encoded_img";

    @Override
    protected Bitmap getBitmap(WidgetArgs widgetArgs) {
        final int degrees = 90;
        return rotateBitmap(RDTJsonFormUtils.convertBase64StrToBitmap(widgetArgs.getJsonObject().optString(BASE64_ENCODED_IMG)), degrees);
    }

    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        if (source == null) {
            return source;
        }
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        final int xyCoordinate = 0;
        return Bitmap.createBitmap(source, xyCoordinate, xyCoordinate, source.getWidth(), source.getHeight(), matrix, true);
    }
}
