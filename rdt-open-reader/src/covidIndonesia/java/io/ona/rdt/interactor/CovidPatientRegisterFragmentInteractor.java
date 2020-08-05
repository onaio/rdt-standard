package io.ona.rdt.interactor;

import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 05/08/2020
 */
public class CovidPatientRegisterFragmentInteractor extends PatientRegisterFragmentInteractor {

    @Override
    protected FormLauncher getFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected FormSaver getFormSaver() {
        return new CovidFormSaver();
    }
}
