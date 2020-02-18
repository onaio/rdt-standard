package io.ona.rdt.callback;

import java.util.List;

import io.ona.rdt.domain.ParasiteProfileResult;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public interface OnParasiteProfileFetchedCallback {
    void onParasiteProfileFetched(List<ParasiteProfileResult> parasiteProfileResults);
}
