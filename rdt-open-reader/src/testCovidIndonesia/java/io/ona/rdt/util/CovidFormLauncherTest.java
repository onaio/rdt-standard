package io.ona.rdt.util;

import android.app.Activity;

import org.json.JSONException;

import io.ona.rdt.domain.Patient;

import static io.ona.rdt.util.Constants.Form.RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.COVID_RDT_TEST_FORM;
import static io.ona.rdt.util.CovidConstants.Form.SAMPLE_COLLECTION_FORM;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;

/**
 * Created by Vincent Karuri on 15/07/2020
 */
public class CovidFormLauncherTest extends BaseFormLauncherTest {

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
        formLauncher.launchForm(activity, COVID_RDT_TEST_FORM, patient);
        formLauncher.launchForm(activity, SAMPLE_COLLECTION_FORM, patient);
    }

    @Override
    protected void verifyUniqueIDsAreGenerated() {
        verify(formUtils, times(2)).getNextUniqueIds(formLaunchArgsArgumentCaptor.capture(), eq(formLauncher), eq(1));
    }
}
