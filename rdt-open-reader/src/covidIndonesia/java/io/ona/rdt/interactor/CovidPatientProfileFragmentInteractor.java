package io.ona.rdt.interactor;

import io.ona.rdt.util.CovidFormLauncher;
import io.ona.rdt.util.CovidFormSaver;
import io.ona.rdt.util.FormLauncher;
import io.ona.rdt.util.FormLauncherAndSaver;
import io.ona.rdt.util.FormSaver;

/**
 * Created by Vincent Karuri on 15/06/2020
 */
public class CovidPatientProfileFragmentInteractor extends FormLauncherAndSaver {

    @Override
    protected FormLauncher createFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected FormSaver createFormSaver() {
        return new CovidFormSaver();
    }
}
