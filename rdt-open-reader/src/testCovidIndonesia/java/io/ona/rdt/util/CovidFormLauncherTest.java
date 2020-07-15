package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidFormLauncherTest extends FormLauncherTest {

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return mock(CovidRDTJsonFormUtils.class);
    }

    @Override
    protected FormLauncher getFormLauncher() {
        return new CovidFormLauncher();
    }

    @Override
    protected void launchForms(Activity activity, Patient patient) throws JSONException {
        super.launchForms(activity, patient);
        formLauncher.launchForm(activity, SAMPLE_COLLECTION_FORM, patient);
    }

    @Override
    protected void verifyUniqueIDsAreGenerated() {
        verify(formUtils, times(2)).getNextUniqueIds(formLaunchArgsArgumentCaptor.capture(), eq(formLauncher), eq(1));
    }
}
