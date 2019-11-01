package io.ona.rdt.interactor;

import android.content.Context;

import io.ona.rdt.callback.OnImageSavedCallback;
import io.ona.rdt.domain.CompositeImage;
import io.ona.rdt.presenter.CustomRDTCapturePresenter;
import io.ona.rdt.util.RDTJsonFormUtils;

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
