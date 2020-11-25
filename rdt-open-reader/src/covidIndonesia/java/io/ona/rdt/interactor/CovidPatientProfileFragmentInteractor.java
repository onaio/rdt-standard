package io.ona.rdt.interactor;

import io.ona.rdt.util.CovidFormLauncherAndSaver;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileFragmentInteractor extends CovidFormLauncherAndSaver {

    @Override
    protected FormSaver getFormSaver() {
        if (formSaver == null) {
            formSaver = createFormSaver();
        }
        return formSaver;
    }

    @Override
    protected FormSaver createFormSaver() {
        return new CovidFormSaver();
    }
}
