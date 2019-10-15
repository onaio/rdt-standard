package io.ona.rdt_app.presenter;

import android.content.Context;

import io.ona.rdt_app.callback.OnImageSavedCallback;
import io.ona.rdt_app.contract.CustomRDTCaptureContract;
import io.ona.rdt_app.domain.ImageMetaData;
import io.ona.rdt_app.interactor.CustomRDTCaptureInteractor;

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

    public void saveImage(Context context, ImageMetaData imageMetaData, OnImageSavedCallback onImageSavedCallBack) {
        interactor.saveImage(context, imageMetaData, onImageSavedCallBack);
    }
}
