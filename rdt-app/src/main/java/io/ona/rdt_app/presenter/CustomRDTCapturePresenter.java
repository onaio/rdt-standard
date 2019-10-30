package io.ona.rdt_app.presenter;

import android.content.Context;

import org.opencv.core.Point;

import io.ona.rdt_app.callback.OnImageSavedCallback;
import io.ona.rdt_app.contract.CustomRDTCaptureContract;
import io.ona.rdt_app.domain.CompositeImage;
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
}
