package io.ona.rdt.presenter;

import io.ona.rdt.contract.PatientProfileActivityContract;
import io.ona.rdt.interactor.CovidPatientProfileActivityInteractor;
import io.ona.rdt.interactor.PatientProfileActivityInteractor;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 17/02/2020
 */
public class CovidPatientProfileActivityPresenter extends PatientProfileActivityPresenter implements PatientProfileActivityContract.Presenter {

    public CovidPatientProfileActivityPresenter(PatientProfileActivityContract.View activity) {
        super(activity);
    }

    @Override
    protected FormSaver createInteractor() {
        return new CovidPatientProfileActivityInteractor();
    }
}
