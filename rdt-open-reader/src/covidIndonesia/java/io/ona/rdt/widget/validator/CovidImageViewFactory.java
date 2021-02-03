package io.ona.rdt.widget.validator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.vijay.jsonwizard.domain.WidgetArgs;
import com.vijay.jsonwizard.fragments.JsonFormFragment;
import com.vijay.jsonwizard.interfaces.CommonListener;
import com.vijay.jsonwizard.widgets.ImageViewFactory;

import org.json.JSONObject;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import io.ona.rdt.R;
import io.ona.rdt.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 27/11/2020
 */
public class CovidImageViewFactory extends ImageViewFactory {

    public static final String BASE64_ENCODED_IMG = "base64_encoded_img";

    @Override
    public List<View> getViewsFromJson(String stepName, Context context, JsonFormFragment formFragment, JSONObject jsonObject, CommonListener listener, boolean popup) throws Exception {
        List<View> views = super.getViewsFromJson(stepName, context, formFragment, jsonObject, listener, popup);

        View rootLayout = views.get(0);
        ImageView imageView = rootLayout.findViewById(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        imageView.setLayoutParams(layoutParams);

        return views;
    }

    @Override
    protected Bitmap getBitmap(WidgetArgs widgetArgs) {
        return RDTJsonFormUtils.convertBase64StrToBitmap(widgetArgs.getJsonObject().optString(BASE64_ENCODED_IMG));
    }
}
