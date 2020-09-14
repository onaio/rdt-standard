package io.ona.rdt.util;

/**
 * Created by Vincent Karuri on 10/09/2020
 */
public class CovidFormLauncherAndSaver extends FormLauncherAndSaver {

    @Override
    protected FormLauncher createFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected FormSaver createFormSaver() {
        return new CovidFormSaver();
    }
}
