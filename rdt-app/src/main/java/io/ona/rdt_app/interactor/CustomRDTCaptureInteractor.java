package io.ona.rdt_app.interactor;

import android.content.Context;

import io.ona.rdt_app.callback.OnImageSavedCallback;
import io.ona.rdt_app.domain.CompositeImage;
import io.ona.rdt_app.presenter.CustomRDTCapturePresenter;
import io.ona.rdt_app.util.RDTJsonFormUtils;

/**
 * Created by Vincent Karuri on 27/06/2019
 */
public class CustomRDTCaptureInteractor {

    private CustomRDTCapturePresenter presenter;

    public CustomRDTCaptureInteractor(CustomRDTCapturePresenter presenter) {
        this.presenter = presenter;
    }

    public void saveImage(Context context, CompositeImage compositeImage, OnImageSavedCallback onImageSavedCallBack) {
        RDTJsonFormUtils.saveStaticImagesToDisk(context, compositeImage, onImageSavedCallBack);
    }
}
