package io.ona.rdt.listener;

import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.android.volley.toolbox.ImageLoader;

import org.smartregister.util.OpenSRPImageListener;

import edu.washington.cs.ubicomplab.rdt_reader.core.RDTInterpretationResult;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.UnParcelableImageMetadata;
import io.ona.rdt.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 02/07/2019
 */

public class RDTImageListener extends OpenSRPImageListener {


    public RDTImageListener(ImageView imageView, String entityId, int defaultImageResId, int errorImageResId) {
        super(imageView, entityId, defaultImageResId, errorImageResId);
    }

    @Override
    public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
        Bitmap image = response.getBitmap();
        RDTApplication application = RDTApplication.getInstance();

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withProviderId(application.getContext().allSharedPreferences().fetchRegisteredANM())
                .withBaseEntityId("base_entity_id"); // todo: pass real base entity id here

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(new RDTInterpretationResult());

        CompositeImage compositeImage = new CompositeImage();
        compositeImage.withFullImage(image)
                .withParcelableImageMetadata(parcelableImageMetadata)
                .withUnParcelableImageMetadata(unParcelableImageMetadata);

        if (image != null) {
            // todo: this has a default false result
            RDTJsonFormUtils.saveStaticImagesToDisk(application.getApplicationContext(), compositeImage, compositeImage1 -> {
                // do nothing
            });
        }
    }
}

