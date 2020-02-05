package io.ona.rdt.contract;

import io.ona.rdt.callback.OnParasiteProfileFetchedCallback;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public interface TestsProfileFragmentContract {
    interface View extends OnParasiteProfileFetchedCallback { }

    interface Presenter {
        void getParasiteProfiles(String rdtId, String tableName, String experimentType, OnParasiteProfileFetchedCallback callback);
    }
}
