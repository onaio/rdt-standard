package io.ona.rdt_app.interactor;

import android.graphics.Bitmap;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;
import io.ona.rdt_app.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureInteractor {
    CustomRDTCapturePresenter presenter;

    public CustomRDTCaptureInteractor(CustomRDTCapturePresenter presenter) {
        this.presenter = presenter;
    }

    public void saveImage(Bitmap image, String providerId, String baseEntityId, OnImageSavedCallBack onImageSavedCallBack) {
        RDTJsonFormUtils.saveStaticImageToDisk(image, providerId, baseEntityId, onImageSavedCallBack);
    }
}
