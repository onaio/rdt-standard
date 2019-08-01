package io.ona.rdt_app.callback;

import org.smartregister.domain.UniqueId;

import io.ona.rdt_app.util.FormLaunchArgs;

/**
 * Created by Vincent Karuri on 01/08/2019
 */
public interface OnUniqueIdFetchedCallback {
    void onUniqueIdFetched(FormLaunchArgs args, UniqueId uniqueId);
}
