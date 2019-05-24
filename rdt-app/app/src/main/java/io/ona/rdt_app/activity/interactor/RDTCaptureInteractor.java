package io.ona.rdt_app.activity.interactor;

import android.content.Context;

import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.activity.presenter.RDTCapturePresenter;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public class RDTCaptureInteractor {

    RDTCapturePresenter presenter;

    public RDTCaptureInteractor(RDTCapturePresenter presenter) {
        this.presenter = presenter;
    }

    public void saveImage(Context context, byte[] imageByteArray, long timeTaken, OnImageSavedCallBack onImageSavedCallBack) {
        ImageUtil.saveImage(context, imageByteArray, timeTaken, onImageSavedCallBack);
    }
}
