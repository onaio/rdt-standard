package io.ona.rdt.presenter;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;

import io.ona.rdt.BuildConfig;

import static io.ona.rdt.util.Constants.APP_VERSION;
import static io.ona.rdt.util.Constants.PHONE_MANUFACTURER;
import static io.ona.rdt.util.Constants.PHONE_MODEL;
import static io.ona.rdt.util.Constants.PHONE_OS_VERSION;

/**
 * Created by Vincent Karuri on 12/11/2019
 */
public class RDTApplicationPresenter {

    private Map<String, String> phoneProperties;

    public RDTApplicationPresenter() {
        phoneProperties = new HashMap<>();
    }

    public Map<String, String> getPhoneProperties() {
        if (phoneProperties.size() == 0) {
            phoneProperties.put(PHONE_MANUFACTURER, Build.MANUFACTURER);
            phoneProperties.put(PHONE_MODEL, Build.MODEL);
            phoneProperties.put(PHONE_OS_VERSION, Build.VERSION.RELEASE);
            phoneProperties.put(APP_VERSION, BuildConfig.VERSION_NAME);
        }
        return phoneProperties;
    }
}
