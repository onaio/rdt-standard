package io.ona.rdt.interactor;

import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 17/07/2020
 */
public class CovidPatientRegisterActivityInteractor extends PatientRegisterActivityInteractor {

    @Override
    protected FormLauncher getFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected FormSaver getFormSaver() {
        return new CovidFormSaver();
    }
}
