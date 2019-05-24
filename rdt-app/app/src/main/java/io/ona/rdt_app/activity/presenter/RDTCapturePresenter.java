package io.ona.rdt_app.activity.presenter;

import android.content.Context;

import edu.washington.cs.ubicomplab.rdt_reader.callback.OnImageSavedCallBack;
import io.ona.rdt_app.activity.RDTCaptureActivity;
import io.ona.rdt_app.activity.interactor.RDTCaptureInteractor;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public class RDTCapturePresenter {

    private RDTCaptureActivity activity;
    private RDTCaptureInteractor interactor;

    public RDTCapturePresenter(RDTCaptureActivity activity) {
        this.activity = activity;
        this.interactor = new RDTCaptureInteractor(this);
    }

    public void saveImage(Context context, byte[] imageByteArray, long timeTaken, OnImageSavedCallBack onImageSavedCallBack) {
        interactor.saveImage(context, imageByteArray, timeTaken, onImageSavedCallBack);
    }
}
