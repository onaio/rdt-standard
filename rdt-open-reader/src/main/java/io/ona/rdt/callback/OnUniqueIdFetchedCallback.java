package io.ona.rdt.callback;

import org.smartregister.domain.UniqueId;

import io.ona.rdt.util.FormLaunchArgs;

/**
 * Created by Vincent Karuri on 01/08/2019
 */
public interface OnUniqueIdFetchedCallback {
    void onUniqueIdFetched(FormLaunchArgs args, UniqueId uniqueId);
}
