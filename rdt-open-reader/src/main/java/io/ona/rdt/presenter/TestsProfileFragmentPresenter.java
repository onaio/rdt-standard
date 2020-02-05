package io.ona.rdt.presenter;

import io.ona.rdt.callback.OnParasiteProfileFetchedCallback;
import io.ona.rdt.contract.TestsProfileFragmentContract;
import io.ona.rdt.interactor.TestsProfileFragmentInteractor;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public class TestsProfileFragmentPresenter implements TestsProfileFragmentContract.Presenter {

    private TestsProfileFragmentInteractor interactor;

    public TestsProfileFragmentPresenter() {
        interactor = new TestsProfileFragmentInteractor();
    }

    @Override
    public void getParasiteProfiles(String rdtId, String tableName, String experimentType, OnParasiteProfileFetchedCallback callback) {
        interactor.getParasiteProfiles(rdtId, tableName, experimentType, callback);
    }
}
