package io.ona.rdt.presenter;

import java.util.List;

import io.ona.rdt.contract.TestsProfileFragmentContract;
import io.ona.rdt.domain.ParasiteProfileResult;
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
    public List<ParasiteProfileResult> getParasiteProfiles(String rdtId, String tableName, String experimentType) {
        return interactor.getParasiteProfiles(rdtId, tableName, experimentType);
    }
}
