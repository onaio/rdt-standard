package io.ona.rdt.widget.validator;

import android.graphics.Bitmap;

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
        return RDTJsonFormUtils.convertBase64StrToBitmap(widgetArgs.getJsonObject().optString(BASE64_ENCODED_IMG));
    }
}
