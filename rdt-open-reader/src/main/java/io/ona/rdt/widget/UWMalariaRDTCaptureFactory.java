package io.ona.rdt.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.vijay.jsonwizard.constants.JsonFormConstants;

/**
 * Created by Vincent Karuri on 18/11/2020
 */
public class UWMalariaRDTCaptureFactory extends UWRDTCaptureFactory {

    protected void launchCamera(Intent intent, Context context) {
        Activity activity = (Activity) context;
        activity.startActivityForResult(intent, JsonFormConstants.RDT_CAPTURE_CODE);
    }
}
