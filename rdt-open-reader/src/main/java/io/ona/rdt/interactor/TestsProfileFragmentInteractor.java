package io.ona.rdt.interactor;

import io.ona.rdt.callback.OnParasiteProfileFetchedCallback;
import io.ona.rdt.repository.ParasiteProfileRepository;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public class TestsProfileFragmentInteractor {

    private ParasiteProfileRepository parasiteProfileRepository;

    public TestsProfileFragmentInteractor() {
        this.parasiteProfileRepository = new ParasiteProfileRepository();
    }

    public void getParasiteProfiles(String rdtId, String tableName, String experimentType, OnParasiteProfileFetchedCallback callback) {
        callback.onParasiteProfileFetched(parasiteProfileRepository.getParasiteProfiles(rdtId, tableName, experimentType));
    }
}
