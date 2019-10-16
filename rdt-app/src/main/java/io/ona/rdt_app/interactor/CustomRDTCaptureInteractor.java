package io.ona.rdt_app.interactor;

import android.content.Context;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.domain.ImageMetaData;
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

    public void saveImage(Context context, ImageMetaData imageMetaData, OnImageSavedCallBack onImageSavedCallBack) {
        RDTJsonFormUtils.saveStaticImageToDisk(context, imageMetaData, onImageSavedCallBack);
    }
}
