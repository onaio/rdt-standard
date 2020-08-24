package io.ona.rdt.presenter;

import java.util.List;

import io.ona.rdt.domain.Visit;
import io.ona.rdt.interactor.CovidPatientHistoryFragmentInteractor;

/**
 * Created by Vincent Karuri on 24/08/2020
 */
public class CovidPatientHistoryFragmentPresenter {

    private CovidPatientHistoryFragmentInteractor interactor;

    public CovidPatientHistoryFragmentPresenter() {
        interactor = new CovidPatientHistoryFragmentInteractor();
    }

    public List<Visit> getVisits(String baseEntityId) {
        return interactor.getVisits(baseEntityId);
    }
}
