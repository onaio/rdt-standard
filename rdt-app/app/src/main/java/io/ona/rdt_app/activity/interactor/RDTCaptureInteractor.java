package io.ona.rdt_app.activity.interactor;

import android.content.Context;

import edu.washington.cs.ubicomplab.rdt_reader.ImageUtil;
import io.ona.rdt_app.activity.presenter.RDTCapturePresenter;

/**
 * Created by Vincent Karuri on 23/05/2019
 */
public class RDTCaptureInteractor {

    RDTCapturePresenter presenter;

    public RDTCaptureInteractor(RDTCapturePresenter presenter) {
        this.presenter = presenter;
    }

    public String saveImage(Context context, byte[] imageByteArray, long timeTaken) {
        return ImageUtil.saveImage(context, imageByteArray, timeTaken);
    }
}
