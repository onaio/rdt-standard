package io.ona.rdt.presenter;

import java.util.List;

import io.ona.rdt.contract.CovidPatientVisitFragmentContract;
import io.ona.rdt.domain.Visit;
import io.ona.rdt.interactor.CovidPatientVisitFragmentInteractor;

/**
 * Created by Vincent Karuri on 28/08/2020
 */
public class CovidPatientVisitFragmentPresenter implements CovidPatientVisitFragmentContract.Presenter {

    private CovidPatientVisitFragmentContract.View view;
    private CovidPatientVisitFragmentInteractor interactor;

    public CovidPatientVisitFragmentPresenter(CovidPatientVisitFragmentContract.View view) {
        this.view = view;
        this.interactor = new CovidPatientVisitFragmentInteractor(this);
    }

    public List<Visit> getPatientVisits(String baseEntityId) {
        return interactor.getPatientVisits(baseEntityId);
    }

    @Override
    public String translateString(int resourceId) {
        return view.translateString(resourceId);
    }
}
