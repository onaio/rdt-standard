package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Vincent Karuri on 07/08/2019
 */
public class FormLauncherTest extends BaseFormLauncherTest {

    @Override
    protected void verifyUniqueIDsAreGenerated() {
        verify(formUtils).getNextUniqueIds(formLaunchArgsArgumentCaptor.capture(), eq(formLauncher), eq(1));
    }

    @Override
    protected void launchForms(Activity activity, Patient patient) throws JSONException {
        formLauncher.launchForm(activity, RDT_TEST_FORM, patient);
    }

    @Override
    protected FormLauncher getFormLauncher() {
        return new FormLauncher();
    }

    @Override
    protected RDTJsonFormUtils getFormUtils() {
        return mock(RDTJsonFormUtils.class);
    }
}
