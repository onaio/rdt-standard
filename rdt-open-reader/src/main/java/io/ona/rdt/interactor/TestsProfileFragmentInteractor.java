package io.ona.rdt.interactor;

import java.util.List;

import io.ona.rdt.application.RDTApplication;
import io.ona.rdt.domain.ParasiteProfileResult;
import io.ona.rdt.repository.ParasiteProfileRepository;

/**
 * Created by Vincent Karuri on 05/02/2020
 */
public class TestsProfileFragmentInteractor {

    private ParasiteProfileRepository parasiteProfileRepository;

    public TestsProfileFragmentInteractor() {
        this.parasiteProfileRepository = RDTApplication.getInstance().getParasiteProfileRepository();
    }

    public List<ParasiteProfileResult> getParasiteProfiles(String rdtId, String tableName, String experimentType) {
        return parasiteProfileRepository.getParasiteProfiles(rdtId, tableName, experimentType);
    }
}
