package io.ona.rdt_app.listener;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.toolbox.ImageLoader;

import org.smartregister.util.OpenSRPImageListener;
import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import io.ona.rdt_app.application.RDTApplication;
import io.ona.rdt_app.callback.OnImageSavedCallback;
import io.ona.rdt_app.domain.CompositeImage;
import io.ona.rdt_app.domain.ParcelableImageMetadata;
import io.ona.rdt_app.domain.UnParcelableImageMetadata;
import io.ona.rdt_app.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 02/07/2019
 */
public class RDTImageListener extends OpenSRPImageListener {

    public RDTImageListener(ImageView imageView, int defaultImageResId, int errorImageResId) {
        super(imageView, defaultImageResId, errorImageResId);
    }

    public RDTImageListener(ImageView imageView, String entityId, int defaultImageResId, int errorImageResId) {
        super(imageView, entityId, defaultImageResId, errorImageResId);
    }

    public RDTImageListener(RemoteViews remoteView, int imageViewId, int defaultImageResId, int errorImageResId) {
        super(remoteView, imageViewId, defaultImageResId, errorImageResId);
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        Bitmap image = response.getBitmap();
        RDTApplication application = RDTApplication.getInstance();

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withProviderId(application.getContext().allSharedPreferences().fetchRegisteredANM())
                .withBaseEntityId("base_entity_id"); // todo: pass real base entity id here

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(new ImageProcessor.InterpretationResult());

        CompositeImage compositeImage = new CompositeImage();
        compositeImage.withFullImage(image)
                .withParcelableImageMetadata(parcelableImageMetadata)
                .withUnParcelableImageMetadata(unParcelableImageMetadata);

        if (image != null) {
            // todo: this has a default false result
            RDTJsonFormUtils.saveStaticImagesToDisk(application.getApplicationContext(), compositeImage, new OnImageSavedCallback() {
                @Override
                public void onImageSaved(CompositeImage compositeImage) {
                    // do nothing
                }
            });
        }
    }
}

