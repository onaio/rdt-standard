package io.ona.rdt_app.presenter;

import android.graphics.Bitmap;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.activity.CustomRDTCaptureActivity;
import io.ona.rdt_app.interactor.CustomRDTCaptureInteractor;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCapturePresenter {
    private CustomRDTCaptureActivity activity;
    private CustomRDTCaptureInteractor interactor;

    public CustomRDTCapturePresenter(CustomRDTCaptureActivity activity) {
        this.activity = activity;
        this.interactor = new CustomRDTCaptureInteractor(this);
    }

    public void saveImage(Bitmap image, String providerId, String baseEntityId, OnImageSavedCallBack onImageSavedCallBack) {
        interactor.saveImage(image, providerId, baseEntityId, onImageSavedCallBack);
    }
}
