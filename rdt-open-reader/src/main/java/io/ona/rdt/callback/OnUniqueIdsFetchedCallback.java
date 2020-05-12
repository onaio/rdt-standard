package io.ona.rdt.callback;

import org.smartregister.domain.UniqueId;

import java.util.List;

import io.ona.rdt.util.FormLaunchArgs;

/**
 * Created by Vincent Karuri on 01/08/2019
 */
public interface OnUniqueIdsFetchedCallback {
    void onUniqueIdsFetched(FormLaunchArgs args, List<UniqueId> uniqueIds);
}
