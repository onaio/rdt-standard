package io.ona.rdt.presenter;

import android.content.Context;

import org.opencv.core.Point;

import edu.washington.cs.ubicomplab.rdt_reader.ImageProcessor;
import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.contract.CustomRDTCaptureContract;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.domain.LineReadings;
import io.ona.rdt.domain.ParcelableImageMetadata;
import io.ona.rdt.domain.UnParcelableImageMetadata;
import io.ona.rdt.interactor.CustomRDTCaptureInteractor;

import static io.ona.rdt.util.RDTJsonFormUtils.convertByteArrayToBitmap;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCapturePresenter {

    private CustomRDTCaptureContract.View  activity;
    private CustomRDTCaptureInteractor interactor;

    public CustomRDTCapturePresenter(CustomRDTCaptureContract.View activity) {
        this.activity = activity;
        this.interactor = new CustomRDTCaptureInteractor(this);
    }

    public void saveImage(Context context, CompositeImage compositeImage, OnImageSavedCallback onImageSavedCallBack) {
        interactor.saveImage(context, compositeImage, onImageSavedCallBack);
    }

    public String formatPoints(Point[] points) {
        String result = "";
        if (points == null) {
            return result;
        }

        for (Point point : points) {
            result += "(" + point.x + ", " + point.y + "), ";
        }

        return result.substring(0, result.length() - 2);
    }

    public CompositeImage buildCompositeImage(ImageProcessor.CaptureResult captureResult,
                                               ImageProcessor.InterpretationResult interpretationResult,
                                               long timeTaken) {

        final byte[] fullImage = ImageUtil.matToRotatedByteArray(captureResult.resultMat);
        byte[] croppedImage = fullImage;

        UnParcelableImageMetadata unParcelableImageMetadata = new UnParcelableImageMetadata();
        unParcelableImageMetadata.withInterpretationResult(interpretationResult);

        ParcelableImageMetadata parcelableImageMetadata = new ParcelableImageMetadata();
        parcelableImageMetadata.withBaseEntityId(activity.getBaseEntityId())
                .withProviderId(RDTApplication.getInstance().getContext().allSharedPreferences().fetchRegisteredANM())
                .withTimeTaken(timeTaken)
                .withFlashOn(captureResult.flashEnabled)
                .withLineReadings(new LineReadings(false, false, false))
                .withCassetteBoundary("(0, 0), (0, 0), (0, 0), (0, 0)");

        if (!activity.isManualCapture()) {
            croppedImage = ImageUtil.matToRotatedByteArray(interpretationResult.resultMat);
            unParcelableImageMetadata.withBoundary(captureResult.boundary.toArray());
            parcelableImageMetadata.withLineReadings(new LineReadings(interpretationResult.topLine, interpretationResult.middleLine, interpretationResult.bottomLine))
                    .withCassetteBoundary(formatPoints(unParcelableImageMetadata.getBoundary()));
        }


        CompositeImage compositeImage = new CompositeImage();
        compositeImage.withFullImage(convertByteArrayToBitmap(fullImage))
                .withCroppedImage(convertByteArrayToBitmap(croppedImage))
                .withParcelableImageMetadata(parcelableImageMetadata)
                .withUnParcelableImageMetadata(unParcelableImageMetadata);

        return compositeImage;
    }
}
