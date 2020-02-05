package io.ona.rdt.contract;

import java.util.List;

import io.ona.rdt.callback.OnParasiteProfileFetchedCallback;
import io.ona.rdt.domain.ParasiteProfileResult;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public interface TestsProfileFragmentContract {
    interface View extends OnParasiteProfileFetchedCallback { }

    interface Presenter {
        List<ParasiteProfileResult> getParasiteProfiles(String rdtId, String tableName, String experimentType);
    }
}
